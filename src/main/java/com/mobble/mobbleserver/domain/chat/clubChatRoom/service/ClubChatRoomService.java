package com.mobble.mobbleserver.domain.chat.clubChatRoom.service;

import com.mobble.mobbleserver.domain.chat.chatMessage.dto.request.ChatMessageRequestDto;
import com.mobble.mobbleserver.domain.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.domain.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.domain.chat.chatMessage.repository.ChatMessageRepository;
import com.mobble.mobbleserver.domain.chat.chatMessage.service.ChatMessageService;
import com.mobble.mobbleserver.domain.chat.chatRoom.entity.ChatRoom;
import com.mobble.mobbleserver.domain.chat.chatRoom.entity.ChatRoomType;
import com.mobble.mobbleserver.domain.chat.chatRoom.repository.ChatRoomRepository;
import com.mobble.mobbleserver.domain.chat.chatRoomParticipant.entity.ChatRoomParticipant;
import com.mobble.mobbleserver.domain.chat.chatRoomParticipant.repository.ChatRoomParticipantRepository;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.request.ClubChatMessageRequestDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response.ClubChatMessageResponseDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.entity.ClubChatRoom;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.repository.ClubChatRoomRepository;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.validator.ClubChatRoomValidator;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubChatRoomService {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService chatMessageService;

    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ClubChatRoomRepository clubChatRoomRepository;

    private final MemberValidator memberValidator;
    private final ClubMemberValidator clubMemberValidator;
    private final ClubChatRoomValidator clubChatRoomValidator;

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

    @Transactional
    public ClubChatRoomPreviewResponseDto createClubChatRoom(Long clubId, Long memberId) {
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Club club = clubMember.getClub();
        Member member = clubMember.getMember();

        clubChatRoomValidator.existsClubChatRoomByClubIdOrThrow(club.getId());

        ChatRoom chatRoom = ChatRoom.createChatRoom(ChatRoomType.GROUP);
        chatRoomRepository.save(chatRoom);

        ChatRoomParticipant participant = ChatRoomParticipant.createChatRoomParticipant(chatRoom, member);
        chatRoomParticipantRepository.save(participant);

        ClubChatRoom clubChatRoom = ClubChatRoom.createClubChatRoom(club, chatRoom);
        clubChatRoomRepository.save(clubChatRoom);

        clubChatRoom.attachTo(club);

        return ClubChatRoomPreviewResponseDto.toDto(chatRoom, club, null, 0, null);
    }

    @Transactional
    public void joinClubChatRoom(ClubMember clubMember) {
        Member member = clubMember.getMember();
        Club club = clubMember.getClub();
        ClubChatRoom clubChatRoom = club.getClubChatRoom();
        ChatRoom chatRoom = clubChatRoom.getChatRoom();

        if (!chatRoomParticipantRepository.existsByChatRoomIdAndMemberId(chatRoom.getId(), member.getId())) {
            ChatRoomParticipant participant = ChatRoomParticipant.createChatRoomParticipant(chatRoom, member);
            chatRoomParticipantRepository.save(participant);
        }
    }

    public List<ClubChatRoomPreviewResponseDto> getClubChatRooms(Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        List<ClubMember> clubMembers = clubMemberValidator.findAllClubMemberByMemberId(member.getId());
        List<Long> chatRoomIds = extractChatRoomIds(clubMembers);

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

    public List<ChatMessageResponseDto> getClubChatRoomMessages(Long clubId, Long memberId, ChatMessageRequestDto dto) {
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Member member = clubMember.getMember();

        return chatMessageService.getMessagesForParticipant(member.getId(), dto);
    }

    @Transactional
    public void leaveClubChatRoom(ClubMember clubMember) {
        Member member = clubMember.getMember();
        Club club = clubMember.getClub();
        ChatRoom chatRoom = club.getClubChatRoom().getChatRoom();

        chatRoomParticipantRepository.deleteByChatRoomIdAndMemberId(chatRoom.getId(), member.getId());
    }

    @Transactional
    public void deleteClubChatRoom(Long clubId) {
        ClubChatRoom clubChatRoom = clubChatRoomValidator.findClubChatRoomByClubIdOrThrow(clubId);
        ChatRoom chatRoom = clubChatRoom.getChatRoom();

        clubChatRoom.detach();

        chatRoomParticipantRepository.deleteByChatRoomId(chatRoom.getId());
        chatMessageRepository.deleteByChatRoomId(chatRoom.getId());
        clubChatRoomRepository.deleteById(clubChatRoom.getId());
        chatRoomRepository.deleteById(chatRoom.getId());
    }

    private List<Long> extractChatRoomIds(List<ClubMember> clubMembers) {
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
