package com.mobble.mobbleserver.application.chat.message.service;

import com.mobble.mobbleserver.application.chat.message.error.ChatMessageBusinessError;
import com.mobble.mobbleserver.application.chat.message.port.provided.MessageQueryPort;
import com.mobble.mobbleserver.application.chat.message.port.required.MessageReadPort;
import com.mobble.mobbleserver.application.chat.message.result.ChatMessageSliceResult;
import com.mobble.mobbleserver.application.chat.room.error.ChatRoomBusinessError;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.member.error.MemberBusinessError;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.application.chat.message.result.ChatMessageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageQueryService implements MessageQueryPort {

    private static final int LIMIT = 100;

    private final MemberReadPort memberReadPort;
    private final ChatRoomReadPort chatRoomReadPort;
    private final MessageReadPort messageReadPort;

    /**
     * <커서 기반(과거 전용) 메시지 조회>
     * - cursorId + cursorCreatedAt 둘 다 null 이면: 최신 100개
     * - 둘 다 존재하면: (createdAt, id) < (cursorCreatedAt, cursorId) 인 과거 100개
     * - 응답은 항상 createdAt desc, id desc 순서로 내려감.
     */
    @Override
    public ChatMessageSliceResult getMessages(
            Long chatRoomId,
            Long memberId,
            Long cursorId,
            LocalDateTime cursorCreatedAt
    ) {
        Member member = assertMemberByMemberId(memberId);
        ChatRoom chatRoom = assertChatRoomByChatRoomId(chatRoomId);
        assertHasParticipant(chatRoom, member);

        LocalDateTime startDate = chatRoom.joinedAtOf(member);

        if ((cursorId == null) != (cursorCreatedAt == null)) {
            throw new BusinessException(ChatMessageBusinessError.INVALID_CURSOR);
        }

        boolean hasCursor = cursorId != null;
        Long effectiveCursorId = hasCursor ? cursorId : null;
        LocalDateTime effectiveCursorCreatedAt = hasCursor ? cursorCreatedAt : null;

        List<ChatMessage> messages = messageReadPort.findMessages(
                chatRoom.getId(),
                startDate,
                effectiveCursorId,
                effectiveCursorCreatedAt,
                LIMIT + 1
        );

        if (messages.isEmpty()) return ChatMessageSliceResult.empty();

        boolean hasMore = messages.size() > LIMIT;
        List<ChatMessage> page = hasMore ? messages.subList(0, LIMIT) : messages;

        List<ChatMessageResult> results = page.stream()
                .map(ChatMessageResult::create)
                .toList();

        ChatMessage last = page.get(page.size() - 1);

        return ChatMessageSliceResult.create(
                hasMore,
                last.getId(),
                last.getCreatedAt(),
                results
        );
    }

    /* ==== Private Helper ==== */
    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new BusinessException(MemberBusinessError.NOT_FOUND));
    }

    private ChatRoom assertChatRoomByChatRoomId(Long chatRoomId) {
        return chatRoomReadPort.findChatRoomById(chatRoomId)
                .orElseThrow(() -> new BusinessException(ChatRoomBusinessError.NOT_FOUND));
    }

    private void assertHasParticipant(ChatRoom chatRoom, Member member) {
        if (!chatRoom.hasParticipant(member)) throw new BusinessException(ChatRoomBusinessError.NOT_PARTICIPANT);
    }
}

