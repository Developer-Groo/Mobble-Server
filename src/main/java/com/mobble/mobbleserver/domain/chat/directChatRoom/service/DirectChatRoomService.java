package com.mobble.mobbleserver.domain.chat.directChatRoom.service;

import com.mobble.mobbleserver.domain.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.domain.chat.chatMessage.service.ChatMessageService;
import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.request.DirectChatMessageRequestDto;
import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.request.DirectChatRoomCreateRequestDto;
import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.response.DirectChatMessageResponseDto;
import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.response.DirectChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DirectChatRoomService {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService chatMessageService;

    private final MemberValidator memberValidator;

    @Transactional
    public void sendDirectMessage(DirectChatMessageRequestDto dto, Long memberId) {
        Member targetMember = memberValidator.findMemberByMemberIdOrThrow(dto.receiverId());
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

    @Transactional
    public DirectChatRoomPreviewResponseDto createDirectChatRoom(DirectChatRoomCreateRequestDto dto, Long memberId) {
        return null;
    }

    public void getDirectChatRooms(Long memberId) {

    }

    public void getDirectChatRoomMessages(Long memberId, Long chatRoomId) {

    }

    @Transactional
    public void leaveDirectChatRoom(Long memberId, Long chatRoomId) {

    }
    
    private void deleteDirectChatRoom(Long chatRoomId) {

    }
}
