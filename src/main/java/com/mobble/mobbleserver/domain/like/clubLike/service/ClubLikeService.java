package com.mobble.mobbleserver.domain.like.clubLike.service;

import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.club.club.validator.ClubValidator;
import com.mobble.mobbleserver.domain.like.clubLike.entity.ClubLike;
import com.mobble.mobbleserver.domain.like.clubLike.repository.ClubLikeRepository;
import com.mobble.mobbleserver.domain.like.entity.LikeType;
import com.mobble.mobbleserver.domain.like.service.AbstractLikeService;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClubLikeService extends AbstractLikeService<Club, ClubLike> {

    private final ClubLikeRepository clubLikeRepository;
    private final ClubValidator clubValidator;

    public ClubLikeService(MemberValidator memberValidator, ClubLikeRepository clubLikeRepository, ClubValidator clubValidator) {
        super(memberValidator);
        this.clubLikeRepository = clubLikeRepository;
        this.clubValidator = clubValidator;
    }

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
