package com.mobble.mobbleserver.refactor.like.clubLike.service;

import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.refactor.like.baseLike.service.AbstractLikeService;
import com.mobble.mobbleserver.domain.like.core.ClubLike;
import com.mobble.mobbleserver.refactor.like.clubLike.repository.ClubLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClubLikeService extends AbstractLikeService<Club, ClubLike> {

    private final ClubLikeRepository clubLikeRepository;

    private final ClubReadPort clubReadPort;

    @Override
    public LikeType getType() {
        return LikeType.CLUB;
    }

    @Override
    protected Club getTarget(Long targetId) {
        return findClubByClubIdOrThrow(targetId);
    }

    @Override
    protected Optional<ClubLike> findExistingLike(Club club, Member member) {
        return clubLikeRepository.findLikedByClubIdAndMemberId(club.getId(), member.getId());
    }

    @Override
    protected ClubLike createLike(Club club, Member member) {
        return ClubLike.createClubLike(club, member);
    }

    @Override
    protected void saveLike(ClubLike entity) {
        clubLikeRepository.save(entity);
    }

    @Override
    protected void deleteLike(ClubLike entity) {
        clubLikeRepository.delete(entity);
    }

    private Club findClubByClubIdOrThrow(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new DomainException((ClubErrorCode.NOT_FOUND)));
    }
}
