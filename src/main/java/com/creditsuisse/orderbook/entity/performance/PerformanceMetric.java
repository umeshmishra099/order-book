package com.creditsuisse.orderbook.entity.performance;

public class PerformanceMetric
{
  private Long numberOfOrderBookProcessed;
  private Long maximumProcessTime;
  private Long minimumProcessTime;
  private Long quantile95ProcessTime;

  public PerformanceMetric(Long numberOfOrderBookProcessed, Long maximumProcessTime, Long minimumProcessTime, Long quantile95ProcessTime)
  {
    this.numberOfOrderBookProcessed = numberOfOrderBookProcessed;
    this.maximumProcessTime = maximumProcessTime;
    this.minimumProcessTime = minimumProcessTime;
    this.quantile95ProcessTime = quantile95ProcessTime;
  }

  public Long getNumberOfOrderProcessed()
  {
    return numberOfOrderBookProcessed;
  }

  public Long getMaximumProcessTime()
  {
    return maximumProcessTime;
  }

  public Long getMinimumProcessTime()
  {
    return minimumProcessTime;
  }

  public Long getQuantile95ProcessTime()
  {
    return quantile95ProcessTime;
  }
}
