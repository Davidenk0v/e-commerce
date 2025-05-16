package com.ecommerce.payment.exception;

import java.io.IOException;

import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.ecommerce.payment.dto.response.ErrorResponseDto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@Hidden
public class GlobalExceptionHandler {

    private static final String INTERNAL_ERROR_MESSAGE = "Ha ocurrido un error inesperado. Nuestro equipo ha sido notificado y está trabajando en resolverlo. Por favor, inténtelo más tarde.";

    // Function to create ErrorResponseDto objects and return them in ResponseEntity objects
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
    	log.error("Illegal argument exception occurred", exception);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), webRequest);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handlerBadRequestException(BadRequestException exception, WebRequest webRequest) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), webRequest);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlerNotFoundException(NotFoundException exception, WebRequest webRequest) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage(), webRequest);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponseDto> handleIOException(IOException exception, WebRequest webRequest) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Problema de conexión o almacenamiento: " + exception.getMessage(), webRequest);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException exception, WebRequest webRequest) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), webRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handlerGlobalException(Exception exception, WebRequest webRequest) {
        log.error("An unexpected error occurred", exception);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR_MESSAGE, webRequest);
    }
    
    // Criteria API exceptions
    @ExceptionHandler(PropertyReferenceException.class) 
	public ResponseEntity<ErrorResponseDto> handlePropertyReferenceException(PropertyReferenceException exception, WebRequest webRequest) {
		log.error("Invalid request parameter", exception);
    	return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid query parameter", webRequest);
    }
}
