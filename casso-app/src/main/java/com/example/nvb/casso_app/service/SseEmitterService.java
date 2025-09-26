package com.example.nvb.casso_app.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterService {
    SseEmitter subscribe(String clientId);
    void broadcast(String eventName, Object data);
}
