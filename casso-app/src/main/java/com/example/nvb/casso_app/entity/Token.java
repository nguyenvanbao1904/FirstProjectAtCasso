package com.example.nvb.casso_app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(unique = true)
    String accessToken;
    @Column(unique = true)
    String grantToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fiservice_id")
    FiService fiService;
}
