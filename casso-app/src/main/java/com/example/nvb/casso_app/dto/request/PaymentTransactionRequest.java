package com.example.nvb.casso_app.dto.request;

import com.example.nvb.casso_app.enums.PaymentTransactionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentTransactionRequest {
    String id;
    @Enumerated(EnumType.STRING)
    PaymentTransactionStatus status;
}
