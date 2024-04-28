package com.example.xujie.entity.mysql;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "members")
@NamedEntityGraph(name = "Member.orders", attributeNodes = @NamedAttributeNode("orders"))
public @Data class Members implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private Integer gender;
    private Integer age;

    // 訂單與會員的關聯
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Orders> orders;
}
