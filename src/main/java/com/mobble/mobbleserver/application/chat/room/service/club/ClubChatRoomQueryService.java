package com.mobble.mobbleserver.application.chat.room.service.club;

import com.mobble.mobbleserver.application.chat.message.port.required.MessageReadPort;
import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomQueryPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.Participant;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.infrastructure.web.chat.room.club.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.validator.ClubMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubChatRoomQueryService implements ClubChatRoomQueryPort {

    private final MemberReadPort memberReadPort;
    private final ChatRoomReadPort chatRoomReadPort;
    private final MessageReadPort messageReadPort;

    // Todo: port 변경 필요
    private final ClubMemberValidator clubMemberValidator;

    @Override
    public List<ClubChatRoomPreviewResponseDto> getClubChatRoomsPreview(Long memberId) {
        Member member = findMemberByMemberIdOrThrow(memberId);

        List<ClubMember> clubMembers = clubMemberValidator.findAllClubMemberByMemberId(member.getId());
        List<Long> chatRoomIds = extractChatRoomIds(clubMembers);

        Map<Long, Long> lastReadMessageIdsByChatRoom = getLastReadMessageIdsByChatRoom(chatRoomIds, member.getId());
        Map<Long, ChatMessage> latestMessagesMap = messageReadPort.findLatestMessagesByChatRoomIds(chatRoomIds);
        Map<Long, Integer> unreadCountMap = messageReadPort.countUnreadMessagesByChatRoomIds(chatRoomIds, lastReadMessageIdsByChatRoom);

        return clubMembers.stream()
                .map(clubMember -> {
                    Club club = clubMember.getClub();
                    ChatRoom chatRoom = club.getClubRoomInfo().getChatRoom();
                    Long chatRoomId = chatRoom.getId();

                    ChatMessage lastMessage = latestMessagesMap.get(chatRoomId);
                    int unreadCount = unreadCountMap.getOrDefault(chatRoomId, 0);
                    Long lastReadMessageId = lastReadMessageIdsByChatRoom.getOrDefault(chatRoomId, 0L);

                    return ClubChatRoomPreviewResponseDto.toDto(chatRoom, club, lastMessage, unreadCount, lastReadMessageId);
                })
                .toList();
    }

    private List<Long> extractChatRoomIds(List<ClubMember> clubMembers) {
        return clubMembers.stream()
                .map(cm -> cm.getClub().getClubRoomInfo().getChatRoom().getId())
                .toList();
    }

    private Map<Long, Long> getLastReadMessageIdsByChatRoom(List<Long> chatRoomIds, Long memberId) {
        return chatRoomReadPort.findAllByChatRoomIdsAndMemberId(chatRoomIds, memberId)
                .stream()
                .collect(Collectors.toMap(
                        participant -> participant.getChatRoom().getId(),
                        Participant::getLastReadMessageId
                ));
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}

