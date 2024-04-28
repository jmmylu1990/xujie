package com.example.xujie.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

public @Data class OrderDTO implements Serializable {
    private String orderNumber;
    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC+8")
    private Date purchaseDate;
}
