package com.example.nvb.casso_app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "fi_service")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class FiService {
    @Id
    String id;
    String code;
    String name;
    String logo;
    String fiFullName;
}
