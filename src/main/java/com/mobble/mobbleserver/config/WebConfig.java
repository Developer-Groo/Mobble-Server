package com.mobble.mobbleserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080") // 클라이언트 origin
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)                  // ✅ 쿠키 포함
                .allowedHeaders("*")
                .exposedHeaders("Set-Cookie");            // ✅ 쿠키 응답 헤더 노출
    }
}
