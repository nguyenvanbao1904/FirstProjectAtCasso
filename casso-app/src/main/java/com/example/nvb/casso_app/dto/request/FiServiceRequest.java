package com.example.nvb.casso_app.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FiServiceRequest {
    String id;
    String code;
    String name;
    String logo;
    String fiFullName;
}
