package com.example.nvb.casso_app.service.impl;

import com.example.nvb.casso_app.dto.request.ExchangeTokenRequest;
import com.example.nvb.casso_app.dto.request.GrantTokenRequest;
import com.example.nvb.casso_app.dto.response.ExchangeTokenResponse;
import com.example.nvb.casso_app.dto.response.GrantTokenResponse;
import com.example.nvb.casso_app.entity.Token;
import com.example.nvb.casso_app.mapper.TokenMapper;
import com.example.nvb.casso_app.repository.TokenRepository;
import com.example.nvb.casso_app.service.CallApiService;
import com.example.nvb.casso_app.service.FiServiceService;
import com.example.nvb.casso_app.service.TokenService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    CallApiService callApiService;
    TokenRepository tokenRepository;
    TokenMapper tokenMapper;
    StringEncryptor stringEncryptor;
    FiServiceService fiServiceService;

    @NonFinal
    @Value("${casso-key.client-id}")
    private String CASSO_CLIENT_ID;

    @NonFinal
    @Value("${casso-key.secret-key}")
    private String CASSO_SECRET_KEY;

    @Override
    public GrantTokenResponse getGrantToken(GrantTokenRequest request) {
        GrantTokenResponse grantToken = this.getGrantToken(request.getFiServiceId());
        if(grantToken == null) {
            Map<String, String> headers = new HashMap<>();
            headers.put("x-client-id", CASSO_CLIENT_ID);
            headers.put("x-secret-key", CASSO_SECRET_KEY);

            GrantTokenResponse token = callApiService.callApi("https://sandbox.bankhub.dev/grant/token",
                    HttpMethod.POST,
                    headers,
                    request,
                    GrantTokenResponse.class);
            token.setFiService(fiServiceService.getFiService(request.getFiServiceId()));
            token = this.saveGrantToken(token);
            token.setGrantToken(stringEncryptor.decrypt(token.getGrantToken()));
            return token;
        }
        grantToken.setGrantToken(stringEncryptor.decrypt(grantToken.getGrantToken()));
        return grantToken;
    }

    @Override
    public ExchangeTokenResponse exchangeToken(ExchangeTokenRequest request) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-client-id", CASSO_CLIENT_ID);
        headers.put("x-secret-key", CASSO_SECRET_KEY);

        ExchangeTokenResponse rs = callApiService.callApi("https://sandbox.bankhub.dev/grant/exchange",
                HttpMethod.POST,
                headers,
                request,
                ExchangeTokenResponse.class);
        rs.setFiService(fiServiceService.getFiService(request.getFiServiceId()));
        return saveToken(rs);
    }

    @Override
    public ExchangeTokenResponse saveToken(ExchangeTokenResponse response) {
        Token token = tokenRepository.findByFiService_Id(response.getFiService().getId()).orElse(null);
        if(token != null) {
            token.setAccessToken(stringEncryptor.encrypt(response.getAccessToken()));
            token.setGrantId(stringEncryptor.encrypt(response.getGrantId()));
            return tokenMapper.toExchangeTokenResponse(tokenRepository.save(token));
        }
        return null;
    }

    @Override
    public String getAccessToken(String id) {
        return tokenRepository.findByFiService_Id(id)
                .map(token -> stringEncryptor.decrypt(token.getAccessToken()))
                .orElse(null);
    }

    @Override
    @Transactional
    public void deleteAccessToken(String id, boolean isNeedCallApi) {
        String accessToken = getAccessToken(id);
        Map<String, String> headers = new HashMap<>();
        headers.put("x-client-id", CASSO_CLIENT_ID);
        headers.put("x-secret-key", CASSO_SECRET_KEY);
        headers.put("Authorization", accessToken);
        Map<String, String> body = new HashMap<>();
        body.put("redirectUri", "http://localhost:3000/success");

        if (isNeedCallApi){
            callApiService.callApi(
                    "https://sandbox.bankhub.dev/grant/remove",
                    HttpMethod.POST,
                    headers,
                    body,
                    Object.class
            );
        }
        tokenRepository.deleteByFiService_Id(id);
    }

    @Override
    public GrantTokenResponse saveGrantToken(GrantTokenResponse response) {
        Token token = tokenMapper.toEntity(response);
        token.setGrantToken(stringEncryptor.encrypt(token.getGrantToken()));
        return tokenMapper.toGrantTokenResponse(tokenRepository.save(token));
    }

    @Override
    public GrantTokenResponse getGrantToken(String id) {
        Token token = tokenRepository.findByFiService_Id(id).orElse(null);

        if(token == null) {
          return null;
        }
        token.setGrantToken(stringEncryptor.decrypt(token.getGrantToken()));
        return tokenMapper.toGrantTokenResponse(token);
    }

    @Override
    public GrantTokenResponse updateGrantToken(GrantTokenRequest request) {
        String accessToken = getAccessToken(request.getFiServiceId());
        Map<String, String> headers = new HashMap<>();
        headers.put("x-client-id", CASSO_CLIENT_ID);
        headers.put("x-secret-key", CASSO_SECRET_KEY);
        headers.put("Authorization", accessToken);
        Token currentToken =  tokenRepository.findByFiService_Id(request.getFiServiceId()).orElse(null);
        if(currentToken == null) {
            return null;
        }

        GrantTokenResponse token = callApiService.callApi("https://sandbox.bankhub.dev/grant/token",
                HttpMethod.POST,
                headers,
                request,
                GrantTokenResponse.class);
        currentToken.setGrantToken(stringEncryptor.encrypt(token.getGrantToken()));
        tokenRepository.save(currentToken);
        return token;
    }

    @Override
    public GrantTokenResponse getGrantId(String id) {
        Token token = tokenRepository.findAll().stream()
                .filter(t -> id.equals(stringEncryptor.decrypt(t.getGrantId())))
                .findFirst()
                .orElse(null);

        if (token == null) {
            return null;
        }
        token.setGrantId(stringEncryptor.decrypt(token.getGrantId()));
        return tokenMapper.toGrantTokenResponse(token);
    }
}