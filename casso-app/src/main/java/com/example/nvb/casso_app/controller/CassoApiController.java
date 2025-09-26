package com.example.nvb.casso_app.controller;

import com.example.nvb.casso_app.dto.request.CassoWebhookRequest;
import com.example.nvb.casso_app.dto.response.ApiResponse;
import com.example.nvb.casso_app.dto.response.GrantTokenResponse;
import com.example.nvb.casso_app.dto.response.PaymentTransactionResponse;
import com.example.nvb.casso_app.enums.PaymentTransactionStatus;
import com.example.nvb.casso_app.mapper.PaymentTransactionMapper;
import com.example.nvb.casso_app.service.FiServiceService;
import com.example.nvb.casso_app.service.PaymentService;
import com.example.nvb.casso_app.service.SseEmitterService;
import com.example.nvb.casso_app.service.TokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/casso-api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CassoApiController {
    PaymentService paymentService;
    PaymentTransactionMapper paymentTransactionMapper;
    TokenService tokenService;
    SseEmitterService sseEmitterService;
    FiServiceService fiServiceService;

    @PostMapping("/webhook-qrpay")
    public ApiResponse<Void> handleWebhookQRPay(@RequestBody CassoWebhookRequest payload) {
        log.info("Received Webhook QR Pay request: {}", payload);
        if ("TRANSACTIONS".equalsIgnoreCase(payload.getWebhookType())
                && payload.getTransaction() != null) {

            CassoWebhookRequest.Transaction tx = payload.getTransaction();
            String bankDescription = tx.getDescription();
            String ref = extractReference(bankDescription);
            log.info("Received Webhook QR Pay transaction: {}", ref);
            PaymentTransactionResponse pt = paymentService.getPaymentTransactionById(ref);
            if(pt != null) {
                pt.setStatus(PaymentTransactionStatus.SUCCESS);
                paymentService.savePaymentTransaction(paymentTransactionMapper.toRequest(pt));
            }
        }
        return ApiResponse.<Void>builder()
                .code(0)
                .message("Success")
                .build();
    }

    @PostMapping("/webhook-grant")
    public ApiResponse<Void> handleWebhookRevokedGrant(@RequestBody CassoWebhookRequest payload) {
        log.info("Received Webhook for grant request: {}", payload);
        if ("GRANT".equalsIgnoreCase(payload.getWebhookType())
                && "USER_PERMISSION_REVOKED".equalsIgnoreCase(payload.getWebhookCode())) {

            GrantTokenResponse webhookGrantId = tokenService.getGrantId(payload.getGrantId());
            if(webhookGrantId != null) {
                tokenService.deleteAccessToken(webhookGrantId.getFiService().getId(), false);
                sseEmitterService.broadcast("GRANT_REVOKED",
                        fiServiceService.getFiService(webhookGrantId.getFiService().getId()).getFiFullName());
            }
        }
        return ApiResponse.<Void>builder()
                .code(0)
                .message("Success")
                .build();
    }

    private String extractReference(String description) {
        Pattern p = Pattern.compile("\\.(\\w{9})\\.");
        Matcher m = p.matcher(description);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

}
