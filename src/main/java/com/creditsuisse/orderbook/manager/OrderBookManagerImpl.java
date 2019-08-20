package com.creditsuisse.orderbook.manager;

import com.creditsuisse.orderbook.entity.execution.Execution;
import com.creditsuisse.orderbook.entity.order.Order;
import com.creditsuisse.orderbook.entity.order.OrderBook;
import com.creditsuisse.orderbook.entity.performance.PerformanceMetric;
import com.creditsuisse.orderbook.exception.OrderBookException;
import com.creditsuisse.orderbook.persistence.OrderBookPersistenceManager;
import com.creditsuisse.orderbook.request.OrderPlaced;
import com.creditsuisse.orderbook.response.OrderBookStatistics;
import com.creditsuisse.orderbook.response.OrderProcessed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.creditsuisse.orderbook.entity.order.Order.OrderType;
import static com.creditsuisse.orderbook.entity.order.OrderBook.OrderBookStatus;
import static com.creditsuisse.orderbook.response.OrderProcessed.OrderStatus;

@Component
public class OrderBookManagerImpl implements OrderBookManager
{
  private static final Logger logger = LoggerFactory.getLogger(OrderBookManagerImpl.class);

  private OrderBookPersistenceManager orderBookPersistenceManager;

  @Autowired
  public void setOrderBookPersistenceManager(OrderBookPersistenceManager orderBookPersistenceManager)
  {
    this.orderBookPersistenceManager = orderBookPersistenceManager;
  }

  @Override
  public synchronized String openCreateOrderBook(String instrumentId)
  {
    Map<String, OrderBook> orderBooks = orderBookPersistenceManager.getOrderBooks();
    if (orderBooks.containsKey(instrumentId))
    {
      OrderBook orderBook = orderBooks.get(instrumentId);
      Long orderBookId = orderBook.getOrderBookId();
      if (orderBook.getOrderBookStatus().equals(OrderBookStatus.OPEN))
      {
        String response = String.format("OrderBook already opened with OrderBookId : %s", orderBookId);
        logger.error(response);
        throw new OrderBookException(response);
      }
      else
      {
        orderBookPersistenceManager.updateOrderBookStatus(orderBookId, OrderBookStatus.OPEN);
        String response = String.format("OrderBook opened for OrderBookId : %s", orderBookId);
        logger.info(response);
        return response;
      }
    }
    else
    {
      return orderBookPersistenceManager.createOrderBook(instrumentId);
    }
  }

  @Override
  public synchronized String closeOrderBook(Long orderBookId)
  {
    List<OrderBook> orderBookList = orderBookPersistenceManager.getOrderBookList();
    List<OrderBook> availableOrderBook = orderBookList.stream().filter(orderBook -> orderBook.getOrderBookId().equals(orderBookId))
            .collect(Collectors.toList());
    if (availableOrderBook.size() > 0)
    {
      if (availableOrderBook.get(0).getOrderBookStatus().equals(OrderBookStatus.CLOSE))
      {
        String response = String.format("orderBookId: %s already closed", orderBookId);
        logger.error(response);
        throw new OrderBookException(response);
      }
      else
      {
        orderBookPersistenceManager.updateOrderBookStatus(orderBookId, OrderBookStatus.CLOSE);
        String response = String.format("OrderBookId: %s closed, you can add executions now", orderBookId);
        logger.info(response);
        return response;
      }
    }
    else
    {
      String response = String.format("OrderBookId: %s does not exist.", orderBookId);
      logger.error(response);
      throw new OrderBookException(response);
    }
  }

  @Override
  public synchronized String addOrder(OrderPlaced orderPlaced)
  {
    Order order = new Order(orderPlaced.getOrderBookId(), orderPlaced.getQuantity(), orderPlaced.getPrice());
    List<OrderBook> orderBookList = orderBookPersistenceManager.getOrderBookList();
    Long orderBookId = order.getOrderBookId();
    List<OrderBook> orderBooks = orderBookList.stream().filter(orderBook -> orderBook.getOrderBookId().equals(orderBookId))
            .collect(Collectors.toList());
    if (orderBooks.size() > 0)
    {
      if (orderBooks.get(0).getOrderBookStatus().equals(OrderBookStatus.CLOSE) || orderBooks.get(0).getOrderBookStatus()
              .equals(OrderBookStatus.EXECUTED))
      {
        String response = String.format("OrderBookId: %s is closed or executed. Please open OrderBook to add Order", orderBookId);
        logger.error(response);
        throw new OrderBookException(response);
      }
      else
      {
        return orderBookPersistenceManager.addOrder(order);
      }
    }
    else
    {
      String response = String.format("OrderBookId: %s doest not exist. Please create OrderBookId.", orderBookId);
      logger.error(response);
      throw new OrderBookException(response);
    }
  }

