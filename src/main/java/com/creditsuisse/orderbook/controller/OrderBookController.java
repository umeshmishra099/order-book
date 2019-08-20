package com.creditsuisse.orderbook.controller;

import com.creditsuisse.orderbook.entity.execution.Execution;
import com.creditsuisse.orderbook.manager.OrderBookManager;
import com.creditsuisse.orderbook.request.OrderPlaced;
import com.creditsuisse.orderbook.response.OrderProcessed;
import com.creditsuisse.orderbook.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/order-book")
public class OrderBookController
{
  private OrderBookManager orderBookManager;

  @Autowired
  public void setOrderBookManager(OrderBookManager orderBookManager)
  {
    this.orderBookManager = orderBookManager;
  }

  @RequestMapping(value = "/open/{instrumentId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> openCreateOrderBook(@PathVariable String instrumentId)
  {
    return new ResponseEntity<>(new Response(orderBookManager.openCreateOrderBook(instrumentId)), HttpStatus.OK);
  }

  @RequestMapping(value = "/close/{orderBookId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> closeOrderBook(@PathVariable Long orderBookId)
  {
    return new ResponseEntity<>(new Response(orderBookManager.closeOrderBook(orderBookId)), HttpStatus.OK);
  }

  @RequestMapping(value = "/acceptOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> addOrder(@RequestBody OrderPlaced orderPlaced)
  {
    return new ResponseEntity<>(new Response(orderBookManager.addOrder(orderPlaced)), HttpStatus.OK);
  }

  @RequestMapping(value = "/addExecution", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<OrderProcessed> addExecution(@RequestBody Execution execution)
  {
    return orderBookManager.addExecution(execution);
  }
}
