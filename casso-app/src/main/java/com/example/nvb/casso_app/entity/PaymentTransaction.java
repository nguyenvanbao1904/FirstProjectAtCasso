package com.example.nvb.casso_app.entity;

import com.example.nvb.casso_app.enums.PaymentTransactionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class PaymentTransaction {
    @Id
    String id;
    @Enumerated(EnumType.STRING)
    PaymentTransactionStatus status;
}
