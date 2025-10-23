package com.mobble.mobbleserver.application.chat.message.service;

import com.mobble.mobbleserver.application.chat.message.port.provided.MessageQueryPort;
import com.mobble.mobbleserver.application.chat.message.port.required.MessageReadPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.web.chat.message.dto.response.ChatMessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageQueryService implements MessageQueryPort {

    private final MemberReadPort memberReadPort;
    private final ChatRoomReadPort chatRoomReadPort;
    private final MessageReadPort messageReadPort;

    @Override
    public List<ChatMessageResponseDto> list(
            Long chatRoomId,
            Long memberId,
            Long cursor,
            LocalDateTime lastCreatedAt,
            String direction,
            int limit
    ) {
        Member member = memberReadPort.findByIdAndIsDeletedFalse(memberId).orElseThrow();
        ChatRoom chatRoom = chatRoomReadPort.findChatRoomById(chatRoomId).orElseThrow();

        LocalDateTime joinedAt = chatRoom.joinedAtOf(member);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = joinedAt.isAfter(now.minusDays(30)) ? joinedAt : now.minusDays(30);

        // Todo: 커서 페이지네이션 고도화: 현재 prev 전용, limit=50 고정, lastCreatedAt=null)에서
        //  → (createdAt,id) 기반 커서로 lastCreatedAt 포함 + direction(prev/next) + limit 적용하도록 MessageReadPort/쿼리 메서드 확장
        List<ChatMessage> messages = messageReadPort.findMessagesFrom(chatRoom.getId(), startDate, cursor, lastCreatedAt);

        return messages.stream()
                .map(ChatMessageResponseDto::toDto)
                .toList();
    }
}

