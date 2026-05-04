package com.arabicpt.health.controller;

import com.arabicpt.common.response.ApiResponseDTO;
import com.arabicpt.health.model.dto.HealthStatusDTO;
import com.arabicpt.health.service.HealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {
    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<HealthStatusDTO>> getHealth() {
        HealthStatusDTO data = healthService.getHealthStatus();
        return ResponseEntity.ok(ApiResponseDTO.success(data));
    }
}
