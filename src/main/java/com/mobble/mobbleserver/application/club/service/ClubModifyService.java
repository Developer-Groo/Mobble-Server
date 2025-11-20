package com.mobble.mobbleserver.application.club.service;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.category.port.required.CategoryReadPort;
import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomCreatePort;
import com.mobble.mobbleserver.application.chat.room.port.provided.common.ChatRoomExitPort;
import com.mobble.mobbleserver.application.club.command.CreateClubCommand;
import com.mobble.mobbleserver.application.club.command.UpdateClubCommand;
import com.mobble.mobbleserver.application.club.port.provided.ClubCreatePort;
import com.mobble.mobbleserver.application.club.port.provided.ClubDeletePort;
import com.mobble.mobbleserver.application.club.port.provided.ClubUpdatePort;
import com.mobble.mobbleserver.application.club.port.required.ClubWritePort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberWritePort;
import com.mobble.mobbleserver.application.image.port.required.ImageReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.category.Category;
import com.mobble.mobbleserver.domain.category.CategoryCode;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.common.Location;
import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubModifyService implements ClubCreatePort, ClubUpdatePort, ClubDeletePort {

    private final ClubChatRoomCreatePort clubChatRoomCreatePort;

    private final ClubWritePort clubWritePort;
    private final ClubMemberWritePort clubMemberWritePort;

    private final MemberReadPort memberReadPort;
    private final ImageReadPort imageReadPort;
    private final CategoryReadPort categoryReadPort;

    private final ClubMemberReadPort clubMemberReadPort;
    private final ArticleReadPort articleReadPort;

    private final ChatRoomExitPort chatRoomExitPort;

    @Override
    public Club create(CreateClubCommand command) {
        Member leader = assertMemberByMemberId(command.leaderId());
        Category category = assertCategoryByCode(command.categoryCode());
        Image mainImage = resolveMainImage(command.mainImageId());

        Location location = Location.create(
                command.address1(),
                command.address2(),
                command.city(),
                command.district(),
                command.latitude(),
                command.longitude()
        );

        Club club = Club.create(
                command.name(),
                leader,
                mainImage,
                category,
                location,
                command.ageGroup(),
                command.description(),
                command.isAutoJoin()
        );

        clubWritePort.save(club);

        ClubMember leaderMembership = ClubMember.createLeader(leader, club);
        clubMemberWritePort.save(leaderMembership);

        clubChatRoomCreatePort.createClubChatRoom(club.getId(), leader.getId());

        return club;
    }

    @Override
    public Club update(UpdateClubCommand command) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(command.clubId(), command.leaderId());

        assertLeader(clubMember);

        Club club = clubMember.getClub();
        Category category = assertCategoryByCode(command.categoryCode());
        Image mainImage = resolveMainImage(command.mainImageId());

        Location location = Location.create(
                command.address1(),
                command.address2(),
                command.city(),
                command.district(),
                command.latitude(),
                command.longitude()
        );

        club.update(
                command.name(),
                mainImage,
                category,
                location,
                command.ageGroup(),
                command.description(),
                command.isAutoJoin()
        );

        return club;
    }

    @Override
    public void delete(Long clubId, Long memberId) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(clubId, memberId);
        Club club = clubMember.getClub();

        assertLeader(clubMember);

        chatRoomExitPort.delete(club.getId());

        List<Long> articleIds = articleReadPort.findIdsByClubId(club.getId());

        clubMemberWritePort.deleteAllClubMemberByClubId(club.getId());

        clubWritePort.delete(club);
    }

    /* ==== Private Helper ==== */
    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER)); // Todo: Error 수정 필요
    }

    private ClubMember assertClubMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB)); // Todo: Error 수정 필요
    }

    private Category assertCategoryByCode(CategoryCode code) {
        return categoryReadPort.findByCode(code)
                .orElseThrow(); // Todo: Error 수정 필요
    }

    private void assertLeader(ClubMember clubMember) {
        if (!clubMember.isLeader()) throw new DomainException(ClubMemberErrorCode.NO_PERMISSION); // Todo: Error 수정 필요
    }

    private Image resolveMainImage(Long imageId) {
        return (imageId == null)
                ? null // Todo: getDefaultImage 메서드 호출
                : assertImageByImageId(imageId);
    }

    private Image assertImageByImageId(Long imageId) {
        return imageReadPort.findById(imageId)
                .orElseThrow(); // Todo: Error 수정 필요
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
