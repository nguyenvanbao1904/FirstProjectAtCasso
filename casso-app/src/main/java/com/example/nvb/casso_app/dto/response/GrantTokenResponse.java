package com.example.nvb.casso_app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GrantTokenResponse {
    String grantToken;
    String expiration;
    FiServiceResponse.FiServiceItem fiService;
}
