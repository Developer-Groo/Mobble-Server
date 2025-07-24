package com.mobble.mobbleserver.domain.chat.clubChatRoom.service;

import com.mobble.mobbleserver.domain.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.domain.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.domain.chat.chatMessage.repository.ChatMessageRepository;
import com.mobble.mobbleserver.domain.chat.chatRoom.entity.ChatRoom;
import com.mobble.mobbleserver.domain.chat.chatRoomParticipant.repository.ChatRoomParticipantRepository;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.request.ClubChatMessageRequestDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response.ClubChatMessageResponseDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.domain.club.entity.Club;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubChatRoomService {

    private final SimpMessagingTemplate messagingTemplate;

    private final MemberValidator memberValidator;
    private final ClubMemberValidator clubMemberValidator;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;

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

        List<ClubMember> clubMembers = clubMemberValidator.findAllClubMemberByMemberId(member.getId());
        List<Long> chatRoomIds = clubMembers.stream()
                .map(cm -> cm.getClub().getClubChatRoom().getChatRoom().getId())
                .toList();

        Map<Long, Long> lastReadMessageIdsByChatRoom = chatRoomParticipantRepository.findAllByChatRoomIdsAndMemberId(chatRoomIds, member.getId())
                .stream()
                .collect(Collectors.toMap(
                        p -> p.getChatRoom().getId(),
                        p -> Optional.ofNullable(p.getLastReadMessage())
                                .map(ChatMessage::getId)
                                .orElse(0L)
                ));

        Map<Long, ChatMessage> latestMessagesMap = chatMessageRepository.findLatestMessagesByChatRoomIds(chatRoomIds);
        Map<Long, Integer> unreadCountMap = chatMessageRepository.countUnreadMessagesByChatRoomIds(chatRoomIds, lastReadMessageIdsByChatRoom);

        return clubMembers.stream()
                .map(clubMember -> {
                    Club club = clubMember.getClub();
                    ChatRoom chatRoom = club.getClubChatRoom().getChatRoom();
                    Long chatRoomId = chatRoom.getId();

                    ChatMessage lastMessage = latestMessagesMap.get(chatRoomId);
                    int unreadCount = unreadCountMap.getOrDefault(chatRoomId, 0);
                    Long lastReadMessageId = lastReadMessageIdsByChatRoom.getOrDefault(chatRoomId, 0L);

                    return new ClubChatRoomPreviewResponseDto(
                            chatRoomId,
                            club.getId(),
                            club.getName(),
                            lastMessage != null ? lastMessage.getContent() : "",
                            lastMessage != null ? lastMessage.getCreatedAt() : null,
                            unreadCount,
                            lastReadMessageId
                    );
                })
                .toList();
    }

    public List<ChatMessageResponseDto> getClubChatRoomMessages(Long clubId, Long lastMessageId) {
        return null;
    }

    @Transactional
    public void updateLastReadMessage(Long clubId, Long memberId, Long lastMessageId) {

    }
}
