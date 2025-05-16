package com.ecommerce.cart.exception;

import com.ecommerce.cart.dto.error.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String INTERNAL_ERROR_MESSAGE = "An unexpected error occurred. Our team has been notified and will work to resolve the issue as soon as possible.";

    //  Function to create ErrorResponseDto objects and return them in ResponseEntity objects
    private ResponseEntity<ErrorResponseDto> buildErrorResponse(HttpStatus status, String message, WebRequest webRequest) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status(status.toString())
                .message(message)
                .details(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handlerArgumentException(IllegalArgumentException exception, WebRequest webRequest) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), webRequest);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handlerBadRequestException(BadRequestException exception, WebRequest webRequest) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), webRequest);
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlerNotFoundException(ChangeSetPersister.NotFoundException exception, WebRequest webRequest) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage(), webRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handlerGlobalException(Exception exception, WebRequest webRequest) {
        log.error("An unexpected error occurred", exception);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR_MESSAGE, webRequest);
    }
}
