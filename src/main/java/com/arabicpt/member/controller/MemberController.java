package com.arabicpt.member.controller;

import com.arabicpt.auth.service.JwtService;
import com.arabicpt.common.exception.BusinessException;
import com.arabicpt.common.response.ApiResponseDTO;
import com.arabicpt.common.response.ResultCode;
import com.arabicpt.member.model.dto.MemberDTO;
import com.arabicpt.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<MemberDTO>> getMe(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "액세스 토큰이 필요합니다.");
        }
        String token = authorization.substring("Bearer ".length()).trim();
        Long memberId = jwtService.extractMemberIdFromAccessToken(token);
        MemberDTO member = memberService.findById(memberId);
        if (member == null || !"Y".equals(member.getStatus())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "사용할 수 없는 회원입니다.");
        }
        return ResponseEntity.ok(ApiResponseDTO.success(member));
    }
}
