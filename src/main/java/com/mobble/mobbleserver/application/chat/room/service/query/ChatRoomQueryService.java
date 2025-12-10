package com.mobble.mobbleserver.application.chat.room.service.query;

import com.mobble.mobbleserver.application.chat.message.port.required.MessageReadPort;
import com.mobble.mobbleserver.application.chat.room.port.provided.query.ChatRoomQueryPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.chat.room.result.ChatRoomPreviewResult;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.member.error.MemberBusinessError;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.chat.message.ChatMessage;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomQueryService implements ChatRoomQueryPort {

    private final MemberReadPort memberReadPort;
    private final ClubMemberReadPort clubMemberReadPort;
    private final ChatRoomReadPort chatRoomReadPort;
    private final MessageReadPort messageReadPort;

    @Override
    public List<ChatRoomPreviewResult> getChatRoomsPreview(Long memberId, ChatRoomPreviewFilter filter) {
        Member member = assertMemberByMemberId(memberId);

        List<DirectRoomInfo> directRoomInfos = (filter == ChatRoomPreviewFilter.CLUB_ONLY)
                ? List.of()
                : chatRoomReadPort.findDirectChatRoomsAllByMemberId(member.getId());

        List<ClubMember> clubMembers = (filter == ChatRoomPreviewFilter.DIRECT_ONLY)
                ? List.of()
                : clubMemberReadPort.findAllClubMemberByMemberId(member.getId());

        List<ChatRoom> directChatRooms = extractDirectChatRooms(directRoomInfos);
        List<ChatRoom> clubChatRooms = extractClubChatRooms(clubMembers);

        ArrayList<ChatRoom> allChatRooms = new ArrayList<>();
        allChatRooms.addAll(directChatRooms);
        allChatRooms.addAll(clubChatRooms);

        if (allChatRooms.isEmpty()) return List.of();

        List<Long> chatRoomIds = allChatRooms.stream()
                .map(ChatRoom::getId)
                .distinct()
                .toList();

        Map<Long, Long> lastReadMessageIdsByChatRoom = getLastReadMessageIdsByChatRoom(chatRoomIds, member.getId());
        Map<Long, ChatMessage> latestMessagesMap = messageReadPort.findLatestMessagesByChatRoomIds(chatRoomIds);
        Map<Long, Integer> unreadCountMap = messageReadPort.countUnreadMessagesByChatRoomIds(chatRoomIds, lastReadMessageIdsByChatRoom);

        List<ChatRoomPreviewResult> results = new ArrayList<>();

        allChatRooms.forEach(chatRoom ->
            results.add(ChatRoomPreviewResult.create(
                    member,
                    chatRoom,
                    lastReadMessageIdsByChatRoom,
                    latestMessagesMap,
                    unreadCountMap
            ))
        );

        results.sort(Comparator.comparing(
                ChatRoomPreviewResult::latestMessageAt,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        return results;
    }

    /* ==== Private Helper ==== */
    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new BusinessException(MemberBusinessError.NOT_FOUND));
    }

    private List<ChatRoom> extractDirectChatRooms(List<DirectRoomInfo> directRoomInfos) {
        if (directRoomInfos == null || directRoomInfos.isEmpty()) return List.of();

        return directRoomInfos.stream()
                .map(DirectRoomInfo::getChatRoom)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private List<ChatRoom> extractClubChatRooms(List<ClubMember> clubMembers) {
        if (clubMembers == null || clubMembers.isEmpty()) return List.of();

        List<Long> clubIds = clubMembers.stream()
                .map(clubMember -> clubMember.getClub().getId())
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (clubIds.isEmpty()) return List.of();

        return chatRoomReadPort.findChatRoomsByClubIds(clubIds);
    }

    private Map<Long, Long> getLastReadMessageIdsByChatRoom(List<Long> chatRoomIds, Long memberId) {
        if (chatRoomIds == null || chatRoomIds.isEmpty()) return Map.of();

        return chatRoomReadPort.findAllByChatRoomIdsAndMemberId(chatRoomIds, memberId)
                .stream()
                .collect(Collectors.toMap(
                        participant -> participant.getChatRoom().getId(),
                        participant -> Optional.ofNullable(participant.getLastReadMessageId()).orElse(0L),
                        (existing, replacement) -> replacement
                ));
    }
}
