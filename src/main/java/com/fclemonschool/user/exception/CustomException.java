package com.fclemonschool.user.exception;

import lombok.Getter;

/**
 * 커스텀 Exception.
 */
@Getter
public class CustomException extends RuntimeException {
  private final ExceptionType exceptionType;

  public CustomException(ExceptionType exceptionType) {
    super(exceptionType.getMessage());
    this.exceptionType = exceptionType;
  }
}
