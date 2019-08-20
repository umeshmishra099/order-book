package com.creditsuisse.orderbook.response;

import com.creditsuisse.orderbook.entity.order.Order;

import java.math.BigDecimal;
import java.util.Map;

public class OrderBookStatistics
{
  private Long numberOfOrders;
  private Long totalOrderedQuantity;
  private Long totalValidQuantity;
  private Long totalInvalidQuantity;
  private Order biggestOrder;
  private Order smallestOrder;
  private Order firstOrder;
  private Order lastOrder;
  private Long validOrdersCount;
  private Long invalidOrderCounts;
  private Map<BigDecimal, Long> totalLimitBreakDownTable;
  private Map<BigDecimal, Long> validLimitBreakDownTable;
  private Map<BigDecimal, Long> invalidLimitBreakDownTable;
  private Long totalExecutedQuantity;
  private BigDecimal executionPrice;

  public Long getNumberOfOrders()
  {
    return numberOfOrders;
  }

  public void setNumberOfOrders(Long numberOfOrders)
  {
    this.numberOfOrders = numberOfOrders;
  }

  public Long getTotalOrderedQuantity()
  {
    return totalOrderedQuantity;
  }

  public void setTotalOrderedQuantity(Long totalOrderedQuantity)
  {
    this.totalOrderedQuantity = totalOrderedQuantity;
  }

  public Long getTotalValidQuantity()
  {
    return totalValidQuantity;
  }

  public void setTotalValidQuantity(Long totalValidQuantity)
  {
    this.totalValidQuantity = totalValidQuantity;
  }

  public Long getTotalInvalidQuantity()
  {
    return totalInvalidQuantity;
  }

  public void setTotalInvalidQuantity(Long totalInvalidQuantity)
  {
    this.totalInvalidQuantity = totalInvalidQuantity;
  }

  public Order getBiggestOrder()
  {
    return biggestOrder;
  }

  public void setBiggestOrder(Order biggestOrder)
  {
    this.biggestOrder = biggestOrder;
  }

  public Order getSmallestOrder()
  {
    return smallestOrder;
  }

  public void setSmallestOrder(Order smallestOrder)
  {
    this.smallestOrder = smallestOrder;
  }

  public Order getFirstOrder()
  {
    return firstOrder;
  }

  public void setFirstOrder(Order firstOrder)
  {
    this.firstOrder = firstOrder;
  }

  public Order getLastOrder()
  {
    return lastOrder;
  }

  public void setLastOrder(Order lastOrder)
  {
    this.lastOrder = lastOrder;
  }

  public Long getValidOrdersCount()
  {
    return validOrdersCount;
  }

  public void setValidOrdersCount(Long validOrdersCount)
  {
    this.validOrdersCount = validOrdersCount;
  }

  public Long getInvalidOrderCounts()
  {
    return invalidOrderCounts;
  }

  public void setInvalidOrderCounts(Long invalidOrderCounts)
  {
    this.invalidOrderCounts = invalidOrderCounts;
  }

  public Map<BigDecimal, Long> getTotalLimitBreakDownTable()
  {
    return totalLimitBreakDownTable;
  }

  public void setTotalLimitBreakDownTable(Map<BigDecimal, Long> totalLimitBreakDownTable)
  {
    this.totalLimitBreakDownTable = totalLimitBreakDownTable;
  }

  public Map<BigDecimal, Long> getValidLimitBreakDownTable()
  {
    return validLimitBreakDownTable;
  }

  public void setValidLimitBreakDownTable(Map<BigDecimal, Long> validLimitBreakDownTable)
  {
    this.validLimitBreakDownTable = validLimitBreakDownTable;
  }

  public Map<BigDecimal, Long> getInvalidLimitBreakDownTable()
  {
    return invalidLimitBreakDownTable;
  }

  public void setInvalidLimitBreakDownTable(Map<BigDecimal, Long> invalidLimitBreakDownTable)
  {
    this.invalidLimitBreakDownTable = invalidLimitBreakDownTable;
  }

  public Long getTotalExecutedQuantity()
  {
    return totalExecutedQuantity;
  }

  public void setTotalExecutedQuantity(Long totalExecutedQuantity)
  {
    this.totalExecutedQuantity = totalExecutedQuantity;
  }

  public BigDecimal getExecutionPrice()
  {
    return executionPrice;
  }

  public void setExecutionPrice(BigDecimal executionPrice)
  {
    this.executionPrice = executionPrice;
  }
}
