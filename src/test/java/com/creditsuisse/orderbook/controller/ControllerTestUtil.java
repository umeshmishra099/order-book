package com.creditsuisse.orderbook.controller;

import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

class ControllerTestUtil
{
  static void validate(MockHttpServletResponse mockHttpServletResponse, String response) throws UnsupportedEncodingException
  {
    assertEquals(200, mockHttpServletResponse.getStatus());
    assertEquals(response, mockHttpServletResponse.getContentAsString());
  }
}
