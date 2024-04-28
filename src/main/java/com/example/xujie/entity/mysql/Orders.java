package com.example.xujie.entity.mysql;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ORDERS")
public @Data class Orders implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private Date purchaseDate;

    // 與會員關聯
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Members member;

}
