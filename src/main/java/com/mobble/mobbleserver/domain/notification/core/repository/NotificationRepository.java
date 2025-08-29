package com.mobble.mobbleserver.domain.notification.core.repository;

import com.mobble.mobbleserver.domain.notification.core.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
