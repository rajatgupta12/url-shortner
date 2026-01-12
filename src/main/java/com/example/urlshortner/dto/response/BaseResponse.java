package com.example.urlshortner.dto.response;

import com.example.urlshortner.dto.error.BaseError;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    LocalDateTime localDateTime;
    Integer size;
    T body;
    List<BaseError> errors;

    public BaseResponse(T body) {
        this.body = body;
    }

    public void addError(BaseError baseError) {
        if (errors == null)     errors = new ArrayList<>();

        errors.add(baseError);
    }
}

