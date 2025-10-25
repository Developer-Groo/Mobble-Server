package com.mobble.mobbleserver.application.notification.core.port.required;

import com.mobble.mobbleserver.domain.notification.core.Notification;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NotificationReadPort {

    Optional<Notification> findById(Long id);

    List<Notification> findSlice(Long memberId, Long cursorId, Pageable pageable);

    long countByReceiverIdAndIsReadFalse(Long memberId);
}
