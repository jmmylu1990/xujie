package com.example.xujie.dto;

import lombok.Data;

import java.io.Serializable;

public @Data class MemberStatisticsDTO implements Serializable {
    private Long memberId;
    private String memberName;
    private String memberEmail;
}
