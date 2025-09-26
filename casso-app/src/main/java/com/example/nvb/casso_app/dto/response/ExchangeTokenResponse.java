package com.example.nvb.casso_app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeTokenResponse {
    String accessToken;
    String grantId;
    FiServiceResponse.FiServiceItem fiService;
}
