package com.creditsuisse.orderbook.manager;

import com.creditsuisse.orderbook.entity.execution.Execution;
import com.creditsuisse.orderbook.entity.performance.PerformanceMetric;
import com.creditsuisse.orderbook.request.OrderPlaced;
import com.creditsuisse.orderbook.response.OrderBookStatistics;
import com.creditsuisse.orderbook.response.OrderProcessed;

import java.util.List;
import java.util.Map;

public interface OrderBookManager
{
  String openCreateOrderBook(String instrumentId);

  String closeOrderBook(Long orderBookId);

  String addOrder(OrderPlaced orderPlaced);

  List<OrderProcessed> addExecution(Execution execution);

  OrderProcessed getOrderDetail(Long orderId);

  Map<Long, OrderBookStatistics> OrderBookStatistics();

  PerformanceMetric getPerformanceMetric();
}
