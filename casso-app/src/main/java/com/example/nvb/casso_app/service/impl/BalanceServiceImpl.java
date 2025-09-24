package com.example.nvb.casso_app.service.impl;

import com.example.nvb.casso_app.dto.request.BalanceRequest;
import com.example.nvb.casso_app.dto.response.AccountTransactionResponse;
import com.example.nvb.casso_app.dto.response.BalanceResponse;
import com.example.nvb.casso_app.service.BalanceService;
import com.example.nvb.casso_app.service.CallApiService;
import com.example.nvb.casso_app.service.TokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class BalanceServiceImpl implements BalanceService {
    CallApiService callApiService;
    TokenService tokenService;

    @NonFinal
    @Value("${casso-key.client-id}")
    private String CASSO_CLIENT_ID;

    @NonFinal
    @Value("${casso-key.secret-key}")
    private String CASSO_SECRET_KEY;

    @Override
    public BalanceResponse getBalance(String id) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-client-id", CASSO_CLIENT_ID);
        headers.put("x-secret-key", CASSO_SECRET_KEY);
        headers.put("Authorization", tokenService.getAccessToken(id));

        return callApiService.callApi(
                "https://sandbox.bankhub.dev/balance",
                HttpMethod.GET,
                headers,
                null,
                BalanceResponse.class
        );
    }
}
