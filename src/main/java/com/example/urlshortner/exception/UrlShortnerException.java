package com.example.urlshortner.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class UrlShortnerException extends RuntimeException {
    private HttpStatus errorCode;

    public UrlShortnerException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
