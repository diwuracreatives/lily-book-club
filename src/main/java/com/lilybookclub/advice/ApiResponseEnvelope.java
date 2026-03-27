package com.lilybookclub.advice;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@Builder
public class ApiResponseEnvelope {
    private boolean successStatus;
    private LocalDateTime responseDate;
    private Object result;
    private List<Object> errorMessage;


    public static ApiResponseEnvelope buildErrorResponse(Object error) {
        return ApiResponseEnvelope.builder()
                .successStatus(false)
                .responseDate(LocalDateTime.now())
                .errorMessage(Collections.singletonList(error))
                .build();
    }
}
