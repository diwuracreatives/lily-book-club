package com.lilybookclub.advice;

import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.lilybookclub.advice.ApiResponseEnvelope.buildErrorResponse;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ErrorControllerAdvice {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResponseEnvelope handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException. Error is : {}", ex.getMessage(), ex);

        List<String> response = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            String result = String.format(fieldName,":", " ", errorMessage);
            response.add(result);
        });
        return buildErrorResponse(response);
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseEnvelope handleBadRequest(BadRequestException ex) {
        log.error("BadRequestException. Error is : {}", ex.getMessage(), ex);

        return buildErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponseEnvelope handleResourceNotFound(NotFoundException ex) {
        log.error("BadRequestException. Error is : {}", ex.getMessage(), ex);

        return buildErrorResponse(ex.getMessage());
    }

}
