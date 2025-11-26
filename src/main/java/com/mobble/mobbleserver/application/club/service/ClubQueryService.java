package com.mobble.mobbleserver.application.club.service;

import com.mobble.mobbleserver.application.club.error.ClubBusinessError;
import com.mobble.mobbleserver.application.club.port.provided.ClubQueryPort;
import com.mobble.mobbleserver.application.club.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.club.result.ClubResult;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.like.port.provided.LikeQueryPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.like.LikeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubQueryService implements ClubQueryPort {

    private final LikeQueryPort likeQueryPort;

    private final ClubReadPort clubReadPort;
    private final MemberReadPort memberReadPort;

    @Override
    public ClubResult getClub(Long clubId, Long memberId) {
        assertMemberByMemberId(memberId);
        Club club = assertClubByClubId(clubId);

        int likeCount = likeQueryPort.getLikeCount(LikeType.CLUB, club.getId());
        List<Long> likedIds = likeQueryPort.getLikedIds(LikeType.CLUB, memberId, List.of(club.getId()));

        return ClubResult.create(club, likeCount, likedIds);
    }

    // Todo: 여러 조건 으로 Club 검색 기능 필요

    /* ==== Private Helper ==== */
    private void assertMemberByMemberId(Long memberId) {
        memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(); // Todo: Error 수정 필요
    }

    private Club assertClubByClubId(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new BusinessException(ClubBusinessError.NOT_FOUND));
    }
}