  @Override
  public synchronized List<OrderProcessed> addExecution(Execution execution)
  {
    Long orderBookId = execution.getOrderBookId();
    List<OrderBook> orderBookList = orderBookPersistenceManager.getOrderBookList();
    List<OrderBook> orderBooks = orderBookList.stream().filter(orderBook -> orderBook.getOrderBookId().equals(orderBookId))
            .collect(Collectors.toList());
    if (orderBooks.size() > 0)
    {
      if (orderBooks.get(0).getOrderBookStatus().equals(OrderBookStatus.OPEN))
      {
        String response = String.format("OrderBookId: %s is open. Please close OrderBook to add Execution", orderBookId);
        logger.error(response);
        throw new OrderBookException(response);
      }
      else if (orderBooks.get(0).getOrderBookStatus().equals(OrderBookStatus.EXECUTED))
      {
        String response = String.format("OrderBook execution completed for orderBookId: %s", orderBookId);
        logger.error(response);
        throw new OrderBookException(response);
      }
      else
      {
        validateExecution(orderBookId, execution.getPrice());
        orderBookPersistenceManager.addExecution(execution);
        validateOrders(orderBooks.get(0), execution);
        processOrders(execution);
        if (isOrderBookCompleted(orderBookId))
        {
          orderBookPersistenceManager.updateOrderBookStatus(orderBookId, OrderBookStatus.EXECUTED);
          orderBookPersistenceManager.setOrderBookExecutedTime(orderBookId);
        }
        return orderBookPersistenceManager.getOrderBookProcessedOrders().get(orderBookId);
      }
    }
    else
    {
      String response = String.format("OrderBookId: %s doest not exist. Please create OrderBookId.", orderBookId);
      logger.error(response);
      throw new OrderBookException(response);
    }
  }

  @Override
  public OrderProcessed getOrderDetail(Long orderId)
  {
    Collection<List<OrderProcessed>> values = orderBookPersistenceManager.getOrderBookProcessedOrders().values();
    for (List<OrderProcessed> orderProcessed : values)
    {
      List<OrderProcessed> collect = orderProcessed.stream().filter(order -> order.getOrderId().equals(orderId)).collect(Collectors.toList());
      if (collect.size() > 0)
      {
        return collect.get(0);
      }
    }

    for (OrderBook orderBook : orderBookPersistenceManager.getOrderBookList())
    {
      List<Order> collect = orderBook.getOrders().stream().filter(order -> order.getOrderId().equals(orderId)).collect(Collectors.toList());
      if (collect.size() > 0)
      {
        new OrderProcessed(collect.get(0).getOrderId(), 0L, new BigDecimal(0.0), OrderStatus.NEW, collect.get(0).getQuantity(),
                collect.get(0).getPrice());
      }
    }
    String response = String.format("Provided OrderId: %s doest not exist.", orderId);
    logger.error(response);
    throw new OrderBookException(response);
  }

