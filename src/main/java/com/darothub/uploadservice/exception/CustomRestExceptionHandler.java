package com.darothub.uploadservice.exception;


import com.darothub.uploadservice.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {


    @Qualifier("returnErrorResponse")
    @Autowired
    private ErrorResponse error;


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return buildResponseEntity(getValidationErrors(ex.getBindingResult().getFieldErrors()));

    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        return errorHandlerController(ex, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return errorHandlerController(ex, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        error.setMessage(NOT_FOUND.toString());
        error.setStatus(NOT_FOUND.value());
        error.setError(ex.getLocalizedMessage().replace("com.darothub.clientservice.entity.", ""));
        return buildResponseEntity(error);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> handleCustomException(
            CustomException ce) {
        return buildResponseEntity(ce.getError());
    }


    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElement(Exception ex) {
        return errorHandlerController(ex, NOT_FOUND);
    }


    private ResponseEntity<Object> buildResponseEntity(ErrorResponse error) {
        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
    }

    private ErrorResponse getValidationErrors(List<FieldError> fieldErrors) {

        error.setStatus(BAD_REQUEST.value());
        error.setMessage(BAD_REQUEST.toString());
        Map<String, String> errors = new HashMap<>();
        fieldErrors.forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        error.setError(errors);
        return error;
    }


    private ResponseEntity<Object> errorHandlerController(Exception ex, HttpStatus status) {
        error.setMessage(status.toString());
        error.setStatus(status.value());
        error.setError(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
    }
}
