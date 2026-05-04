package com.arabicpt.health.service;

import com.arabicpt.health.model.dto.HealthStatusDTO;
import org.springframework.stereotype.Service;

@Service
public class HealthServiceImpl implements HealthService {

    @Override
    public HealthStatusDTO getHealthStatus() {
        // 기본 헬스체크는 "UP" 상태만 반환합니다.
        return new HealthStatusDTO("UP");
    }
}
