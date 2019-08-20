package com.creditsuisse.orderbook.entity.execution;

import java.math.BigDecimal;

public class Execution
{
  private Long orderBookId;
  private Long quantity;
  private BigDecimal price;

  public Long getOrderBookId()
  {
    return orderBookId;
  }

  public void setOrderBookId(Long orderBookId)
  {
    this.orderBookId = orderBookId;
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
