package com.example.nvb.casso_app.mapper;

import com.example.nvb.casso_app.dto.response.ExchangeTokenResponse;
import com.example.nvb.casso_app.dto.response.GrantTokenResponse;
import com.example.nvb.casso_app.entity.Token;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {FiServiceMapper.class})
public interface TokenMapper {
    Token toEntity(GrantTokenResponse response);
    ExchangeTokenResponse toExchangeTokenResponse(Token token);
    GrantTokenResponse toGrantTokenResponse(Token token);
}
