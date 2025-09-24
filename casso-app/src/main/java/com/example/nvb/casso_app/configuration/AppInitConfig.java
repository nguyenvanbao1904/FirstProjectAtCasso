package com.example.nvb.casso_app.configuration;

import com.example.nvb.casso_app.dto.request.FiServiceRequest;
import com.example.nvb.casso_app.dto.response.FiServiceResponse;
import com.example.nvb.casso_app.mapper.FiServiceMapper;
import com.example.nvb.casso_app.service.CallApiService;
import com.example.nvb.casso_app.service.FiServiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppInitConfig {
    CallApiService callApiService;
    FiServiceMapper fiServiceMapper;

    @NonFinal
    @Value("${casso-key.client-id}")
    private String CASSO_CLIENT_ID;

    @NonFinal
    @Value("${casso-key.secret-key}")
    private String CASSO_SECRET_KEY;

    @Bean
    ApplicationRunner applicationRunner(FiServiceService fiServiceService) {
        return args -> {
            if (fiServiceService.countService() == 0) {
                String url = "https://sandbox.bankhub.dev/fi-services";
                Map<String, String> headers = new HashMap<>();
                headers.put("x-client-id", CASSO_CLIENT_ID);
                headers.put("x-secret-key", CASSO_SECRET_KEY);

                FiServiceResponse response = callApiService.callApiWithHeader(
                        url, headers, FiServiceResponse.class);

                List<FiServiceRequest> requests = response.getFiServices()
                        .stream()
                        .collect(Collectors.groupingBy(FiServiceResponse.FiServiceItem::getFiFullName))
                        .values()
                        .stream()
                        .flatMap(group -> {
                            // luôn lấy phần tử đầu tiên
                            FiServiceResponse.FiServiceItem first = group.getFirst();

                            // lấy thêm các item có code kết thúc bằng "_qrpay"
                            List<FiServiceResponse.FiServiceItem> qrpayItems = group.stream()
                                    .filter(item -> item.getCode() != null && item.getCode().endsWith("_qrpay"))
                                    .toList();

                            // gộp lại
                            return Stream.concat(Stream.of(first), qrpayItems.stream());
                        })
                        .map(fiServiceMapper::toRequest)
                        .toList();

                requests.forEach(fiServiceService::saveFiService);
            }
        };
    }
}
