package com.example.nvb.casso_app.controller;

import com.example.nvb.casso_app.dto.request.ExchangeTokenRequest;
import com.example.nvb.casso_app.dto.request.GrantTokenRequest;
import com.example.nvb.casso_app.dto.response.ApiResponse;
import com.example.nvb.casso_app.dto.response.ExchangeTokenResponse;
import com.example.nvb.casso_app.dto.response.GrantTokenResponse;
import com.example.nvb.casso_app.service.TokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grant")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TokenController {
    TokenService tokenService;
    @PostMapping("/token")
    public ApiResponse<GrantTokenResponse> getGrantToken(@RequestBody GrantTokenRequest grantTokenRequest){
        return ApiResponse.<GrantTokenResponse>builder()
                .message("Grant token successfully")
                .code(200)
                .data(tokenService.getGrantToken(grantTokenRequest))
                .build();
    }

    @PostMapping("/exchange")
    public ApiResponse<ExchangeTokenResponse> exchangeToken(@RequestBody ExchangeTokenRequest exchangeTokenRequest){
        return ApiResponse.<ExchangeTokenResponse>builder()
                .message("Exchange token successfully")
                .code(200)
                .data(tokenService.exchangeToken(exchangeTokenRequest))
                .build();
    }

    @DeleteMapping("/token/{id}")
    public ApiResponse<Void> deleteToken(@PathVariable String id){
        tokenService.deleteAccessToken(id);
        return ApiResponse.<Void>builder()
                .code(204)
                .message("Token deleted successfully")
                .build();
    }
}
