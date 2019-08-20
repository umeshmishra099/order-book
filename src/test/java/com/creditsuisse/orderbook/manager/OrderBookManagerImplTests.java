package com.creditsuisse.orderbook.manager;

import com.creditsuisse.orderbook.entity.execution.Execution;
import com.creditsuisse.orderbook.entity.order.Order;
import com.creditsuisse.orderbook.entity.order.OrderBook;
import com.creditsuisse.orderbook.exception.OrderBookException;
import com.creditsuisse.orderbook.persistence.OrderBookPersistenceManager;
import com.creditsuisse.orderbook.request.OrderPlaced;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static com.creditsuisse.orderbook.entity.order.OrderBook.OrderBookStatus;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderBookManagerImplTests
{
  @Mock
  private OrderBookPersistenceManager orderBookPersistenceManager;

  @InjectMocks
  private OrderBookManagerImpl orderBookManager;

  @Test
  public void givenInstrumentIdDoesNotExistWhenOpenCreateCalledThenReturnSuccessAndOpenCreateMethodCalledOnce() throws OrderBookException
  {
    String expectedResponse = "New orderBook opened with OrderBookId : 1";
    String instrumentId = "company";
    when(orderBookPersistenceManager.getOrderBooks()).thenReturn(new HashMap<>());
    when(orderBookPersistenceManager.createOrderBook(instrumentId)).thenReturn(expectedResponse);
    String createdResponse = orderBookManager.openCreateOrderBook(instrumentId);
    verify(orderBookPersistenceManager, times(1)).getOrderBooks();
    verify(orderBookPersistenceManager, times(1)).createOrderBook(instrumentId);
    assertEquals(expectedResponse, createdResponse);
  }

  @Test(expected = OrderBookException.class)
  public void givenInstrumentIdExistWithOpenStatusWhenOpenCreateCalledThenThrowOrderBookException() throws OrderBookException
  {
    String instrumentId = "company";
    HashMap<String, OrderBook> orderBooks = new HashMap<>();
    orderBooks.put(instrumentId, new OrderBook(OrderBookStatus.OPEN));
    when(orderBookPersistenceManager.getOrderBooks()).thenReturn(orderBooks);
    orderBookManager.openCreateOrderBook(instrumentId);
  }

  @Test
  public void givenInstrumentIdExistWithCloseStatusWhenOpenCreateCalledThenReturnSuccess() throws OrderBookException
  {
    String instrumentId = "company";
    Map<String, OrderBook> closedOrderBook = new HashMap<>();
    closedOrderBook.put(instrumentId, new OrderBook(OrderBookStatus.CLOSE));
    when(orderBookPersistenceManager.getOrderBooks()).thenReturn(closedOrderBook);
    doNothing().when(orderBookPersistenceManager).updateOrderBookStatus(closedOrderBook.get(instrumentId).getOrderBookId(), OrderBookStatus.OPEN);
    orderBookManager.openCreateOrderBook(instrumentId);
    verify(orderBookPersistenceManager, times(1)).getOrderBooks();
    verify(orderBookPersistenceManager, times(1)).updateOrderBookStatus(closedOrderBook.get(instrumentId).getOrderBookId(), OrderBookStatus.OPEN);
  }

  @Test(expected = OrderBookException.class)
  public void givenOrderBookIdDoestNotExistWhenCloseCalledThenReturnThrowOrderBookException() throws OrderBookException
  {
    Long orderBookId = 3L;
    orderBookManager.closeOrderBook(orderBookId);
  }

  @Test(expected = OrderBookException.class)
  public void givenOrderBookIdStatusClosedWhenCloseCalledThenReturnThrowOrderBookException() throws OrderBookException
  {
    Long orderBookId = 0L;
    orderBookManager.closeOrderBook(orderBookId);
  }

  @Test(expected = OrderBookException.class)
  public void givenOrderWithOrderBookIdNotExistWhenAddOrderCalledThrowOrderBookException() throws OrderBookException
  {
    when(orderBookPersistenceManager.getOrderBookList()).thenReturn(new ArrayList<>());
    orderBookManager.addOrder(new OrderPlaced(1L, 10L, new BigDecimal(5.0)));
  }

  @Test(expected = OrderBookException.class)
  public void givenOrderWithOrderBookIdClosedWhenAddOrderCalledThrowOrderBookException() throws OrderBookException
  {
    List<OrderBook> orderBooks = new ArrayList<>();
    orderBooks.add(new OrderBook(OrderBookStatus.CLOSE));
    when(orderBookPersistenceManager.getOrderBookList()).thenReturn(orderBooks);
    orderBookManager.addOrder(new OrderPlaced(1L, 10L, new BigDecimal(5.0)));
  }


  @Test(expected = OrderBookException.class)
  public void givenOrderWithOrderBookIdExecutedWhenAddOrderCalledThrowOrderBookException() throws OrderBookException
  {
    List<OrderBook> orderBooks = new ArrayList<>();
    orderBooks.add(new OrderBook(OrderBookStatus.EXECUTED));
    when(orderBookPersistenceManager.getOrderBookList()).thenReturn(orderBooks);
    orderBookManager.addOrder(new OrderPlaced(1L, 10L, new BigDecimal(5.0)));
  }

  @Test
  public void givenOrderWithOrderBookIdOpenWhenAddOrderCalledOrderAddedSuccessfully() throws OrderBookException
  {
    List<OrderBook> orderBooks = new ArrayList<>();
    OrderBook orderBook = new OrderBook(OrderBookStatus.OPEN);
    orderBooks.add(orderBook);
    when(orderBookPersistenceManager.getOrderBookList()).thenReturn(orderBooks);
    OrderPlaced orderPlaced = new OrderPlaced(orderBook.getOrderBookId(), 10L, new BigDecimal(5.0));
    when(orderBookPersistenceManager.addOrder(any(Order.class))).thenReturn("hello");
    orderBookManager.addOrder(orderPlaced);
    verify(orderBookPersistenceManager, times(1)).getOrderBookList();
    verify(orderBookPersistenceManager, times(1)).addOrder(any(Order.class));
  }

  @Test(expected = OrderBookException.class)
  public void givenExecutionWithOrderBookIdDoestNotExistWhenAddExecutionCalledThenReturnException() throws OrderBookException
  {
    Execution execution = new Execution();
    execution.setOrderBookId(0L);
    when(orderBookPersistenceManager.getOrderBookList()).thenReturn(new ArrayList<>());
    orderBookManager.addExecution(execution);
  }

  @Test(expected = OrderBookException.class)
  public void givenExecutionWithOrderBookIdOpenWhenAddExecutionCalledThenReturnException() throws OrderBookException
  {
    List<OrderBook> orderBooks = new ArrayList<>();
    OrderBook orderBook = new OrderBook(OrderBookStatus.OPEN);
    orderBooks.add(orderBook);
    Execution execution = new Execution();
    execution.setOrderBookId(orderBook.getOrderBookId());
    when(orderBookPersistenceManager.getOrderBookList()).thenReturn(orderBooks);
    orderBookManager.addExecution(execution);
  }

  @Test(expected = OrderBookException.class)
  public void givenExecutionWithOrderBookIdExecutedWhenAddExecutionCalledThenReturnException() throws OrderBookException
  {
    List<OrderBook> orderBooks = new ArrayList<>();
    OrderBook orderBook = new OrderBook(OrderBookStatus.EXECUTED);
    orderBooks.add(orderBook);
    Execution execution = new Execution();
    execution.setOrderBookId(orderBook.getOrderBookId());
    when(orderBookPersistenceManager.getOrderBookList()).thenReturn(orderBooks);
    orderBookManager.addExecution(execution);
  }

  @Test(expected = OrderBookException.class)
  public void givenTwoExecutionsWithDifferentPriceWhenAddExecutionCalledThenReturnException() throws OrderBookException
  {
    List<OrderBook> orderBooks = new ArrayList<>();
    OrderBook orderBook = new OrderBook(OrderBookStatus.CLOSE);
    orderBooks.add(orderBook);
    Execution execution1 = new Execution();
    execution1.setOrderBookId(orderBook.getOrderBookId());
    execution1.setPrice(new BigDecimal(4.0));
    when(orderBookPersistenceManager.getOrderBookList()).thenReturn(orderBooks);
    when(orderBookPersistenceManager.getExecutions()).thenReturn(Collections.singletonList(execution1));
    Execution execution2 = new Execution();
    execution2.setOrderBookId(orderBook.getOrderBookId());
    execution2.setPrice(new BigDecimal(5.0));
    orderBookManager.addExecution(execution2);
  }

  @Test
  public void givenExecutionWithOrderBookIdCloseWhenAddExecutionCalledThenReturnSuccess() throws OrderBookException
  {
    List<OrderBook> orderBooks = new ArrayList<>();
    OrderBook orderBook = new OrderBook(OrderBookStatus.CLOSE);
    orderBook.addOrder(new Order(orderBook.getOrderBookId(), 5L, new BigDecimal(10.0)));
    orderBooks.add(orderBook);
    Execution execution = new Execution();
    execution.setPrice(new BigDecimal(6.0));
    execution.setQuantity(5L);
    execution.setOrderBookId(orderBook.getOrderBookId());
    when(orderBookPersistenceManager.getOrderBookList()).thenReturn(orderBooks);
    when(orderBookPersistenceManager.getOrderBookProcessedOrders()).thenReturn(new HashMap<>());
    orderBookManager.addExecution(execution);
    verify(orderBookPersistenceManager, times(1)).getOrderBookList();
    verify(orderBookPersistenceManager, times(6)).getOrderBookProcessedOrders();
  }

}