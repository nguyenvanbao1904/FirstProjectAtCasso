package com.example.nvb.casso_app.service;

import com.example.nvb.casso_app.dto.request.TransactionRequest;
import com.example.nvb.casso_app.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    List<TransactionResponse> getTransactions(TransactionRequest request);
}
