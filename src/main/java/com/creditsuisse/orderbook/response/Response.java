package com.creditsuisse.orderbook.response;

public class Response
{
  private String message;

  public Response(String message)
  {
    this.message = message;
  }

  public String getMessage()
  {
    return message;
  }

}
