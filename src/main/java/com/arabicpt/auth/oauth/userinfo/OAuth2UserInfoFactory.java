package com.arabicpt.auth.oauth.userinfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo create(String provider, Map<String, Object> attributes) {
        if ("google".equalsIgnoreCase(provider)) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        throw new IllegalArgumentException("지원하지 않는 OAuth2 공급자입니다: " + provider);
    }
}
