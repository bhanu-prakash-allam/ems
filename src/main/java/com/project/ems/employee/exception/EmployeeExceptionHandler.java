package com.project.ems.employee.exception;

import com.fasterxml.jackson.core.JacksonException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class EmployeeExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<EmployeeException> handleAllException(Exception ex){

        EmployeeException exception=new EmployeeException(ex.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity<EmployeeException>(exception,HttpStatus.BAD_REQUEST);
    }
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        EmployeeException exception=new EmployeeException("Required request body is missing", LocalDateTime.now(), HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity<Object>(exception,HttpStatus.BAD_REQUEST);
    }
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String field = ex.getFieldError().getField();
        EmployeeException exception=new EmployeeException(field+" Shound not be null", LocalDateTime.now(), HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity<Object>(exception,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(JacksonException.class)
    protected ResponseEntity<EmployeeException> handleJsonConversion(JacksonException ex){

        EmployeeException exception=new EmployeeException(ex.getOriginalMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity<EmployeeException>(exception,HttpStatus.BAD_REQUEST);
    }

}
