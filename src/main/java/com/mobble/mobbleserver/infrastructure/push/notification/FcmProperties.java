package com.mobble.mobbleserver.infrastructure.push.notification;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "fcm")
public class FcmProperties {
    private final boolean enabled;
    private final String projectId;
    private final String serviceAccount;
    private final boolean dryRun;

    public FcmProperties(boolean enabled, String projectId, String serviceAccount, boolean dryRun) {
        this.enabled = enabled;
        this.projectId = projectId;
        this.serviceAccount = serviceAccount;
        this.dryRun = dryRun;
    }
}
