package com.arabicpt.auth.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RefreshTokenRecordDTO {
    private Long tokenId;
    private Long memberId;
    private String refreshToken;
    private LocalDateTime expiresAt;
    private String isRevoked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
