package com.creditsuisse.orderbook.entity.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderBook
{
  private static Long counter = 1L;
  private Long orderBookId;
  private OrderBookStatus orderBookStatus;
  private List<Order> orders = new ArrayList<>();
  private LocalDateTime creationDateTime;
  private LocalDateTime executedDateTime;

  public OrderBook(OrderBookStatus orderBookStatus)
  {
    this.orderBookId = counter++;
    this.orderBookStatus = orderBookStatus;
    this.creationDateTime = LocalDateTime.now();
  }

  public enum OrderBookStatus
  {
    OPEN,
    CLOSE,
    EXECUTED
  }

  public Long getOrderBookId()
  {
    return orderBookId;
  }

  public List<Order> getOrders()
  {
    return orders;
  }

  public void addOrder(Order order)
  {
    this.orders.add(order);
  }

  public OrderBookStatus getOrderBookStatus()
  {
    return orderBookStatus;
  }

  public void setOrderBookStatus(OrderBookStatus orderBookStatus)
  {
    this.orderBookStatus = orderBookStatus;
  }

  public LocalDateTime getCreationDateTime()
  {
    return creationDateTime;
  }

  public LocalDateTime getExecutedDateTime()
  {
    return executedDateTime;
  }

  public void setExecutedDateTime(LocalDateTime executedDateTime)
  {
    this.executedDateTime = executedDateTime;
  }
}
