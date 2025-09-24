package com.example.nvb.casso_app.service;

import com.example.nvb.casso_app.dto.request.BalanceRequest;
import com.example.nvb.casso_app.dto.response.BalanceResponse;

public interface BalanceService {
    BalanceResponse getBalance(String id);
}
