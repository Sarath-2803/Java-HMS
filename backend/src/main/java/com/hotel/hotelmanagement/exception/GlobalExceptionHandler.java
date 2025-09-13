package com.hotel.hotelmanagement.exception;

import com.hotel.hotelmanagement.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFound(UserNotFoundException ex){
        ErrorResponseDto response = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(),ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserExists(UserExistsException ex){
        ErrorResponseDto response = new ErrorResponseDto(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(NotFoundException ex){
        ErrorResponseDto response = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(),ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
}
