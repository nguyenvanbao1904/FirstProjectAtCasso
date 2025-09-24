package com.example.nvb.casso_app.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {
    Long amount;
    String description;
    String referenceNumber;
    String fiServiceId;
}
