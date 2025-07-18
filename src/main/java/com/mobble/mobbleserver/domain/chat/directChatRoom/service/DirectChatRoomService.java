package com.mobble.mobbleserver.domain.chat.directChatRoom.service;

import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.request.DirectChatRoomRequestDto;
import com.mobble.mobbleserver.domain.chat.directChatRoom.dto.response.DirectChatMessageResponseDto;
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
    private final MemberValidator memberValidator;

    @Transactional
    public void sendDirectMessage(DirectChatRoomRequestDto dto, Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);
        Member targetMember = memberValidator.findMemberByMemberIdOrThrow(dto.receiverId());

        DirectChatMessageResponseDto response = DirectChatMessageResponseDto.toDto(
                dto.chatRoomId(),
                dto.content(),
                dto.type(),
                member.getId(),
                member.getName(),
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSendToUser(targetMember.getEmail(), "/queue/direct/chatroom/" + dto.chatRoomId(), response);
    }
}
