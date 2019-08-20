package com.creditsuisse.orderbook.response;

import java.math.BigDecimal;

import static com.creditsuisse.orderbook.entity.order.Order.OrderType;

public class OrderProcessed
{
  private Long orderId;
  private Long processedQuantity;
  private BigDecimal processedPrice;
  private OrderStatus orderStatus;
  private Long orderedQuantity;
  private BigDecimal orderPrice;
  private OrderType orderType;

  public enum OrderStatus
  {
    NEW,
    VALID,
    INVALID
  }

  public OrderProcessed(Long orderId, Long processedQuantity, BigDecimal processedPrice, OrderStatus orderStatus, Long orderedQuantity,
          BigDecimal orderPrice)
  {
    this.orderId = orderId;
    this.processedQuantity = processedQuantity;
    this.processedPrice = processedPrice;
    this.orderStatus = orderStatus;
    this.orderedQuantity = orderedQuantity;
    this.orderPrice = orderPrice;
    this.orderType = OrderType.LIMIT;
    if (orderPrice.doubleValue() <= 0.0)
    {
      this.orderType = OrderType.MARKET;
    }
  }

  public Long getOrderId()
  {
    return orderId;
  }

  public Long getProcessedQuantity()
  {
    return processedQuantity;
  }

  public void setProcessedQuantity(Long processedQuantity)
  {
    this.processedQuantity = processedQuantity;
  }

  public BigDecimal getProcessedPrice()
  {
    return processedPrice;
  }

  public void setProcessedPrice(BigDecimal processedPrice)
  {
    this.processedPrice = processedPrice;
  }

  public OrderStatus getOrderStatus()
  {
    return orderStatus;
  }

  public Long getOrderedQuantity()
  {
    return orderedQuantity;
  }

  public BigDecimal getOrderPrice()
  {
    return orderPrice;
  }

  public OrderType getOrderType()
  {
    return orderType;
  }
}
