package com.example.nvb.casso_app.controller;

import com.example.nvb.casso_app.dto.response.ApiResponse;
import com.example.nvb.casso_app.dto.response.FiServiceResponse;
import com.example.nvb.casso_app.service.FiServiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fi-service")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FiServiceController {
    FiServiceService fiServiceService;
    @GetMapping()
    public ApiResponse<FiServiceResponse> list() {
        return ApiResponse.<FiServiceResponse>builder()
                .message("Get financial service success")
                .code(200)
                .data(fiServiceService.getFiServices())
                .build();
    }

    @GetMapping("/my-fi-service")
    public ApiResponse<FiServiceResponse> listMyFiService() {
        return ApiResponse.<FiServiceResponse>builder()
                .message("Get my financial service success")
                .code(200)
                .data(fiServiceService.getMyFiServices())
                .build();
    }
}