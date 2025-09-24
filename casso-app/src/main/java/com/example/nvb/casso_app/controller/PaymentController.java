package com.example.nvb.casso_app.controller;

import com.example.nvb.casso_app.dto.request.PaymentRequest;
import com.example.nvb.casso_app.dto.response.ApiResponse;
import com.example.nvb.casso_app.dto.response.PaymentResponse;
import com.example.nvb.casso_app.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentController {
    PaymentService paymentService;
    @PostMapping
    public ApiResponse<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest) {
        return ApiResponse.<PaymentResponse>builder()
                .code(201)
                .message("Payment created successfully")
                .data(paymentService.createQrcode(paymentRequest))
                .build();
    }

    @GetMapping("/check/{reference}")
    public ApiResponse<String> checkPaymentStatus(@PathVariable String reference) {
        return ApiResponse.<String>builder()
                .code(200)
                .message("Payment status check successfully")
                .data(paymentService.getPaymentStatus(reference))
                .build();
    }
}
