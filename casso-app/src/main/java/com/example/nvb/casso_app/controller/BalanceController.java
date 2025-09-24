package com.example.nvb.casso_app.controller;

import com.example.nvb.casso_app.dto.response.ApiResponse;
import com.example.nvb.casso_app.dto.response.BalanceResponse;
import com.example.nvb.casso_app.service.BalanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class BalanceController {

    BalanceService balanceService;

    @GetMapping("/{id}")
    public ApiResponse<BalanceResponse> getBalance(@PathVariable String id) {
        return ApiResponse.<BalanceResponse>builder()
                .code(200)
                .message("Get Balance Success")
                .data(balanceService.getBalance(id))
                .build();
    }
}
