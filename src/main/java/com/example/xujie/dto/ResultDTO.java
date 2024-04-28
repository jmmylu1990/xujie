package com.example.xujie.dto;

import lombok.Data;

import java.io.Serializable;

public @Data class ResultDTO<T> implements Serializable {
    private T data;

    private boolean Success;

    private Integer code;

    private String msg;



}
