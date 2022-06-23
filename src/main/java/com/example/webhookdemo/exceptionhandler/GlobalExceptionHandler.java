package com.example.webhookdemo.exceptionhandler;


import com.example.webhookdemo.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<ErrorResponse> getResponseForContentNotFoundException(ContentNotFoundException e, WebRequest request) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(getErrorDetails(request, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WebHookFrequencyException.class)
    public ResponseEntity<ErrorResponse> getResponseForContentNotFoundException(WebHookFrequencyException e, WebRequest request) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(getErrorDetails(request, e.getMessage()), HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
    }

    @ExceptionHandler(WebHookConflictException.class)
    public ResponseEntity<ErrorResponse> getResponseForContentNotFoundException(WebHookConflictException e, WebRequest request) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(getErrorDetails(request, e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> getResponseForGlobalExceptionHandler(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(getErrorDetails(request, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(getErrorDetails(request, ex.getMessage()), status);
    }

    private ErrorResponse getErrorDetails(WebRequest request, String message) {
        return ErrorResponse.builder()
            .message(message)
            .details(request.getDescription(false))
            .build();
    }
}