package com.example.nvb.casso_app.controller;

import com.example.nvb.casso_app.dto.request.TransactionRequest;
import com.example.nvb.casso_app.dto.response.ApiResponse;
import com.example.nvb.casso_app.dto.response.TransactionResponse;
import com.example.nvb.casso_app.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TransactionController {
    TransactionService transactionService;

    @PostMapping
    public ApiResponse<List<TransactionResponse>> list(@RequestBody TransactionRequest request) {
        return ApiResponse.<List<TransactionResponse>>builder()
                .code(200)
                .message("Get transaction list successfully")
                .data(transactionService.getTransactions(request))
                .build();
    }
}
