package com.example.xujie.entity.mysql;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
@Entity
public @Data class MemberStatistics implements Serializable {
    @Id
    @Column(name = "id")
    private Long menberId;
    private String name;
    private String email;
}
