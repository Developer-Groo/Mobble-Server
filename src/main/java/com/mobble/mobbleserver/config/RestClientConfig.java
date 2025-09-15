package com.mobble.mobbleserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient kakaoRestClient() {
        return RestClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public RestClient naverRestClient() {
        return RestClient.builder()
                .baseUrl("https://openapi.naver.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public RestClient googleRestClient() {
        return RestClient.builder()
                .baseUrl("https://www.googleapis.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public RestClient appleRestClient() {
        return RestClient.builder()
                .baseUrl("https://appleid.apple.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
