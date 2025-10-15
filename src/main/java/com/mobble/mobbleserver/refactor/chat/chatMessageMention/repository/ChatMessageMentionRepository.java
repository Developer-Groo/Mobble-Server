package com.mobble.mobbleserver.refactor.chat.chatMessageMention.repository;

import com.mobble.mobbleserver.domain.chat.message.ChatMessageMention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageMentionRepository extends JpaRepository<ChatMessageMention, Long> {
}
