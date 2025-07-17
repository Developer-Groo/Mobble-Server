package com.mobble.mobbleserver.domain.chat.clubChatRoom.service;

import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.request.ClubChatRoomRequestDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response.ClubChatRoomResponseDto;
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
public class ClubChatRoomService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MemberValidator memberValidator;

    @Transactional(readOnly = true)
    public void sendGroupMessage(ClubChatRoomRequestDto dto, Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        ClubChatRoomResponseDto response = ClubChatRoomResponseDto.toDto(
                dto.chatRoomId(),
                dto.content(),
                dto.type(),
                member.getId(),
                member.getName(),
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend("/topic/group/chatroom/" + dto.chatRoomId(), response);
    }
}
