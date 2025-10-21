package com.mobble.mobbleserver.application.like.service;

import com.mobble.mobbleserver.application.like.required.LikeReadPort;
import com.mobble.mobbleserver.application.like.required.LikeWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.like.clubLike.ClubLike;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.club.core.validator.ClubValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubLikeModifyService {

    private final ClubValidator clubValidator;

    private final MemberReadPort memberReadPort;

    @Qualifier("clubLikePersistenceAdapter")
    private final LikeReadPort<ClubLike> likeReadPort;

    @Qualifier("clubLikePersistenceAdapter")
    private final LikeWritePort<ClubLike> likeWritePort;

    public void toggleLike(Long clubId, Long memberId) {
        Club club = clubValidator.findClubByClubIdOrThrow(clubId);
        Member member = findMemberByMemberIdOrThrow(memberId);

        Optional<ClubLike> existLike = likeReadPort.findLike(club.getId(), member.getId());

        if (existLike.isPresent()) {
            likeWritePort.delete(existLike.get());
        } else {
            likeWritePort.save(ClubLike.createClubLike(club, member));
        }
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
