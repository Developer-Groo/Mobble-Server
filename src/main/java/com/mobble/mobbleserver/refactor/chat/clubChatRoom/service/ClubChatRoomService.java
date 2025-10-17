package com.mobble.mobbleserver.refactor.chat.clubChatRoom.service;

import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.room.ClubRoomInfo;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.request.ChatMessageRequestDto;
import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.refactor.chat.chatMessage.repository.ChatMessageRepository;
import com.mobble.mobbleserver.refactor.chat.chatMessage.service.ChatMessageService;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.ChatRoomType;
import com.mobble.mobbleserver.refactor.chat.chatRoom.repository.ChatRoomRepository;
import com.mobble.mobbleserver.domain.chat.room.ChatRoomParticipant;
import com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.repository.ChatRoomParticipantRepository;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.dto.request.ClubChatMessageRequestDto;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.dto.response.ClubChatMessageResponseDto;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.repository.ClubChatRoomRepository;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.validator.ClubChatRoomValidator;
import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubChatRoomService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ClubMemberValidator clubMemberValidator;

    @Transactional
    public void sendGroupMessage(ClubChatMessageRequestDto dto, Long memberId) {
        ChatMessage savedMessage = chatMessageService.saveChatMessageAndUpdateLastRead(dto.chatRoomId(), memberId, dto.content(), dto.type());

        ClubChatMessageResponseDto response = ClubChatMessageResponseDto.toDto(
                savedMessage.getChatRoom().getId(),
                savedMessage.getContent(),
                savedMessage.getType(),
                savedMessage.getSender().getId(),
                savedMessage.getSender().getName(),
                DateTimeUtils.toKST(savedMessage.getCreatedAt())
        );

        messagingTemplate.convertAndSend("/topic/group/chatroom/" + dto.chatRoomId(), response);
    }

    public List<ChatMessageResponseDto> getClubChatRoomMessages(Long clubId, Long memberId, ChatMessageRequestDto dto) {
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Member member = clubMember.getMember();

        return chatMessageService.getMessagesForParticipant(member.getId(), dto);
    }
}
