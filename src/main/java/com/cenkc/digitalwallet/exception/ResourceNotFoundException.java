package com.cenkc.digitalwallet.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@NoArgsConstructor
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@EqualsAndHashCode(callSuper = true)
public class ResourceNotFoundException extends RuntimeException {

    private String message;

    public ResourceNotFoundException(String message) {
        this.message = message;
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }
}
