package com.creditsuisse.orderbook.controller;

import com.creditsuisse.orderbook.entity.performance.PerformanceMetric;
import com.creditsuisse.orderbook.manager.OrderBookManager;
import com.creditsuisse.orderbook.response.OrderBookStatistics;
import com.creditsuisse.orderbook.response.OrderProcessed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/order-book-statistics")
public class OrderBookStatsController
{
  private OrderBookManager orderBookManager;

  @Autowired
  public void setOrderBookManager(OrderBookManager orderBookManager)
  {
    this.orderBookManager = orderBookManager;
  }

  @RequestMapping(value = "order/{orderId}", method = RequestMethod.GET)
  public ResponseEntity<OrderProcessed> orderDetail(@PathVariable Long orderId)
  {
    return new ResponseEntity<>(orderBookManager.getOrderDetail(orderId), HttpStatus.OK);
  }

  @RequestMapping(value = "/detail", method = RequestMethod.GET)
  public Map<Long, OrderBookStatistics> OrderBookStatistics()
  {
    return orderBookManager.OrderBookStatistics();
  }

  @RequestMapping(value = "/metrics", method = RequestMethod.GET)
  public ResponseEntity<PerformanceMetric> performance()
  {
    return new ResponseEntity<>(orderBookManager.getPerformanceMetric(), HttpStatus.OK);
  }
}
