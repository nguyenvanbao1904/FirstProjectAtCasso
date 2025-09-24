package com.example.nvb.casso_app.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CassoWebhookRequest {
    String environment;
    String webhookType;
    String webhookCode;
    String grantId;
    Transaction transaction;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Transaction {
        String id;
        String reference;
        Integer amount;
        String description;
        String accountNumber;
    }
}
