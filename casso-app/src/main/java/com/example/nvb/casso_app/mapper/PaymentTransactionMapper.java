package com.example.nvb.casso_app.mapper;

import com.example.nvb.casso_app.dto.request.PaymentTransactionRequest;
import com.example.nvb.casso_app.dto.response.PaymentTransactionResponse;
import com.example.nvb.casso_app.entity.PaymentTransaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentTransactionMapper {
    PaymentTransaction toEntity(PaymentTransactionRequest paymentTransactionRequest);
    PaymentTransactionResponse toResponse(PaymentTransaction paymentTransaction);
    PaymentTransactionRequest toRequest(PaymentTransactionResponse paymentTransactionResponse);
}
