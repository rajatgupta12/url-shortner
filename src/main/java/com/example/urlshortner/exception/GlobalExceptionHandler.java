package com.example.urlshortner.exception;

import com.example.urlshortner.dto.BaseError;
import com.example.urlshortner.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlShortnerException.class)
    public ResponseEntity<?> handleException(UrlShortnerException e) {
        BaseResponse<?> baseResponse = new BaseResponse<>();
        BaseError baseError = new BaseError(e.getMessage(), e.getErrorCode().value());
        baseResponse.addError(baseError);

        return ResponseEntity.status(e.getErrorCode()).body(baseResponse);
    }
}
