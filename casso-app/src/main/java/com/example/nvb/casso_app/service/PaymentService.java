package com.example.nvb.casso_app.service;

import com.example.nvb.casso_app.dto.request.PaymentRequest;
import com.example.nvb.casso_app.dto.request.PaymentTransactionRequest;
import com.example.nvb.casso_app.dto.response.PaymentResponse;
import com.example.nvb.casso_app.dto.response.PaymentTransactionResponse;

public interface PaymentService {
    PaymentResponse createQrcode(PaymentRequest paymentRequest);
    PaymentTransactionResponse savePaymentTransaction(PaymentTransactionRequest request);
    PaymentTransactionResponse getPaymentTransactionById(String id);
    String getPaymentStatus(String id);
}
