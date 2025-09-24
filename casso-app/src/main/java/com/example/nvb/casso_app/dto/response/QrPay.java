package com.example.nvb.casso_app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QrPay {
    String bin;
    String amount;
    String description;
    String qrCode;
    String referenceNumber;
    String virtualAccountNumber;
}
