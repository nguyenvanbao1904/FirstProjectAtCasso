package com.example.nvb.casso_app.service.impl;

import com.example.nvb.casso_app.service.CallApiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class CallApiServiceImpl implements CallApiService {
    WebClient.Builder webClientBuilder;

    @Override
    public <T> T callApiWithHeader(String url, Map<String, String> headers, Class<T> responseType) {
        WebClient.RequestHeadersSpec<?> request = webClientBuilder.build().get().uri(url);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request = request.header(entry.getKey(), entry.getValue());
        }

        return request.retrieve()
                .bodyToMono(responseType)
                .block();
    }

    @Override
    public <T> T callApi(String url,
                         HttpMethod method,
                         Map<String, String> headers,
                         Object body,
                         Class<T> responseType) {
        WebClient.RequestBodySpec request = webClientBuilder.build()
                .method(method)
                .uri(url);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request = request.header(entry.getKey(), entry.getValue());
        }

        WebClient.ResponseSpec responseSpec;
        if (body != null && (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH)) {
            responseSpec = request.bodyValue(body).retrieve();
        } else {
            responseSpec = request.retrieve();
        }

        return responseSpec
                .bodyToMono(responseType)
                .block();
    }
}
