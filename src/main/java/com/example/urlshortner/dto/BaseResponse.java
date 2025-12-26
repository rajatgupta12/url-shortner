package com.example.urlshortner.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
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

