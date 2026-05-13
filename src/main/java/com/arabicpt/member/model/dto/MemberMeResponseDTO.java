package com.arabicpt.member.model.dto;

import lombok.Builder;
import lombok.Getter;

/** GET /api/members/me — 클라이언트에 필요한 필드만 노출 */
@Getter
@Builder
public class MemberMeResponseDTO {
    private final Long memberId;
    private final String email;
    private final String name;
    private final String profileImage;

    public static MemberMeResponseDTO from(MemberDTO member) {
        if (member == null) {
            return null;
        }
        return MemberMeResponseDTO.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .name(member.getName())
                .profileImage(member.getProfileImage())
                .build();
    }
}
