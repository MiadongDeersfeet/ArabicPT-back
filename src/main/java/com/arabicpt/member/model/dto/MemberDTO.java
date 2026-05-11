package com.arabicpt.member.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberDTO {
    private Long memberId;
    private String googleSub;
    private String email;
    private String name;
    private String profileImage;
    private String role;
    private String status;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
