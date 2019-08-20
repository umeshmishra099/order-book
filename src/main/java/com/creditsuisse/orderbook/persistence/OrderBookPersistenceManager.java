package com.creditsuisse.orderbook.persistence;

import com.creditsuisse.orderbook.entity.execution.Execution;
import com.creditsuisse.orderbook.entity.order.Order;
import com.creditsuisse.orderbook.entity.order.OrderBook;
import com.creditsuisse.orderbook.response.OrderProcessed;

import java.util.List;
import java.util.Map;

import static com.creditsuisse.orderbook.entity.order.OrderBook.OrderBookStatus;

public interface OrderBookPersistenceManager
{
  void updateOrderBookStatus(Long orderBookId, OrderBookStatus orderBookStatus);

  String createOrderBook(String instrumentId);

  String addOrder(Order order);

  void addExecution(Execution execution);

  void setOrderBookExecutedTime(Long orderBookId);

  Map<String, OrderBook> getOrderBooks();

  List<OrderBook> getOrderBookList();

  List<Execution> getExecutions();

  Map<Long, List<OrderProcessed>> getOrderBookProcessedOrders();

}