  @Override
  public Map<Long, OrderBookStatistics> OrderBookStatistics()
  {
    Map<Long, OrderBookStatistics> orderBookStatistics = new HashMap<>();
    List<OrderBook> orderBookList = orderBookPersistenceManager.getOrderBookList();
    for (OrderBook orderBook : orderBookList)
    {
      OrderBookStatistics orderBookStatistic = new OrderBookStatistics();
      List<Order> orders = orderBook.getOrders();
      int totalOrdersCount = orders.size();
      if (totalOrdersCount > 0)
      {
        orderBookStatistic.setNumberOfOrders((long)totalOrdersCount);
        orders.sort(Comparator.comparingLong(Order::getQuantity));
        orderBookStatistic.setSmallestOrder(orders.get(0));
        orderBookStatistic.setBiggestOrder(orders.get(totalOrdersCount - 1));
        Map<Long, List<OrderProcessed>> orderBookProcessedOrders = orderBookPersistenceManager.getOrderBookProcessedOrders();

        if (orderBookProcessedOrders.size() > 0)
        {
          List<OrderProcessed> orderProcessed = orderBookProcessedOrders.get(orderBook.getOrderBookId());
          if (orderProcessed != null)
          {
            orderBookStatistic.setTotalLimitBreakDownTable(getLimitBreakDown(orderProcessed));
            List<OrderProcessed> orderBookValidOrders = orderProcessed.stream()
                    .filter(x -> x.getOrderStatus().equals(OrderStatus.VALID)).collect(Collectors.toList());
            orderBookStatistic.setValidOrdersCount((long)orderBookValidOrders.size());
            orderBookStatistic.setTotalValidQuantity(orderBookValidOrders.stream().mapToLong(OrderProcessed::getOrderedQuantity).sum());
            orderBookStatistic.setValidLimitBreakDownTable(getLimitBreakDown(orderBookValidOrders));

            List<OrderProcessed> orderBookInvalidOrders = orderProcessed.stream()
                    .filter(x -> x.getOrderStatus().equals(OrderStatus.INVALID)).collect(Collectors.toList());

            orderBookStatistic.setInvalidOrderCounts((long)orderBookInvalidOrders.size());
            orderBookStatistic.setInvalidLimitBreakDownTable(getLimitBreakDown(orderBookInvalidOrders));
            orderBookStatistic.setTotalInvalidQuantity(orderBookInvalidOrders.stream().mapToLong(OrderProcessed::getOrderedQuantity).sum());

            orderBookStatistic.setTotalExecutedQuantity(orderProcessed.stream().mapToLong(OrderProcessed::getProcessedQuantity).sum());
            orderBookStatistic.setExecutionPrice(orderProcessed.get(0).getProcessedPrice());
          }
        }
        orders.sort(Comparator.comparing(Order::getCreationTime));
        orderBookStatistic.setFirstOrder(orders.get(0));
        orderBookStatistic.setLastOrder(orders.get(totalOrdersCount - 1));
        orderBookStatistic.setTotalOrderedQuantity(orders.stream().mapToLong(Order::getQuantity).sum());
      }

      orderBookStatistics.put(orderBook.getOrderBookId(), orderBookStatistic);
    }
    return orderBookStatistics;
  }

  @Override
  public PerformanceMetric getPerformanceMetric()
  {
    Long totalNumberOfOrderBookProcessed = 0L;
    Long maximumProcessTime = 0L;
    Long minimumProcessTime = 0L;
    Long quantile95ProcessTime = 0L;
    List<OrderBook> executedOrderBook = orderBookPersistenceManager.getOrderBookList().stream()
            .filter(x -> x.getOrderBookStatus().equals(OrderBookStatus.EXECUTED))
            .collect(Collectors.toList());
    if (executedOrderBook.size() > 0)
    {
      List<Long> timeToProcessOrderBook = new ArrayList<>();
      totalNumberOfOrderBookProcessed = (long)executedOrderBook.size();
      executedOrderBook
              .forEach(x -> timeToProcessOrderBook.add(Math.abs(Duration.between(x.getCreationDateTime(), x.getExecutedDateTime()).toMillis())));
      maximumProcessTime = Collections.max(timeToProcessOrderBook);
      minimumProcessTime = Collections.min(timeToProcessOrderBook);
      Collections.sort(timeToProcessOrderBook);
      int Index = (int)Math.ceil((95 / (double)100) * (double)timeToProcessOrderBook.size());
      quantile95ProcessTime = timeToProcessOrderBook.get(Index - 1);
    }

    return new PerformanceMetric(totalNumberOfOrderBookProcessed, maximumProcessTime, minimumProcessTime, quantile95ProcessTime);
  }

  private void validateExecution(Long orderBookId, BigDecimal price)
  {
    List<Execution> executions = orderBookPersistenceManager.getExecutions().stream()
            .filter(execution -> execution.getOrderBookId().equals(orderBookId))
            .collect(Collectors.toList());
    if (executions.size() > 0 && !executions.get(0).getPrice().equals(price))
    {
      String response = String.format("All executions for OrderBookId: %s  must have same price", orderBookId);
      logger.error(response);
      throw new OrderBookException(response);
    }
  }

  private void validateOrders(OrderBook orderBook, Execution execution)
  {
    List<Order> orders = orderBook.getOrders();
    for (Order order : orders)
    {
      OrderProcessed orderProcessed;
      if (order.getOrderType().equals(OrderType.LIMIT) && order.getPrice().compareTo(execution.getPrice()) < 0)
      {
        orderProcessed = new OrderProcessed(order.getOrderId(), 0L, new BigDecimal(0.0), OrderStatus.INVALID, order.getQuantity(),
                order.getPrice());
      }
      else
      {
        orderProcessed = new OrderProcessed(order.getOrderId(), 0L, new BigDecimal(0.0), OrderStatus.VALID, order.getQuantity(),
                order.getPrice());
      }
      addToProcessedList(orderBook.getOrderBookId(), orderProcessed);
    }
  }

