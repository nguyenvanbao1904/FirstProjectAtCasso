package com.example.nvb.casso_app.service.impl;

import com.example.nvb.casso_app.dto.request.TransactionRequest;
import com.example.nvb.casso_app.dto.response.AccountTransactionResponse;
import com.example.nvb.casso_app.dto.response.TransactionResponse;
import com.example.nvb.casso_app.service.CallApiService;
import com.example.nvb.casso_app.service.TokenService;
import com.example.nvb.casso_app.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    CallApiService callApiService;
    TokenService tokenService;

    @NonFinal
    @Value("${casso-key.client-id}")
    private String CASSO_CLIENT_ID;

    @NonFinal
    @Value("${casso-key.secret-key}")
    private String CASSO_SECRET_KEY;

    @Override
    public List<TransactionResponse> getTransactions(TransactionRequest request) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-client-id", CASSO_CLIENT_ID);
        headers.put("x-secret-key", CASSO_SECRET_KEY);
        headers.put("Authorization", tokenService.getAccessToken(request.getFiServiceId()));

        AccountTransactionResponse response = callApiService.callApi(
                "https://sandbox.bankhub.dev/transactions",
                HttpMethod.GET,
                headers,
                null,
                AccountTransactionResponse.class
        );
        return response.getTransactions();
    }
}
