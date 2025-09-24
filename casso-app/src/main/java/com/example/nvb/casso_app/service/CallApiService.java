package com.example.nvb.casso_app.service;

import org.springframework.http.HttpMethod;

import java.util.Map;

public interface CallApiService {
    <T> T callApiWithHeader(String url, Map<String, String> headers, Class<T> responseType);
    <T> T callApi(String url,
                         HttpMethod method,
                         Map<String, String> headers,
                         Object body,
                         Class<T> responseType);
}
