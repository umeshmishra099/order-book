package com.creditsuisse.orderbook.persistence;

import com.creditsuisse.orderbook.entity.order.Order;
import com.creditsuisse.orderbook.entity.order.OrderBook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OrderBookPersistenceManagerImplTests
{

  @Test
  public void givenOrderBookIdAndStatusWhenUpdateOrderBookStatusCalledThenStatusUpdatedSuccessfully()
  {
    OrderBookPersistenceManagerImpl orderBookPersistenceManagerImpl = new OrderBookPersistenceManagerImpl();
    String actualResponse = orderBookPersistenceManagerImpl.createOrderBook("Company");
    Long orderBookId = Long.valueOf(actualResponse.substring(actualResponse.lastIndexOf(":") + 1).trim());
    orderBookPersistenceManagerImpl.updateOrderBookStatus(orderBookId, OrderBook.OrderBookStatus.OPEN);
    List<OrderBook> orderBookList = orderBookPersistenceManagerImpl.getOrderBookList();
    assertEquals(1, orderBookList.size());
  }

  @Test
  public void givenInstrumentIdWhenCreateOrderBookCalledThenCreateOrderBookForInstrumentId()
  {
    OrderBookPersistenceManagerImpl orderBookPersistenceManagerImpl = new OrderBookPersistenceManagerImpl();
    String actualResponse = orderBookPersistenceManagerImpl.createOrderBook("Company1");
    Long orderBookId = Long.valueOf(actualResponse.substring(actualResponse.lastIndexOf(":") + 1).trim());
    assertEquals("New orderBook opened with OrderBookId : " + orderBookId, actualResponse);
  }

  @Test
  public void givenOrderWhenAddOrderThenOrderAddedSuccessfully()
  {
    OrderBookPersistenceManagerImpl orderBookPersistenceManagerImpl = new OrderBookPersistenceManagerImpl();
    String actualResponse = orderBookPersistenceManagerImpl.createOrderBook("Company2");
    Long orderBookId = Long.valueOf(actualResponse.substring(actualResponse.lastIndexOf(":") + 1).trim());
    Order order = new Order(orderBookId, 4L, new BigDecimal(5.0));
    String actualResponseAddOrder = orderBookPersistenceManagerImpl.addOrder(order);
    assertEquals("Order added successfully in orderBookId: "+orderBookId +", orderId: " + order.getOrderId(), actualResponseAddOrder);
  }
}