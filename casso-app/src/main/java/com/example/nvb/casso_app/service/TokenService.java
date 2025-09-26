package com.example.nvb.casso_app.service;

import com.example.nvb.casso_app.dto.request.ExchangeTokenRequest;
import com.example.nvb.casso_app.dto.request.GrantTokenRequest;
import com.example.nvb.casso_app.dto.response.ExchangeTokenResponse;
import com.example.nvb.casso_app.dto.response.GrantTokenResponse;

public interface TokenService {
    GrantTokenResponse getGrantToken(GrantTokenRequest token);
    ExchangeTokenResponse exchangeToken(ExchangeTokenRequest request);
    ExchangeTokenResponse saveToken(ExchangeTokenResponse response);
    String getAccessToken(String id);
    void deleteAccessToken(String id, boolean isNeedCallApi);
    GrantTokenResponse saveGrantToken(GrantTokenResponse response);
    GrantTokenResponse getGrantToken(String id);
    GrantTokenResponse updateGrantToken(GrantTokenRequest token);
    GrantTokenResponse getGrantId(String id);

}
