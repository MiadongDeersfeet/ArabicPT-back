package com.arabicpt.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.audio.upload-dir:uploads/audio}")
    private String uploadDir;

    @Value("${app.audio.public-path:/audio}")
    private String publicPath;

    @Value("${app.cors.allowed-origins:http://localhost:5173,http://127.0.0.1:5173}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String normalizedPublicPath = publicPath.startsWith("/") ? publicPath : "/" + publicPath;
        String normalizedUploadDir = uploadDir.replace("\\", "/");
        if (!normalizedUploadDir.endsWith("/")) {
            normalizedUploadDir += "/";
        }

        registry.addResourceHandler(normalizedPublicPath + "/**")
                .addResourceLocations("file:" + normalizedUploadDir);
    }
}
