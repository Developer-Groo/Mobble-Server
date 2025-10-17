package com.mobble.mobbleserver.refactor.chat.directChatRoom.service;

import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.request.ChatMessageRequestDto;
import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.refactor.chat.chatMessage.service.ChatMessageService;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.request.DirectChatMessageRequestDto;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.response.DirectChatMessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DirectChatRoomService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final MemberReadPort memberReadPort;

    @Transactional
    public void sendDirectMessage(DirectChatMessageRequestDto dto, Long memberId) {
        Member targetMember = findMemberByMemberIdOrThrow(dto.receiverId());
        ChatMessage savedMessage = chatMessageService.saveChatMessageAndUpdateLastRead(dto.chatRoomId(), memberId, dto.content(), dto.type());

        DirectChatMessageResponseDto response = DirectChatMessageResponseDto.toDto(
                savedMessage.getChatRoom().getId(),
                savedMessage.getContent(),
                savedMessage.getType(),
                savedMessage.getSender().getId(),
                savedMessage.getSender().getName(),
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSendToUser(targetMember.getEmail(), "/queue/direct/chatroom/" + dto.chatRoomId(), response);
    }

    public List<ChatMessageResponseDto> getDirectChatRoomMessages(Long memberId, ChatMessageRequestDto dto) {
        Member member = findMemberByMemberIdOrThrow(memberId);

        return chatMessageService.getMessagesForParticipant(member.getId(), dto);
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
