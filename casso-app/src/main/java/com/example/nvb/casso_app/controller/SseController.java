package com.example.nvb.casso_app.controller;

import com.example.nvb.casso_app.service.SseEmitterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SseController {
    SseEmitterService sseEmitterService;
    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        return sseEmitterService.subscribe("default");
    }
}
