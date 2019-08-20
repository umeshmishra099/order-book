package com.creditsuisse.orderbook.controller;

import com.creditsuisse.orderbook.entity.execution.Execution;
import com.creditsuisse.orderbook.manager.OrderBookManager;
import com.creditsuisse.orderbook.request.OrderPlaced;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderBookController.class)
public class OrderBookControllerTests
{
  @Autowired
  private MockMvc mvc;

  @MockBean
  private OrderBookManager orderBookManager;

  @Test
  public void openCreateOrderBook() throws Exception
  {
    String instrumentId = "instrumentId";
    String response = "New orderBook opened with OrderBookId : 1";
    Mockito.when(orderBookManager.openCreateOrderBook(instrumentId)).thenReturn(response);
    MockHttpServletResponse mockHttpServletResponse = mvc.perform(post("/order-book/open/" + instrumentId)).andReturn().getResponse();
    ControllerTestUtil.validate(mockHttpServletResponse, "{\"message\":\"New orderBook opened with OrderBookId : 1\"}");
  }

  @Test
  public void closeOrderBook() throws Exception
  {
    Long orderBookId = 1L;
    String response = "OrderBookId: 1 closed, you can add executions now";
    Mockito.when(orderBookManager.closeOrderBook(orderBookId)).thenReturn(response);
    MockHttpServletResponse mockHttpServletResponse = mvc.perform(put("/order-book/close/" + orderBookId)).andReturn().getResponse();
    ControllerTestUtil.validate(mockHttpServletResponse, "{\"message\":\"OrderBookId: 1 closed, you can add executions now\"}");
  }

  @Test
  public void addOrder() throws Exception
  {
    Mockito.when(orderBookManager.addOrder(Mockito.any(OrderPlaced.class))).thenReturn("New orderBook opened with OrderBookId : 1");
    MockHttpServletResponse mockHttpServletResponse = mvc.perform(post("/order-book/acceptOrder")
            .content("{\"OrderBookId\": 1, \"quantity\": 10, \"price\": 22.0}").contentType(
                    MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse();
    ControllerTestUtil.validate(mockHttpServletResponse, "{\"message\":\"New orderBook opened with OrderBookId : 1\"}");
  }

  @Test
  public void addExecutions() throws Exception
  {
    Mockito.when(orderBookManager.addExecution(Mockito.any(Execution.class))).thenReturn(new ArrayList<>());
    MockHttpServletResponse mockHttpServletResponse = mvc.perform(
            post("/order-book/addExecution").content("{ \"orderBookId \" : 1, \"quantity\": 20, \"price\": 5}")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn().getResponse();
    ControllerTestUtil.validate(mockHttpServletResponse, "[]");
  }

}