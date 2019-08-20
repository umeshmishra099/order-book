package com.creditsuisse.orderbook.entity.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class Order
{
  private static Long counter = 1L;
  private Long orderId;
  private Long OrderBookId;
  private Long quantity;
  private LocalDateTime creationDateTime;
  private OrderType orderType;
  private BigDecimal price;

  public Order(Long orderBookId, Long quantity, BigDecimal price)
  {
    OrderBookId = orderBookId;
    this.quantity = quantity;
    this.price = price;
    this.orderType = OrderType.LIMIT;
    if (price.doubleValue() <= 0.0)
    {
      this.orderType = OrderType.MARKET;
    }
    this.creationDateTime = LocalDateTime.now();
    this.orderId = counter++;
  }

  public enum OrderType
  {
    MARKET,
    LIMIT
  }

  public Long getOrderId()
  {
    return orderId;
  }

  public Long getOrderBookId()
  {
    return OrderBookId;
  }

  public Long getQuantity()
  {
    return quantity;
  }

  public LocalDateTime getCreationTime()
  {
    return creationDateTime;
  }

  public OrderType getOrderType()
  {
    return orderType;
  }

  public BigDecimal getPrice()
  {
    return price;
  }
}
