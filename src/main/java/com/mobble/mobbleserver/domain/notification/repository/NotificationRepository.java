package com.mobble.mobbleserver.domain.notification.repository;

import com.mobble.mobbleserver.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
