package com.example.nvb.casso_app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FiServiceResponse {
    private List<FiServiceItem> fiServices;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class FiServiceItem {
        String id;
        String code;
        String name;
        String logo;
        String fiFullName;
    }
}
