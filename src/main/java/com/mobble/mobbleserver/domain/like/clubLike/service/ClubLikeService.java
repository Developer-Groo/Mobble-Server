package com.mobble.mobbleserver.domain.like.clubLike.service;

import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.club.club.validator.ClubValidator;
import com.mobble.mobbleserver.domain.like.clubLike.entity.ClubLike;
import com.mobble.mobbleserver.domain.like.clubLike.repository.ClubLikeRepository;
import com.mobble.mobbleserver.domain.like.baseLike.entity.LikeType;
import com.mobble.mobbleserver.domain.like.baseLike.service.AbstractLikeService;
import com.mobble.mobbleserver.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClubLikeService extends AbstractLikeService<Club, ClubLike> {

    private final ClubLikeRepository clubLikeRepository;
    private final ClubValidator clubValidator;

    @Override
    public LikeType getType() {
        return LikeType.CLUB;
    }

    @Override
    protected Club getTarget(Long targetId) {
        return clubValidator.findClubByClubIdOrThrow(targetId);
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
}
