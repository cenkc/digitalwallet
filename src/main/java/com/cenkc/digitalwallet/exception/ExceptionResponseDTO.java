package com.cenkc.digitalwallet.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponseDTO {

    private LocalDateTime time;
    private String errorMessage;
    private Map<String, String> errors;
    private String uri;
    private String method;
}
