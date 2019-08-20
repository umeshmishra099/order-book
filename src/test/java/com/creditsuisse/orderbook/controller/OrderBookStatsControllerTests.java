package com.creditsuisse.orderbook.controller;

import com.creditsuisse.orderbook.entity.performance.PerformanceMetric;
import com.creditsuisse.orderbook.manager.OrderBookManager;
import com.creditsuisse.orderbook.response.OrderProcessed;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;

import static com.creditsuisse.orderbook.response.OrderProcessed.OrderStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderBookStatsController.class)
public class OrderBookStatsControllerTests
{
  @Autowired
  private MockMvc mvc;

  @MockBean
  private OrderBookManager orderBookManager;

  @Test
  public void orderDetail() throws Exception
  {
    String response = "{\"orderId\":1,\"processedQuantity\":0,\"processedPrice\":0,\"orderStatus\":\"NEW\",\"orderedQuantity\":10,\"orderPrice\":1,\"orderType\":\"LIMIT\"}";
    Mockito.when(orderBookManager.getOrderDetail(Mockito.anyLong()))
            .thenReturn(new OrderProcessed(1L, 0L, new BigDecimal(0.0), OrderStatus.NEW, 10L, new BigDecimal(1.0)));
    MockHttpServletResponse mockHttpServletResponse = mvc.perform(get("/order-book-statistics/order/10")).andReturn().getResponse();
    ControllerTestUtil.validate(mockHttpServletResponse, response);
  }

  @Test
  public void orderBookStatistics() throws Exception
  {
    String response = "{}";
    Mockito.when(orderBookManager.OrderBookStatistics()).thenReturn(new HashMap<>());
    MockHttpServletResponse mockHttpServletResponse = mvc.perform(get("/order-book-statistics/detail")).andReturn().getResponse();
    ControllerTestUtil.validate(mockHttpServletResponse, response);
  }

  @Test
  public void orderBookMetrics() throws Exception
  {
    String response = "{\"maximumProcessTime\":1234,\"minimumProcessTime\":123,\"quantile95ProcessTime\":1234,\"numberOfOrderProcessed\":1}";
    Mockito.when(orderBookManager.getPerformanceMetric()).thenReturn(new PerformanceMetric(1L, 1234L, 123L, 1234L));
    MockHttpServletResponse mockHttpServletResponse = mvc.perform(get("/order-book-statistics/metrics")).andReturn().getResponse();
    ControllerTestUtil.validate(mockHttpServletResponse, response);
  }
}