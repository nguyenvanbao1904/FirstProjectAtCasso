package com.example.nvb.casso_app.service.impl;

import com.example.nvb.casso_app.service.SseEmitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseEmitterServiceImpl implements SseEmitterService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscribe(String clientId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> { //Khi client đóng kết nối
            log.info("SSE completed for clientId={}", clientId);
            emitters.remove(clientId);
        });
        emitter.onTimeout(() -> { //Khi connection hết hạn
            log.warn("SSE timeout for clientId={}", clientId);
            emitters.remove(clientId);
        });
        emitter.onError(e -> { //Khi gặp lỗi (ví dụ network drop)
            log.error("SSE error for clientId={}", clientId, e);
            emitters.remove(clientId);
        });

        emitters.put(clientId, emitter);
        log.info("Client {} subscribed to SSE", clientId);
        return emitter;
    }
    @Override
    public void broadcast(String eventName, Object data) {
        emitters.forEach((clientId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
            } catch (IOException e) {
                log.error("Error broadcasting SSE to clientId={}", clientId, e);
                emitters.remove(clientId);
            }
        });
    }
}
