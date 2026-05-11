package com.arabicpt.auth.controller;

import com.arabicpt.auth.model.dto.AuthTokenResponseDTO;
import com.arabicpt.auth.security.cookie.AuthCookieService;
import com.arabicpt.auth.service.OAuth2Service;
import com.arabicpt.auth.service.RefreshTokenService;
import com.arabicpt.common.response.ApiResponseDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthCookieService authCookieService;
    private final OAuth2Service oAuth2Service;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/google/login-url")
    public ResponseEntity<ApiResponseDTO<String>> getGoogleLoginUrl() {
        return ResponseEntity.ok(ApiResponseDTO.success("/oauth2/authorization/google"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDTO<AuthTokenResponseDTO>> refresh(HttpServletRequest request) {
        String refreshToken = extractRefreshToken(request);
        AuthTokenResponseDTO tokenResponse = oAuth2Service.reissueByRefreshToken(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        authCookieService.createRefreshCookie(
                                tokenResponse.getRefreshToken(),
                                tokenResponse.getRefreshTokenExpiresIn()
                        ).toString())
                .body(ApiResponseDTO.success(tokenResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<String>> logout(HttpServletRequest request) {
        String refreshToken = extractRefreshToken(request);
        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenService.revokeByToken(refreshToken);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCookieService.expireRefreshCookie().toString())
                .body(ApiResponseDTO.success("logged out"));
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        String cookieName = authCookieService.getRefreshCookieName();
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
