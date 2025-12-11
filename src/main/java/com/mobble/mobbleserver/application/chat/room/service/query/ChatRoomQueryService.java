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

        FilteredChatRoomSources sources = loadChatRoomSources(member, filter);

        List<ChatRoom> chatRooms = extractChatRooms(sources);
        if (chatRooms.isEmpty()) return List.of();

        List<Long> chatRoomIds = chatRooms.stream()
                .map(ChatRoom::getId)
                .distinct()
                .toList();

        ChatRoomMessageMeta meta = loadChatRoomMessageMeta(chatRoomIds, member.getId());

        return buildPreviewResults(member, chatRooms, meta);
    }

    /* ==== Private Helper ==== */
    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new BusinessException(MemberBusinessError.NOT_FOUND));
    }

    private FilteredChatRoomSources loadChatRoomSources(Member member, ChatRoomPreviewFilter filter) {
        ChatRoomPreviewFilter effectiveFilter = (filter == null) ? ChatRoomPreviewFilter.ALL : filter;

        List<DirectRoomInfo> directRoomInfos;
        List<ClubMember> clubMembers;

        switch (effectiveFilter) {
            case ALL -> {
                directRoomInfos = chatRoomReadPort.findDirectChatRoomsAllByMemberId(member.getId());
                clubMembers = clubMemberReadPort.findAllClubMemberByMemberId(member.getId());
            }
            case DIRECT_ONLY -> {
                directRoomInfos = chatRoomReadPort.findDirectChatRoomsAllByMemberId(member.getId());
                clubMembers = List.of();
            }
            case CLUB_ONLY -> {
                directRoomInfos = List.of();
                clubMembers = clubMemberReadPort.findAllClubMemberByMemberId(member.getId());
            }
            default -> throw new IllegalStateException(""); // Todo: Error 수정
        }

        return new FilteredChatRoomSources(directRoomInfos, clubMembers);
    }

    private List<ChatRoom> extractChatRooms(FilteredChatRoomSources sources) {
        List<ChatRoom> directChatRooms = extractDirectChatRooms(sources.directRooms());
        List<ChatRoom> clubChatRooms = extractClubChatRooms(sources.clubMembers());

        ArrayList<ChatRoom> allChatRooms = new ArrayList<>();
        allChatRooms.addAll(directChatRooms);
        allChatRooms.addAll(clubChatRooms);

        return allChatRooms;
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

    private ChatRoomMessageMeta loadChatRoomMessageMeta(List<Long> chatRoomIds, Long memberId) {
        if (chatRoomIds == null || chatRoomIds.isEmpty()) {
            return new ChatRoomMessageMeta(Map.of(), Map.of(), Map.of());
        }

        Map<Long, Long> lastReadMessageIdsByChatRoom = getLastReadMessageIdsByChatRoom(chatRoomIds, memberId);
        Map<Long, ChatMessage> latestMessagesMap = messageReadPort.findLatestMessagesByChatRoomIds(chatRoomIds);
        Map<Long, Integer> unreadCountMap = messageReadPort.countUnreadMessagesByChatRoomIds(chatRoomIds, lastReadMessageIdsByChatRoom);

        return new ChatRoomMessageMeta(lastReadMessageIdsByChatRoom, latestMessagesMap, unreadCountMap);
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

    private List<ChatRoomPreviewResult> buildPreviewResults(Member member, List<ChatRoom> chatRooms, ChatRoomMessageMeta meta) {
        List<ChatRoomPreviewResult> results = new ArrayList<>();

        chatRooms.forEach(chatRoom ->
                results.add(ChatRoomPreviewResult.create(
                        member,
                        chatRoom,
                        meta.lastReadMessageIdsByChatRoom(),
                        meta.latestMessagesMap(),
                        meta.unreadCountMap()
                ))
        );

        results.sort(Comparator.comparing(
                ChatRoomPreviewResult::latestMessageAt,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        return results;
    }

    /* ==== Internal DTO records ==== */
    private record FilteredChatRoomSources(
            List<DirectRoomInfo> directRooms,
            List<ClubMember> clubMembers
    ) {}

    private record ChatRoomMessageMeta(
            Map<Long, Long> lastReadMessageIdsByChatRoom,
            Map<Long, ChatMessage> latestMessagesMap,
            Map<Long, Integer> unreadCountMap
    ) {}
}
