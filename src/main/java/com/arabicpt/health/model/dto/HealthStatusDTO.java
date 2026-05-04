package com.arabicpt.health.model.dto;

public class HealthStatusDTO {
    private final String status;

    public HealthStatusDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