  private Map<BigDecimal, Long> getLimitBreakDown(List<OrderProcessed> orderBookValidOrders)
  {
    Map<BigDecimal, Long> limitBreakDownTable = new HashMap<>();
    for (OrderProcessed orderProcessed : orderBookValidOrders)
    {
      if (orderProcessed.getOrderType().equals(OrderType.LIMIT))
      {
        BigDecimal price = orderProcessed.getOrderPrice();
        Long quantity = orderProcessed.getOrderedQuantity();
        if (limitBreakDownTable.containsKey(price))
        {
          quantity = quantity + limitBreakDownTable.get(price);
        }
        limitBreakDownTable.put(price, quantity);
      }
    }
    return limitBreakDownTable;
  }

  private void addToProcessedList(Long orderBookId, OrderProcessed orderProcessed)
  {
    Map<Long, List<OrderProcessed>> orderBookProcessedOrders = orderBookPersistenceManager.getOrderBookProcessedOrders();
    if (orderBookProcessedOrders.containsKey(orderBookId))
    {
      List<OrderProcessed> orderProcesseds = orderBookProcessedOrders.get(orderBookId);
      if (orderProcesseds.stream().filter(x -> x.getOrderId().equals(orderProcessed.getOrderId())).collect(Collectors.toList()).size() == 0)
      {
        orderProcesseds.add(orderProcessed);
      }
      orderBookPersistenceManager.getOrderBookProcessedOrders().put(orderBookId, orderProcesseds);
    }
    else
    {
      List<OrderProcessed> OrderP = new ArrayList<>();
      OrderP.add(orderProcessed);
      orderBookPersistenceManager.getOrderBookProcessedOrders().put(orderBookId, OrderP);

    }
  }

  private void processOrders(Execution execution)
  {
    Long orderBookId = execution.getOrderBookId();

    for (Map.Entry<Long, List<OrderProcessed>> entry : orderBookPersistenceManager.getOrderBookProcessedOrders().entrySet())
    {
      if (entry.getKey().equals(orderBookId))
      {
        List<OrderProcessed> validOrderProcessed = entry.getValue().stream().filter(x -> x.getOrderStatus().equals(OrderStatus.VALID))
                .collect(Collectors.toList());
        if (validOrderProcessed.size() > 0)
        {
          Long totalQuantity = validOrderProcessed.stream().mapToLong(OrderProcessed::getOrderedQuantity).sum();
          Long availableQuantity = execution.getQuantity();
          Long processedQuantity = 0L;
          List<OrderProcessed> processedOrders = new ArrayList<>();
          for (OrderProcessed orderProcessed : validOrderProcessed)
          {
            Long allocatedQuantity = distributeQuantityLinearly(
                    totalQuantity - validOrderProcessed.stream().mapToLong(OrderProcessed::getProcessedQuantity).sum(),
                    orderProcessed.getOrderedQuantity() - orderProcessed.getProcessedQuantity(), availableQuantity);
            if (orderProcessed.getProcessedQuantity() + allocatedQuantity > orderProcessed.getOrderedQuantity())
            {
              orderProcessed.setProcessedQuantity(orderProcessed.getOrderedQuantity());
            }
            else if (processedQuantity + allocatedQuantity > availableQuantity)
            {
              orderProcessed.setProcessedQuantity(orderProcessed.getProcessedQuantity() + availableQuantity - processedQuantity);
            }
            else
            {
              orderProcessed.setProcessedQuantity(orderProcessed.getProcessedQuantity() + allocatedQuantity);
              processedQuantity = processedQuantity + allocatedQuantity;
            }
            orderProcessed.setProcessedPrice(execution.getPrice());

            processedOrders.add(orderProcessed);
          }
          processedOrders.addAll(entry.getValue().stream().filter(x -> x.getOrderStatus().equals(OrderStatus.INVALID)).collect(Collectors.toList()));
          orderBookPersistenceManager.getOrderBookProcessedOrders().put(orderBookId, processedOrders);
        }
      }
    }

  }

  private Long distributeQuantityLinearly(Long totalQuantity, Long orderQuantity, Long availableQuantity)
  {
    return Math.round(((double)(orderQuantity * availableQuantity) / totalQuantity));
  }

  private boolean isOrderBookCompleted(Long orderBookId)
  {
    Map<Long, List<OrderProcessed>> orderBookProcessedOrders = orderBookPersistenceManager.getOrderBookProcessedOrders();
    List<OrderProcessed> orderProcessedList = orderBookProcessedOrders.get(orderBookId);

    return orderProcessedList != null && orderProcessedList.stream().mapToLong(OrderProcessed::getProcessedQuantity).sum() == orderProcessedList
            .stream().filter(x -> x.getOrderStatus().equals(OrderStatus.VALID))
            .mapToLong(OrderProcessed::getOrderedQuantity)
            .sum();
  }
}
