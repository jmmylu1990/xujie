package com.example.xujie.enums;

import lombok.Getter;

@Getter
public enum MemberEnum {
    MALE(1, "Male"),
    FEMALE(2, "Female");

    private final int code;
    private final String gender;

    MemberEnum(int code, String gender) {
        this.code = code;
        this.gender = gender;
    }

}
