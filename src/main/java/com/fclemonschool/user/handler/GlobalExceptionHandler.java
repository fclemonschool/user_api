package com.fclemonschool.user.handler;

import static com.fclemonschool.user.handler.LoggingHandler.LOG_END;

import com.fclemonschool.user.exception.CustomException;
import com.fclemonschool.user.exception.ExceptionType;
import com.fclemonschool.user.model.response.ErrorResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 예외 처리를 위한 핸들러.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  /**
   * CustomException 에 대한 에러 처리.
   *
   * @param exception CustomException, EmptyResultDataAccessException
   * @return CustomException에 담긴 ExceptionType에 해당하는 응답값
   */
  @ExceptionHandler({CustomException.class})
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
    ErrorResponse errorResponse =
        ErrorResponse.builder().errorCode(exception.getExceptionType().getCode())
            .statusCode(exception.getExceptionType().getStatus().value())
            .errorMessage(exception.getExceptionType().getMessage()).build();
    return new ResponseEntity<>(errorResponse, exception.getExceptionType().getStatus());
  }

  /**
   * NoSuchElementException, EmptyResultDataAccessException 에 대한 에러 처리.
   *
   * @param exception NoSuchElementException, EmptyResultDataAccessException
   * @return 404, 해당하는 요소가 없습니다.
   */
  @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException exception) {
    ErrorResponse errorResponse =
        ErrorResponse.builder().errorCode(ExceptionType.NO_SUCH_ELEMENT_EXCEPTION.getCode())
            .statusCode(ExceptionType.NO_SUCH_ELEMENT_EXCEPTION.getStatus().value())
            .errorMessage(exception.getMessage()).build();
    return new ResponseEntity<>(errorResponse, ExceptionType.NO_SUCH_ELEMENT_EXCEPTION.getStatus());
  }

  /**
   * IllegalArgumentException에 대한 에러 처리.
   *
   * @param exception IllegalArgumentException
   * @return 400
   */
  @ExceptionHandler({IllegalArgumentException.class})
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleValidationException(
      IllegalArgumentException exception) {
    ErrorResponse errorResponse =
        ErrorResponse.builder().errorCode(ExceptionType.VALIDATION_EXCEPTION.getCode())
            .statusCode(ExceptionType.VALIDATION_EXCEPTION.getStatus().value())
            .errorMessage(exception.getMessage()).build();
    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * MethodArgumentNotValidException이 발생했을 때 오류에 대한 상세 메시지를 함께 처리한다.
   *
   * @param exception the exception
   * @param headers   the headers to be written to the response
   * @param status    the selected response status
   * @param request   the current request
   * @return 400, 잘못된 입력값이 있습니다, 오류에 대한 상세 메시지
   */
  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    exception.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    ErrorResponse errorResponse =
        ErrorResponse.builder().errorCode(ExceptionType.VALIDATION_EXCEPTION.getCode())
            .statusCode(ExceptionType.VALIDATION_EXCEPTION.getStatus().value())
            .errorMessage(ExceptionType.VALIDATION_EXCEPTION.getMessage()).detail(errors).build();
    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * 처리되지 않은 오류가 발생했을 때 처리한다.
   *
   * @param exception RuntimeException
   * @return 500, 서버에 에러가 발생했습니다.
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleAllUncaughtException(RuntimeException exception) {
    log.error(LOG_END);
    log.error("Message : {}", exception.getMessage());
    log.error("Cause : {}", Arrays.toString(exception.getStackTrace()));
    log.error(LOG_END);
    return ResponseEntity.internalServerError().body(
        ErrorResponse.builder().errorCode(ExceptionType.INTERNAL_SERVER_EXCEPTION.getCode())
            .statusCode(ExceptionType.INTERNAL_SERVER_EXCEPTION.getStatus().value())
            .errorMessage(ExceptionType.INTERNAL_SERVER_EXCEPTION.getMessage()).build());
  }
}
