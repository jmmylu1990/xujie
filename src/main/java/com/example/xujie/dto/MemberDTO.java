package com.example.xujie.dto;

import lombok.Data;

import java.io.Serializable;

public @Data class MemberDTO implements Serializable {
    private Long id;
    private String name;
    private String email;
    private Integer gender;
    private Integer age;

}
