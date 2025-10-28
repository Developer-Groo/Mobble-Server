package com.mobble.mobbleserver.infrastructure.persistence.chat.message;

import com.mobble.mobbleserver.domain.chat.message.ChatMessageMention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatMessageMentionRepository extends JpaRepository<ChatMessageMention, Long> {
}
