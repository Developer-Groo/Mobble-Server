package com.mobble.mobbleserver.domain.chat.clubChatRoom.service;

import com.mobble.mobbleserver.domain.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.request.ClubChatMessageRequestDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response.ClubChatMessageResponseDto;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubChatRoomService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MemberValidator memberValidator;
    private final ClubMemberValidator clubMemberValidator;

    @Transactional
    public void sendGroupMessage(ClubChatMessageRequestDto dto, Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        ClubChatMessageResponseDto response = ClubChatMessageResponseDto.toDto(
                dto.chatRoomId(),
                dto.content(),
                dto.type(),
                member.getId(),
                member.getName(),
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend("/topic/group/chatroom/" + dto.chatRoomId(), response);
    }

    @Transactional
    public ClubChatRoomPreviewResponseDto createClubChatRoom(Long clubId, Long memberId) {
        // Todo: club 과 member 는 해당 메서드를 호출하는 createClub 에서 이미 객체를 가지고 있음(고민 필요)

        // Todo: clubId 로 Club 엔티티 조회
        // Todo: Chatroom 엔티티 생성 후 저장
        // Todo: Member 엔티티 조회(연관 관계 주입)
        // Todo: ChatRoomParticipant 엔티티 생성 후 저장
        // Todo: ClubChatRoom 엔티티 생성(연관 관계 주입)
        // Todo: ClubChatRoom 저장

        return null;
    }

    public List<ClubChatRoomPreviewResponseDto> getClubChatRooms(Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);
        List<ClubMember> clubMembers = clubMemberValidator.findAllClubMemberByMemberId(memberId);

        List<Long> chatRoomIds = clubMembers.stream()
                .map(cm -> cm.getClub().getClubChatRoom().getChatRoom().getId())
                .toList();

        // Todo: 마지막 메세지 조회
        // Todo: 마지막 메세지 시간 조회
        // Todo: 읽지 않은 메세지 갯수 조회

        return null;
    }

    public List<ChatMessageResponseDto> getClubChatRoomMessages(Long clubId, Long lastMessageId) {
        return null;
    }

    @Transactional
    public void updateLastReadMessage(Long clubId, Long memberId, Long lastMessageId) {

    }
}
