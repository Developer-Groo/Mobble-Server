package com.mobble.mobbleserver.application.club.service;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomCreatePort;
import com.mobble.mobbleserver.application.chat.room.port.provided.common.ChatRoomExitPort;
import com.mobble.mobbleserver.application.club.port.provided.ClubCreatePort;
import com.mobble.mobbleserver.application.club.port.provided.ClubDeletePort;
import com.mobble.mobbleserver.application.club.port.provided.ClubUpdatePort;
import com.mobble.mobbleserver.application.club.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.club.port.required.ClubWritePort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.infrastructure.web.club.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.response.ClubResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubModifyService implements ClubCreatePort, ClubUpdatePort, ClubDeletePort {

    private final ClubWritePort clubWritePort;
    private final ClubMemberWritePort clubMemberWritePort;

    private final ClubReadPort clubReadPort;
    private final MemberReadPort memberReadPort;
    private final ClubMemberReadPort clubMemberReadPort;
    private final ArticleReadPort articleReadPort;

    private final ClubChatRoomCreatePort clubChatRoomCreatePort;
    private final ChatRoomExitPort chatRoomExitPort;

    @Override
    public ClubResponseDto createClub(Long memberId, ClubRequestDto dto) {
        Member member = findMemberByMemberIdOrThrow(memberId);
//
//        clubWritePort.save(club);
//
//        List<Long> codeList = dto.groundCodes();
////        List<ClubGround> clubGrounds = createClubGroundList(codeList, club);
////        clubGroundWritePort.saveAll(clubGrounds);
//
//        ClubMember clubMember = ClubMember.createClubMember(member, club, ClubMemberRole.LEADER, JoinStatus.APPROVED);
//        clubMemberWritePort.save(clubMember);
//
//        List<AgeGroup> ageGroups = createClubAgeGroups(club, dto.ageGroup());
//
//        // Todo: 반환값이 Club 채팅방의 preview 에 필요한 데이터이기 때문에 반환 DTO에 포함 되어야 함
//        ClubChatRoomPreviewResponseDto clubChatRoom = clubChatRoomCreatePort.createClubChatRoom(club.getId(), member.getId());
//
//        return buildClubResponse(club, member, member.getName());
        return null;
    }

    @Override
    public ClubResponseDto updateClub(Long clubId, Long memberId, ClubRequestDto dto) {
        Club club = findClubByClubIdOrThrow(clubId);
        Member member = findMemberByMemberIdOrThrow(memberId);
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        assertLeader(clubMember);

//        ClubCategory category = findCategoryOrThrow(dto.category());
//
//        Address address = club.getAddress();
//        AddressRequestDto addrDto = dto.addressDto();
//
//        address.updateAddress(addrDto);
//        club.updateClub(category, dto.name(), address, dto.headcount(), dto.isAutoJoin());
//
////        clubGroundWritePort.deleteAllByClubId(club.getId());
////
////        List<ClubGround> newClubGrounds = createClubGroundList(dto.groundCodes(), club);
////        clubGroundWritePort.saveAll(newClubGrounds);
//        ageGroupWritePort.deleteAllClubAgeGroupByClubId(club.getId());
//
//        List<AgeGroup> newAgeGroups = createClubAgeGroups(club, dto.ageGroup());
//        ageGroupWritePort.saveAll(newAgeGroups);
//
//        return buildClubResponse(club, member, member.getName());
        return null;
    }

    @Override
    public void deleteClub(Long clubId, Long memberId) {
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Club club = clubMember.getClub();

        assertLeader(clubMember);

        chatRoomExitPort.delete(club.getId());

        List<Long> articleIds = articleReadPort.findIdsByClubId(club.getId());

        clubMemberWritePort.deleteAllClubMemberByClubId(club.getId());

        clubWritePort.delete(club);
    }

    private Club findClubByClubIdOrThrow(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new DomainException((ClubErrorCode.NOT_FOUND)));
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    private ClubMember findClubMemberByClubIdAndMemberIdOrThrow(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB));
    }

    private void assertLeader(ClubMember clubMember) {
        if (!clubMember.isLeader()) throw new DomainException(ClubMemberErrorCode.NO_PERMISSION);
    }

//    private ClubCategory findCategoryOrThrow(String categoryName) {
//        return clubCategoryReadPort.findByName(categoryName)
//                .orElseThrow(() -> new DomainException(ClubErrorCode.CATEGORY_NOT_FOUND));
//    }
//
//    private List<AgeGroup> createClubAgeGroups(Club club, List<AgeGroup> ageGroups) {
//        return ageGroups.stream()
//                .map(age -> AgeGroup.createAgeGroup(club, age))
//                .toList();
//    }

//    private ClubResponseDto buildClubResponse(Club club, Member member, String leaderName) {
//        List<AgeGroup> ageGroupList = ageGroupReadPort.findByClubId(club.getId()).stream()
//                .map(AgeGroup::getAgeGroupType)
//                .toList();
//
//        List<Long> groundCodes = clubGroundReadPort.findByClubId(club.getId())
//                .stream()
//                .map(cg -> cg.getGround().getCode())
//                .collect(Collectors.toList());
//
//        List<GroundResponseDto> groundList = groundReadPort.findAllById(groundCodes)
//                .stream()
//                .map(GroundResponseDto::toDto)
//                .toList();
//
//        ClubLikeInfoDto likeInfo = clubReadPort.findLikeInfoByClubIdAndMemberId(club.getId(), member.getId());
//
//        return ClubResponseDto.toDto(club, leaderName, address, ageGroupList, groundList, likeInfo);
//        return null;
//    }
}
