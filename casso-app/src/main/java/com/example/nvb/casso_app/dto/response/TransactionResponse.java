package com.example.nvb.casso_app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionResponse {
    String reference;
    String transactionDateTime;
    Long amount;
    String description;
    String accountNumber;
}
