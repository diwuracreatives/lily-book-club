package com.lilybookclub.advice;

import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.exception.AlreadyExistException;
import com.lilybookclub.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
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

@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorControllerAdvice {

    private final Clock clock;

    private ApiResponseEnvelope buildErrorResponse(Object error) {
        return ApiResponseEnvelope.builder()
                .successStatus(false)
                .responseDate(LocalDateTime.now(clock))
                .errorMessage(Collections.singletonList(error))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResponseEnvelope handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> response = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
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
        return buildErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponseEnvelope handleResourceNotFound(NotFoundException ex) {
        return buildErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = AlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseEnvelope handleResourceAlreadyExist(AlreadyExistException ex) {
        return buildErrorResponse(ex.getMessage());
    }
}
