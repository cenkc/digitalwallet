package com.cenkc.digitalwallet.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponseDTO {

    private LocalDateTime time;
    private String errorMessage;
    private Map<String, String> errors;
    private String uri;
    private String method;

    ExceptionResponseDTO(LocalDateTime time, String errorMessage, Map<String, String> errors, String uri, String method) {
        this.time = time;
        this.errorMessage = errorMessage;
        this.errors = errors;
        this.uri = uri;
        this.method = method;
    }

    public static ExceptionResponseDTOBuilder builder() {
        return new ExceptionResponseDTOBuilder();
    }

    public static class ExceptionResponseDTOBuilder {
        private LocalDateTime time;
        private String errorMessage;
        private Map<String, String> errors;
        private String uri;
        private String method;

        ExceptionResponseDTOBuilder() {
        }

        public ExceptionResponseDTOBuilder time(LocalDateTime time) {
            this.time = time;
            return this;
        }

        public ExceptionResponseDTOBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public ExceptionResponseDTOBuilder errors(Map<String, String> errors) {
            this.errors = errors;
            return this;
        }

        public ExceptionResponseDTOBuilder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public ExceptionResponseDTOBuilder method(String method) {
            this.method = method;
            return this;
        }

        public ExceptionResponseDTO build() {
            return new ExceptionResponseDTO(time, errorMessage, errors, uri, method);
        }

        public String toString() {
            return "ExceptionResponseDTO.ExceptionResponseDTOBuilder(time=" + this.time + ", errorMessage=" + this.errorMessage + ", errors=" + this.errors + ", uri=" + this.uri + ", method=" + this.method + ")";
        }
    }
}
