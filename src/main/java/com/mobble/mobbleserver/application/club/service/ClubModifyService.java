package com.mobble.mobbleserver.application.club.service;

import com.mobble.mobbleserver.application.article.port.provided.ArticleDeletePort;
import com.mobble.mobbleserver.application.category.error.CategoryBusinessError;
import com.mobble.mobbleserver.application.category.port.required.CategoryReadPort;
import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomCreatePort;
import com.mobble.mobbleserver.application.chat.room.port.provided.common.ChatRoomExitPort;
import com.mobble.mobbleserver.application.club.command.CreateClubCommand;
import com.mobble.mobbleserver.application.club.command.UpdateClubCommand;
import com.mobble.mobbleserver.application.club.port.provided.ClubCreatePort;
import com.mobble.mobbleserver.application.club.port.provided.ClubDeletePort;
import com.mobble.mobbleserver.application.club.port.provided.ClubUpdatePort;
import com.mobble.mobbleserver.application.club.port.required.ClubWritePort;
import com.mobble.mobbleserver.application.clubMember.error.ClubMemberBusinessError;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberWritePort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.image.error.ImageBusinessError;
import com.mobble.mobbleserver.application.image.port.required.ImageReadPort;
import com.mobble.mobbleserver.application.image.port.required.ImageWritePort;
import com.mobble.mobbleserver.application.like.port.provided.LikeModifyPort;
import com.mobble.mobbleserver.application.member.error.MemberBusinessError;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.category.Category;
import com.mobble.mobbleserver.domain.category.CategoryCode;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.common.Location;
import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.image.ImageType;
import com.mobble.mobbleserver.domain.like.LikeType;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubModifyService implements ClubCreatePort, ClubUpdatePort, ClubDeletePort {

    private final ArticleDeletePort articleDeletePort;
    private final ClubChatRoomCreatePort clubChatRoomCreatePort;
    private final ChatRoomExitPort chatRoomExitPort;
    private final LikeModifyPort likeModifyPort;

    private final ClubWritePort clubWritePort;
    private final ClubMemberWritePort clubMemberWritePort;
    private final ImageWritePort imageWritePort;

    private final MemberReadPort memberReadPort;
    private final ClubMemberReadPort clubMemberReadPort;
    private final ImageReadPort imageReadPort;
    private final CategoryReadPort categoryReadPort;

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
                command.ageGroup(), // Todo: List 로 여러 type 을 선택하게 할지 결정 필요
                command.description(),
                command.isAutoJoin()
        );

        clubWritePort.save(club);

        ClubMember leaderMembership = ClubMember.createLeader(leader, club);
        clubMemberWritePort.save(leaderMembership);

        clubChatRoomCreatePort.createClubChatRoom(club.getId(), leader.getId());

        // Todo: jwt 토큰 재발급 필요

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

        articleDeletePort.deleteAll(club.getId());
        likeModifyPort.delete(LikeType.CLUB, club.getId());
        chatRoomExitPort.delete(club.getId());

        // Todo:
        //  1. Meeting -> Service port
        //  2. Notification delete -> Service port

        clubMemberWritePort.deleteAllByClubId(club.getId());
        imageWritePort.delete(club.getMainImage());
        clubWritePort.delete(club);
    }

    /* ==== Private Helper ==== */
    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new BusinessException(MemberBusinessError.NOT_FOUND));
    }

    private ClubMember assertClubMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new BusinessException(ClubMemberBusinessError.NOT_JOINED_CLUB));
    }

    private Category assertCategoryByCode(CategoryCode code) {
        return categoryReadPort.findByCode(code)
                .orElseThrow(() -> new BusinessException(CategoryBusinessError.NOT_FOUND));
    }

    private void assertLeader(ClubMember clubMember) {
        if (!clubMember.isLeader()) throw new BusinessException(ClubMemberBusinessError.ONLY_LEADER_ALLOWED);
    }

    private Image assertImageByImageId(Long imageId) {
        return imageReadPort.findById(imageId)
                .orElseThrow(() -> new BusinessException(ImageBusinessError.NOT_FOUND));
    }

    private Image assertDefaultImageByImageType() {
        return imageReadPort.findDefaultByType(ImageType.CLUB_MAIN)
                .orElseThrow(() -> new BusinessException(ImageBusinessError.NOT_FOUND));
    }

    private Image resolveMainImage(Long imageId) {
        return (imageId == null)
                ? assertDefaultImageByImageType()
                : assertImageByImageId(imageId);
    }
}
