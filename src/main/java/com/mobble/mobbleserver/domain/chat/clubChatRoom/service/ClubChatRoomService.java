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
    public ClubChatRoomPreviewResponseDto createClubChatRoom(Club club, Member member) {
        ChatRoom chatRoom = ChatRoom.createChatRoom(ChatRoomType.GROUP);
        ChatRoomParticipant participant = ChatRoomParticipant.createChatRoomParticipant(chatRoom, member);
        ClubChatRoom clubChatRoom = ClubChatRoom.createClubChatRoom(club, chatRoom);

        chatRoomRepository.save(chatRoom);
        chatRoomParticipantRepository.save(participant);
        clubChatRoomRepository.save(clubChatRoom);

        return ClubChatRoomPreviewResponseDto.toDto(chatRoom, club, null, 0, null);
    }

    public List<ClubChatRoomPreviewResponseDto> getClubChatRooms(Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        List<ClubMember> clubMembers = clubMemberValidator.findAllClubMemberByMemberId(member.getId());
        List<Long> chatRoomIds = extractChatRomIds(clubMembers);

        Map<Long, Long> lastReadMessageIdsByChatRoom = getLastReadMessageIdsByChatRoom(chatRoomIds, member.getId());
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

                    return ClubChatRoomPreviewResponseDto.toDto(chatRoom, club, lastMessage, unreadCount, lastReadMessageId);
                })
                .toList();
    }

    public List<ChatMessageResponseDto> getClubChatRoomMessages(Long clubId, Long lastMessageId) {
        return null;
    }

    private List<Long> extractChatRomIds(List<ClubMember> clubMembers) {
        return clubMembers.stream()
                .map(cm -> cm.getClub().getClubChatRoom().getChatRoom().getId())
                .toList();
    }

    private Map<Long, Long> getLastReadMessageIdsByChatRoom(List<Long> chatRoomIds, Long memberId) {
        return chatRoomParticipantRepository.findAllByChatRoomIdsAndMemberId(chatRoomIds, memberId)
                .stream()
                .collect(Collectors.toMap(
                        participant -> participant.getChatRoom().getId(),
                        participant -> Optional.ofNullable(participant.getLastReadMessage())
                                .map(ChatMessage::getId)
                                .orElse(0L)
                ));
    }
}
