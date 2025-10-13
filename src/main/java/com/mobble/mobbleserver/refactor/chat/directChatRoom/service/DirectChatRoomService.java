package com.mobble.mobbleserver.refactor.chat.directChatRoom.service;

import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.request.ChatMessageRequestDto;
import com.mobble.mobbleserver.refactor.chat.chatMessage.dto.response.ChatMessageResponseDto;
import com.mobble.mobbleserver.refactor.chat.chatMessage.entity.ChatMessage;
import com.mobble.mobbleserver.refactor.chat.chatMessage.repository.ChatMessageRepository;
import com.mobble.mobbleserver.refactor.chat.chatMessage.service.ChatMessageService;
import com.mobble.mobbleserver.refactor.chat.chatRoom.entity.ChatRoom;
import com.mobble.mobbleserver.refactor.chat.chatRoom.entity.ChatRoomType;
import com.mobble.mobbleserver.refactor.chat.chatRoom.repository.ChatRoomRepository;
import com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.entity.ChatRoomParticipant;
import com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.repository.ChatRoomParticipantRepository;
import com.mobble.mobbleserver.refactor.chat.chatRoomParticipant.validator.ChatRoomParticipantValidator;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.request.DirectChatMessageRequestDto;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.request.DirectChatRoomCreateRequestDto;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.response.DirectChatMessageResponseDto;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.dto.response.DirectChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.entity.DirectChatRoom;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.repository.DirectChatRoomRepository;
import com.mobble.mobbleserver.refactor.chat.directChatRoom.validator.DirectChatRoomValidator;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.member.validator.MemberValidator;
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
public class DirectChatRoomService {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService chatMessageService;

    private final MemberValidator memberValidator;
    private final ChatRoomParticipantValidator chatRoomParticipantValidator;
    private final DirectChatRoomValidator directChatRoomValidator;

    private final DirectChatRoomRepository directChatRoomRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

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
        directChatRoomValidator.existsDirectChatRoomByBetweenMembersOrThrow(memberId, dto.receiverId());

        Member sender = memberValidator.findMemberByMemberIdOrThrow(memberId);
        Member receiver = memberValidator.findMemberByMemberIdOrThrow(dto.receiverId());

        ChatRoom chatRoom = ChatRoom.createChatRoom(ChatRoomType.DIRECT);
        ChatRoomParticipant senderParticipant = ChatRoomParticipant.createChatRoomParticipant(chatRoom, sender);
        ChatRoomParticipant receiverParticipant = ChatRoomParticipant.createChatRoomParticipant(chatRoom, receiver);
        DirectChatRoom directChatRoom = DirectChatRoom.createDirectChatRoom(chatRoom, sender, receiver);

        chatRoomRepository.save(chatRoom);
        chatRoomParticipantRepository.save(senderParticipant);
        chatRoomParticipantRepository.save(receiverParticipant);
        directChatRoomRepository.save(directChatRoom);

        return DirectChatRoomPreviewResponseDto.toDto(directChatRoom, null, 0, null);
    }

    public List<DirectChatRoomPreviewResponseDto> getDirectChatRooms(Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        List<DirectChatRoom> directChatRooms = directChatRoomValidator.findDirectChatRoomsAllByMemberId(memberId);
        List<Long> chatRoomIds = extractChatRoomIds(directChatRooms);

        Map<Long, Long> lastReadMessageIdsByChatRoom = getLastReadMessageIdsByChatRoom(chatRoomIds, member.getId());
        Map<Long, ChatMessage> latestMessageMap = chatMessageRepository.findLatestMessagesByChatRoomIds(chatRoomIds);
        Map<Long, Integer> unreadCountMap = chatMessageRepository.countUnreadMessagesByChatRoomIds(chatRoomIds, lastReadMessageIdsByChatRoom);

        return directChatRooms.stream()
                .map(directChatRoom -> {
                    ChatRoom chatRoom = directChatRoom.getChatRoom();
                    Long chatRoomId = chatRoom.getId();

                    ChatMessage lastMessage = latestMessageMap.get(chatRoomId);
                    int unreadCount = unreadCountMap.getOrDefault(chatRoomId, 0);
                    Long lastReadMessageId = lastReadMessageIdsByChatRoom.getOrDefault(chatRoomId, 0L);

                    return DirectChatRoomPreviewResponseDto.toDto(directChatRoom, lastMessage, unreadCount, lastReadMessageId);
                })
                .toList();
    }

    public List<ChatMessageResponseDto> getDirectChatRoomMessages(Long memberId, ChatMessageRequestDto dto) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        return chatMessageService.getMessagesForParticipant(member.getId(), dto);
    }

    @Transactional
    public void leaveDirectChatRoom(Long memberId, Long chatRoomId) {
        ChatRoomParticipant participant = chatRoomParticipantValidator.findParticipantByChatRoomIdAndMemberIdOrThrow(chatRoomId, memberId);

        chatRoomParticipantRepository.delete(participant);
        boolean hasRemainingParticipant = chatRoomParticipantRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId);

        if (!hasRemainingParticipant) deleteDirectChatRoom(chatRoomId);
    }

    private void deleteDirectChatRoom(Long chatRoomId) {
        chatMessageRepository.deleteByChatRoomId(chatRoomId);
        chatRoomParticipantRepository.deleteByChatRoomId(chatRoomId);
        chatRoomRepository.deleteById(chatRoomId);
    }

    private List<Long> extractChatRoomIds(List<DirectChatRoom> directChatRooms) {
        return directChatRooms.stream()
                .map(chatRoom -> chatRoom.getChatRoom().getId())
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
