package com.creditsuisse.orderbook.exception;

import com.creditsuisse.orderbook.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController
{
  @ExceptionHandler(OrderBookException.class)
  public ResponseEntity<Response> orderBookException(OrderBookException orderBookException)
  {
    return new ResponseEntity<>(new Response(orderBookException.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
