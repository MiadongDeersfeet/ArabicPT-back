package com.arabicpt.auth.security.handler;

import com.arabicpt.auth.model.dto.AuthTokenResponseDTO;
import com.arabicpt.auth.model.dto.OAuth2UserProfileDTO;
import com.arabicpt.auth.oauth.userinfo.OAuth2UserInfo;
import com.arabicpt.auth.oauth.userinfo.OAuth2UserInfoFactory;
import com.arabicpt.auth.security.cookie.AuthCookieService;
import com.arabicpt.auth.service.OAuth2Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2Service oAuth2Service;
    private final AuthCookieService authCookieService;

    @Value("${app.auth.oauth2.success-redirect-uri}")
    private String successRedirectUri;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.create("google", oAuth2User.getAttributes());

        AuthTokenResponseDTO tokenResponse = oAuth2Service.loginWithGoogleProfile(
                OAuth2UserProfileDTO.builder()
                        .googleSub(userInfo.getProviderId())
                        .email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .profileImage(userInfo.getProfileImage())
                        .build()
        );

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                authCookieService.createRefreshCookie(
                        tokenResponse.getRefreshToken(),
                        tokenResponse.getRefreshTokenExpiresIn()
                ).toString()
        );

        String redirectUrl = UriComponentsBuilder.fromUriString(successRedirectUri)
                .queryParam("accessToken", tokenResponse.getAccessToken())
                .queryParam("memberId", tokenResponse.getMemberId())
                .queryParam("email", tokenResponse.getEmail())
                .queryParam("name", tokenResponse.getName())
                .queryParam("role", tokenResponse.getRole())
                .encode()
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
