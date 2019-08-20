package com.creditsuisse.orderbook.persistence;

import com.creditsuisse.orderbook.entity.execution.Execution;
import com.creditsuisse.orderbook.entity.order.Order;
import com.creditsuisse.orderbook.entity.order.OrderBook;
import com.creditsuisse.orderbook.response.OrderProcessed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.creditsuisse.orderbook.entity.order.OrderBook.OrderBookStatus;

@Component
public class OrderBookPersistenceManagerImpl implements OrderBookPersistenceManager
{
  private static final Logger logger = LoggerFactory.getLogger(OrderBookPersistenceManagerImpl.class);

  private Map<String, OrderBook> orderBooks = new HashMap<>();
  private List<OrderBook> orderBookList = new ArrayList<>();
  private List<Execution> executions = new ArrayList<>();
  private Map<Long, List<OrderProcessed>> orderBookProcessedOrders = new HashMap<>();

  @Override
  public void updateOrderBookStatus(Long orderBookId, OrderBookStatus orderBookStatus)
  {
    logger.info("Updating status for OrderBookId: " + orderBookId + "to " + orderBookStatus);
    orderBookList.stream().filter(orderBook -> orderBook.getOrderBookId().equals(orderBookId)).collect(Collectors.toList()).get(0)
            .setOrderBookStatus(orderBookStatus);
  }

  @Override
  public String createOrderBook(String instrumentId)
  {
    OrderBook orderBook = new OrderBook(OrderBookStatus.OPEN);
    orderBooks.put(instrumentId, orderBook);
    orderBookList.add(orderBook);
    logger.info("New orderBook opened with OrderBookId : %s", orderBook.getOrderBookId());
    return String.format("New orderBook opened with OrderBookId : %s", orderBook.getOrderBookId());
  }

  @Override
  public String addOrder(Order order)
  {
    Long orderBookId = order.getOrderBookId();
    orderBookList.stream().filter(orderBook -> orderBook.getOrderBookId().equals(orderBookId)).collect(Collectors.toList()).get(0).addOrder(order);
    logger.info("Order added successfully in orderBookId: %s, orderId: %s", orderBookId, order.getOrderId());
    return String.format("Order added successfully in orderBookId: %s, orderId: %s", orderBookId, order.getOrderId());
  }

  @Override
  public void addExecution(Execution execution)
  {
    executions.add(execution);
  }

  @Override
  public void setOrderBookExecutedTime(Long orderBookId)
  {
    logger.info("Setting ExecutionTime for OrderBookId: " + orderBookId);
    orderBookList.stream().filter(orderBook -> orderBook.getOrderBookId().equals(orderBookId)).collect(Collectors.toList()).get(0)
            .setExecutedDateTime(LocalDateTime.now());
  }

  @Override
  public Map<String, OrderBook> getOrderBooks()
  {
    return orderBooks;
  }

  @Override
  public List<OrderBook> getOrderBookList()
  {
    return orderBookList;
  }

  @Override
  public List<Execution> getExecutions()
  {
    return executions;
  }

  @Override
  public Map<Long, List<OrderProcessed>> getOrderBookProcessedOrders()
  {
    return orderBookProcessedOrders;
  }

}
