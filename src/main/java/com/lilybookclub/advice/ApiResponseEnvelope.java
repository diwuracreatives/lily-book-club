package com.lilybookclub.advice;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ApiResponseEnvelope {
    private boolean successStatus;
    private LocalDateTime responseDate;
    private Object result;
    private List<Object> errorMessage;
}
