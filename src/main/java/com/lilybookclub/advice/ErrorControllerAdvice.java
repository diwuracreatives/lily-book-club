package com.lilybookclub.advice;

import com.lilybookclub.enums.Category;
import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.exception.InternalServerErrorException;
import com.lilybookclub.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
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

        log.error("MethodArgumentNotValidException. Error is : {}", ex.getMessage(), ex);

        List<String> response = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            String result = String.format("%s: %s", fieldName, errorMessage);
            response.add(result);
        });
        return buildErrorResponse(response);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseEnvelope handleMessageNotReadableRequest(HttpMessageNotReadableException ex) {
        log.error("Invalid club category in request. Error is : {}", ex.getMessage(), ex);
        return buildErrorResponse("Invalid club category provided. Accepted categories are: "
                + Arrays.toString(Category.values()));
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
        log.error("ResourceNotFoundException. Error is : {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponseEnvelope handleAuthorizationDeniedExceptions(Exception ex) {
        log.error("AuthorizationDeniedException. Error is : {}", ex.getMessage(), ex);
        return buildErrorResponse("Access Denied: You are not authorized to perform this operation");
    }

    @ExceptionHandler(value = InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponseEnvelope handleInternalServerError(Exception ex) {
        log.error("InternalServerErrorException. Error is : {}", ex.getMessage(), ex);
        return buildErrorResponse("An Error occurred, Something went wrong on our end, Please try again later.");
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponseEnvelope handleServerError(Exception ex) {
        log.error("ErrorException. Error is : {}", ex.getMessage(), ex);
        return buildErrorResponse("An Error occurred, Something went wrong on our end.");
    }

}
