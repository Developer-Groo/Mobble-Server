package com.mobble.mobbleserver.domain.like.clubLike.service;

import com.mobble.mobbleserver.domain.like.clubLike.repository.ClubLikeRepository;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubLikeService {

    private final MemberRepository memberRepository;
//    private final ClubRepository clubRepository;
    private final ClubLikeRepository clubLikeRepository;

/*
    @Transactional
    public ClubLikeResponseDto toggleLike(Long clubId, Long memberId) {
        Member member = findMemberOrThrow(memberId);
//        Club club = findClubOrThrow(clubId)

        Optional<ClubLike> checkLiked = clubLikeRepository.findLikedByClubIdAndMemberId(club.getId(), member.getId());

        if (checkLiked.isPresent()) {
            clubLikeRepository.delete(checkLiked.get());
        } else {
            ClubLike clubLike = ClubLike.createClubLike(club, member);
            clubLikeRepository.save(clubLike);
        }

        boolean isLiked = checkLiked.isEmpty();
        int likeCount = clubLikeRepository.countClubLikesByClubId(club.getId());

        return ClubLikeResponseDto.toDto(club.getId(), isLiked, likeCount);
    }
*/
/*
    public ClubLikeResponseDto getClubLikeCount(Long clubId, Long memberId) {
        Club club = findClubOrThrow(clubId);
        boolean isLiked = (memberId != null) && clubLikeRepository.existsByClubIdAndMemberId(club.getId(), memberId);
        int likeCount = clubLikeRepository.countClubLikesByClubId(club.getId());

        return ClubLikeResponseDto.toDto(club.getId(), isLiked, likeCount);
    }
*/
/*

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용
    }
*/
/*
    private Club findClubOrThrow(Long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException(""));
    }
*/
}
