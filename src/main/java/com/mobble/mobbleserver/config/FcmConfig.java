package com.mobble.mobbleserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.mobble.mobbleserver.application.notification.outbox.push.FcmClient;
import com.mobble.mobbleserver.application.notification.outbox.push.FcmProperties;
import com.mobble.mobbleserver.application.notification.outbox.push.PushClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableAsync
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@EnableConfigurationProperties(FcmProperties.class)
public class FcmConfig {

    private final FcmProperties properties;
    private final ResourceLoader loader;

    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        Resource resource = loader.getResource(properties.getServiceAccount());
        try (InputStream inputStream = resource.getInputStream()) {
            return GoogleCredentials.fromStream(inputStream)
                    .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));
        }
    }

    @Bean(name = "pushExecutor")
    public Executor pushExecutor() {
        return Executors.newFixedThreadPool(2);
    }

    @Bean
    public PushClient pushClient(GoogleCredentials credentials, ObjectMapper om) {
        if (!properties.isEnabled()) return new PushClient.Noop();
        return new FcmClient(credentials, om, properties.getProjectId(), properties.isDryRun());
    }
}

