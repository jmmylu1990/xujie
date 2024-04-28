package com.example.xujie.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCCESS(1, "System execution succeeded"),
    SYSTEM_FAILURE(2, "System failed"),
    NOT_FOUND(3, "Resource not found"),
    NO_CONTENT(4, "No content found");

    private final Integer code;
    private final String message;
    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
