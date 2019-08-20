package com.creditsuisse.orderbook.request;

import java.math.BigDecimal;

public class OrderPlaced
{
  private Long OrderBookId;
  private Long quantity;
  private BigDecimal price;

  public OrderPlaced()
  {

  }

  public OrderPlaced(Long orderBookId, Long quantity, BigDecimal price)
  {
    OrderBookId = orderBookId;
    this.quantity = quantity;
    this.price = price;
  }

  public Long getOrderBookId()
  {
    return OrderBookId;
  }

  public void setOrderBookId(Long orderBookId)
  {
    OrderBookId = orderBookId;
  }

  public Long getQuantity()
  {
    return quantity;
  }

  public void setQuantity(Long quantity)
  {
    this.quantity = quantity;
  }

  public BigDecimal getPrice()
  {
    return price;
  }

  public void setPrice(BigDecimal price)
  {
    this.price = price;
  }
}
