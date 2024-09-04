package com.soocompany.wodify.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        // 상태 확인: 항상 200 OK를 반환하도록 함.
        return new ResponseEntity<>("Application is running", HttpStatus.OK);
    }
}
