package com.mobble.mobbleserver.application.liked.core.service.core;

import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.liked.core.port.required.LikeReadPort;
import com.mobble.mobbleserver.application.liked.core.port.required.LikeWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.like.core.ClubLike;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubLikeModifyService {

    @Qualifier("clubLikePersistenceAdapter")
    private final LikeReadPort<ClubLike> likeReadPort;

    @Qualifier("clubLikePersistenceAdapter")
    private final LikeWritePort<ClubLike> likeWritePort;

    private final MemberReadPort memberReadPort;
    private final ClubReadPort clubReadPort;

    public void toggleLike(Long clubId, Long memberId) {
        Club club = findClubByClubIdOrThrow(clubId);
        Member member = findMemberByMemberIdOrThrow(memberId);

        Optional<ClubLike> existLike = likeReadPort.findLike(club.getId(), member.getId());

        if (existLike.isPresent()) {
            likeWritePort.delete(existLike.get());
        } else {
            likeWritePort.save(ClubLike.createClubLike(member.getId(), club.getId()));
        }
    }

    private Club findClubByClubIdOrThrow(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new DomainException(ClubErrorCode.NOT_FOUND));
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }


}
