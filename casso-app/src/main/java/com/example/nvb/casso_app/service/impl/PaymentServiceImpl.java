package com.example.nvb.casso_app.service.impl;

import com.example.nvb.casso_app.dto.request.PaymentRequest;
import com.example.nvb.casso_app.dto.request.PaymentTransactionRequest;
import com.example.nvb.casso_app.dto.response.PaymentResponse;
import com.example.nvb.casso_app.dto.response.PaymentTransactionResponse;
import com.example.nvb.casso_app.entity.PaymentTransaction;
import com.example.nvb.casso_app.enums.PaymentTransactionStatus;
import com.example.nvb.casso_app.mapper.PaymentTransactionMapper;
import com.example.nvb.casso_app.repository.PaymentTransactionRepository;
import com.example.nvb.casso_app.service.CallApiService;
import com.example.nvb.casso_app.service.PaymentService;
import com.example.nvb.casso_app.service.TokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    CallApiService callApiService;
    TokenService tokenService;
    PaymentTransactionMapper paymentTransactionMapper;
    PaymentTransactionRepository paymentTransactionRepository;

    @NonFinal
    @Value("${casso-key.client-id}")
    private String CASSO_CLIENT_ID;

    @NonFinal
    @Value("${casso-key.secret-key}")
    private String CASSO_SECRET_KEY;
    @Override
    public PaymentResponse createQrcode(PaymentRequest paymentRequest) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-client-id", CASSO_CLIENT_ID);
        headers.put("x-secret-key", CASSO_SECRET_KEY);
        headers.put("Authorization", tokenService.getAccessToken(paymentRequest.getFiServiceId()));

        Map<String, Object> body = new HashMap<>();
        body.put("amount", paymentRequest.getAmount());
        body.put("description", paymentRequest.getDescription());
        body.put("referenceNumber", paymentRequest.getReferenceNumber());

        this.savePaymentTransaction(PaymentTransactionRequest.builder()
                .id(paymentRequest.getReferenceNumber())
                .status(PaymentTransactionStatus.PENDING)
                .build());

        return callApiService.callApi(
                "https://sandbox.bankhub.dev/qr-pay",
                HttpMethod.POST,
                headers,
                body,
                PaymentResponse.class
        );
    }

    @Override
    public PaymentTransactionResponse savePaymentTransaction(PaymentTransactionRequest request) {
        return paymentTransactionMapper.toResponse(
                paymentTransactionRepository.save(
                        paymentTransactionMapper.toEntity(request)
                )
        );
    }

    @Override
    public PaymentTransactionResponse getPaymentTransactionById(String id) {
        PaymentTransaction paymentTransaction = paymentTransactionRepository.findById(id).orElse(null);
        return paymentTransactionMapper.toResponse(paymentTransaction);
    }

    @Override
    public String getPaymentStatus(String id) {
        PaymentTransaction paymentTransaction = paymentTransactionRepository.findById(id).orElse(null);
        return paymentTransaction != null ? paymentTransaction.getStatus().toString() : null;
    }
}
