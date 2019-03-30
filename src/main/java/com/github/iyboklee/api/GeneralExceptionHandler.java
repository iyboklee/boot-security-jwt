/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.api;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.github.iyboklee.api.model.response.ApiResult;
import com.github.iyboklee.exception.ServiceRuntimeException;
import com.github.iyboklee.exception.UnauthorizedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {

    private ResponseEntity<ApiResult> createResponse(Throwable throwable, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(new ApiResult(throwable, status), headers, status);
    }

    //---------------------------------------------------------------
    //-- Spring/java.lang exception handler
    //---------------------------------------------------------------
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNotFoundException(Exception e) {
        return createResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IllegalArgumentException.class, TypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        return createResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotAllowedException(Exception e) {
        return createResponse(e, HttpStatus.METHOD_NOT_ALLOWED);
    }

    //---------------------------------------------------------------
    //-- ServiceRuntimeException handler
    //---------------------------------------------------------------
    @ExceptionHandler(ServiceRuntimeException.class)
    public ResponseEntity<?> handleServiceRuntimeException(ServiceRuntimeException e) {
        if (e instanceof UnauthorizedException)
            return createResponse(e, HttpStatus.UNAUTHORIZED);

        log.error(e.getMessage(), e);
        return createResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //---------------------------------------------------------------
    //-- Global exception handler
    //---------------------------------------------------------------
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return createResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}