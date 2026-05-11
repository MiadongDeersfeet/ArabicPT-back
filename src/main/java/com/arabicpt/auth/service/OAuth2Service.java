package com.arabicpt.auth.service;

import com.arabicpt.auth.model.dto.AuthTokenResponseDTO;
import com.arabicpt.auth.model.dto.OAuth2UserProfileDTO;
import com.arabicpt.auth.model.dto.RefreshTokenRecordDTO;
import com.arabicpt.common.exception.BusinessException;
import com.arabicpt.common.response.ResultCode;
import com.arabicpt.member.model.dto.MemberDTO;
import com.arabicpt.member.model.dto.MemberUpsertDTO;
import com.arabicpt.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuth2Service {
    private final MemberService memberService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthTokenResponseDTO loginWithGoogleProfile(OAuth2UserProfileDTO profile) {
        if (profile.getGoogleSub() == null || profile.getGoogleSub().isBlank()) {
            throw new BusinessException(ResultCode.INVALID_REQUEST, "구글 사용자 식별자(sub)가 없습니다.");
        }
        if (profile.getEmail() == null || profile.getEmail().isBlank()) {
            throw new BusinessException(ResultCode.INVALID_REQUEST, "구글 이메일 정보가 없습니다.");
        }

        MemberDTO member = upsertMember(profile);
        memberService.touchLastLogin(member.getMemberId());
        return issueTokens(member);
    }

    @Transactional
    public AuthTokenResponseDTO reissueByRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "리프레시 토큰이 없습니다.");
        }

        RefreshTokenRecordDTO record = refreshTokenService.findActiveToken(refreshToken);
        if (record == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "만료되었거나 폐기된 리프레시 토큰입니다.");
        }

        Long memberIdFromToken = jwtService.extractMemberIdFromRefreshToken(refreshToken);
        if (!record.getMemberId().equals(memberIdFromToken)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "토큰 사용자 정보가 일치하지 않습니다.");
        }

        MemberDTO member = memberService.findById(memberIdFromToken);
        if (member == null || !"Y".equals(member.getStatus())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "사용할 수 없는 회원입니다.");
        }

        return issueTokens(member);
    }

    private MemberDTO upsertMember(OAuth2UserProfileDTO profile) {
        MemberDTO existing = memberService.findByGoogleSub(profile.getGoogleSub());
        if (existing == null) {
            existing = memberService.findByEmail(profile.getEmail());
        }

        MemberUpsertDTO upsertDTO = MemberUpsertDTO.builder()
                .memberId(existing != null ? existing.getMemberId() : null)
                .googleSub(profile.getGoogleSub())
                .email(profile.getEmail())
                .name(profile.getName())
                .profileImage(profile.getProfileImage())
                .role(existing != null ? existing.getRole() : "USER")
                .status("Y")
                .build();

        memberService.upsert(upsertDTO);
        return memberService.findByGoogleSub(profile.getGoogleSub());
    }

    private AuthTokenResponseDTO issueTokens(MemberDTO member) {
        String accessToken = jwtService.createAccessToken(member.getMemberId(), member.getEmail(), member.getRole());
        String refreshToken = jwtService.createRefreshToken(member.getMemberId());
        refreshTokenService.rotateRefreshToken(
                member.getMemberId(),
                refreshToken,
                jwtService.getRefreshTokenExpirationSeconds()
        );

        return AuthTokenResponseDTO.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(jwtService.getAccessTokenExpirationSeconds())
                .refreshTokenExpiresIn(jwtService.getRefreshTokenExpirationSeconds())
                .build();
    }
}
