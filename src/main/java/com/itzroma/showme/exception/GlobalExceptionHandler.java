package com.itzroma.showme.exception;

import com.itzroma.showme.domain.dto.response.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponseDto> handleBadRequestException(BadRequestException e) {
        return createResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return createResponse(HttpStatus.BAD_REQUEST, "Bad Credentials");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleUsernameNotFoundException(Exception e) {
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }

    private ResponseEntity<ExceptionResponseDto> createResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new ExceptionResponseDto(httpStatus, message), httpStatus);
    }
}
