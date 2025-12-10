package com.mobble.mobbleserver.infrastructure.persistence.chat.room;

import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomReadPort;
import com.mobble.mobbleserver.application.chat.room.port.required.ChatRoomWritePort;
import com.mobble.mobbleserver.domain.chat.room.ChatRoom;
import com.mobble.mobbleserver.domain.chat.room.ClubRoomInfo;
import com.mobble.mobbleserver.domain.chat.room.DirectRoomInfo;
import com.mobble.mobbleserver.domain.chat.room.Participant;
import com.mobble.mobbleserver.infrastructure.persistence.chat.room.club.JpaClubRoomInfoRepository;
import com.mobble.mobbleserver.infrastructure.persistence.chat.room.common.JpaChatRoomRepository;
import com.mobble.mobbleserver.infrastructure.persistence.chat.room.common.JpaParticipantRepository;
import com.mobble.mobbleserver.infrastructure.persistence.chat.room.direct.JpaDirectRoomInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomPersistenceAdapter implements ChatRoomWritePort, ChatRoomReadPort {

    private final JpaChatRoomRepository chatRoomRepository;
    private final JpaParticipantRepository participantRepository;
    private final JpaClubRoomInfoRepository clubRoomInfoRepository;
    private final JpaDirectRoomInfoRepository directRoomInfoRepository;

    /* ChatRoomWritePort */
    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public void delete(ChatRoom chatRoom) {
        chatRoomRepository.delete(chatRoom);
    }

    /* ChatRoomReadPort */
    @Override
    public Optional<ChatRoom> findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId);
    }

    /* ChatRoomReadPort-Participant */
    @Override
    public List<Participant> findAllByChatRoomIdsAndMemberId(List<Long> chatRoomId, Long memberId) {
        return participantRepository.findAllByChatRoomIdsAndMemberId(chatRoomId, memberId);
    }

    /* ChatRoomReadPort-Club */
    @Override
    public Optional<ClubRoomInfo> findClubRoomInfoByClubId(Long clubId) {
        return clubRoomInfoRepository.findByClub_Id(clubId);
    }

    @Override
    public List<ChatRoom> findChatRoomsByClubIds(List<Long> clubIds) {
        return clubRoomInfoRepository.findByClub_idIn(clubIds).stream()
                .map(ClubRoomInfo::getChatRoom)
                .toList();
    }

    @Override
    public boolean existsClubRoomInfo(Long clubId) {
        return clubRoomInfoRepository.existsByClub_Id(clubId);
    }

    /* ChatRoomReadPort-Direct */
    @Override
    public List<DirectRoomInfo> findDirectChatRoomsAllByMemberId(Long memberId) {
        return directRoomInfoRepository.findDirectChatRoomsAllByMemberId(memberId);
    }

    @Override
    public boolean existsDirectChatRoomByBetweenMembers(Long senderId, Long receiverId) {
        return directRoomInfoRepository.existsDirectChatRoomByBetweenMembers(senderId, receiverId);
    }
}
