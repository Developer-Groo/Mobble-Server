package com.mobble.mobbleserver.domain.club.club.service;

import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.club.club.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.club.club.repository.ClubRepository;
import com.mobble.mobbleserver.domain.club.clubAgeGroup.entity.ClubAgeGroupType;
import com.mobble.mobbleserver.domain.club.clubAgeGroup.repository.ClubAgeGroupRepository;
import com.mobble.mobbleserver.domain.club.club.dto.response.ClubResponseDto;
import com.mobble.mobbleserver.domain.club.club.repository.dto.ClubLikeInfoDto;
import com.mobble.mobbleserver.domain.club.club.validator.ClubValidator;
import com.mobble.mobbleserver.domain.club.clubAgeGroup.entity.ClubAgeGroup;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.clubCategory.repository.ClubCategoryRepository;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.entity.JoinStatus;
import com.mobble.mobbleserver.domain.clubMember.repository.ClubMemberRepository;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import com.mobble.mobbleserver.domain.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.like.clubLike.repository.ClubLikeRepository;
import com.mobble.mobbleserver.domain.like.commentLike.repository.CommentLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubCategoryRepository clubCategoryRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubAgeGroupRepository clubAgeGroupRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final ClubLikeRepository clubLikeRepository;

    private final ClubValidator clubValidator;
    private final MemberValidator memberValidator;
    private final ClubMemberValidator clubMemberValidator;

    private final EntityManager entityManager;

    @Transactional
    public ClubResponseDto createClub(Long memberId, ClubRequestDto dto) {
        ClubCategory category = findCategoryOrThrow(dto.category());
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        Club club = dto.toEntity(category);
        clubRepository.save(club);

        ClubMember clubMember = ClubMember.createClubMember(member, club, ClubMemberRole.LEADER, JoinStatus.APPROVED);
        clubMemberRepository.save(clubMember);

        List<ClubAgeGroup> ageGroups = createClubAgeGroups(club, dto.ageGroup());
        clubAgeGroupRepository.saveAll(ageGroups);

        return buildClubResponse(club, member, member.getName());
    }

    public ClubResponseDto findClubById(Long clubId, Long memberId) {
        Club club = clubValidator.findClubByClubIdOrThrow(clubId);
        ClubMember leader = clubMemberRepository
                .findByClubIdAndClubMemberRole(clubId, ClubMemberRole.LEADER).get();
        String leaderName = leader.getMember().getName();

        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        return buildClubResponse(club, member, leaderName);
    }

    @Transactional
    public ClubResponseDto updateClub(Long clubId, Long memberId, ClubRequestDto dto) {
        Club club = clubValidator.findClubByClubIdOrThrow(clubId);
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);
        validateLeader(clubId, memberId);

        ClubCategory category = findCategoryOrThrow(dto.category());
        club.updateClub(category, dto.name(), dto.ground(), dto.address(), dto.headcount(), dto.isAutoJoin());

        clubAgeGroupRepository.deleteAllClubAgeGroupByClubId(club.getId());
        List<ClubAgeGroup> newAgeGroups = createClubAgeGroups(club, dto.ageGroup());
        clubAgeGroupRepository.saveAll(newAgeGroups);

        return buildClubResponse(club, member, member.getName());
    }

    @Transactional
    public void deleteClub(Long clubId, Long memberId) {
        Club club = clubValidator.findClubByClubIdOrThrow(clubId);
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);
        validateLeader(clubId, memberId);

        clubLikeRepository.deleteClubLikeAllByClub_Id(clubId);

        List<Long> articleIds = articleRepository.findArticleIdsByClubId(clubId);

        commentLikeRepository.deleteAllCommentLikeByComment_Article_IdIn(articleIds);
        commentRepository.deleteAllCommentByArticle_IdIn(articleIds);
        articleLikeRepository.deleteAllArticleLikeByArticle_IdIn(articleIds);
        articleRepository.deleteAllArticleByClub_Id(clubId);
        clubMemberRepository.deleteAllClubMemberByClubId(clubId);

        clubLikeRepository.deleteClubLikeAllByClub_Id(clubId);
        clubAgeGroupRepository.deleteAllClubAgeGroupByClubId(clubId);

        clubRepository.deleteById(clubId);
    }

    private ClubCategory findCategoryOrThrow(String categoryName) {
        return clubCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new DomainException(ClubErrorCode.CATEGORY_NOT_FOUND));
    }

    private void validateLeader(Long clubId, Long memberId) {
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        if (!clubMember.getClubMemberRole().equals(ClubMemberRole.LEADER)) {
            throw new DomainException(ClubErrorCode.NO_PERMISSION);
        }
    }

    private ClubResponseDto buildClubResponse(Club club, Member member, String leaderName) {
        List<ClubAgeGroupType> ageGroupList = clubAgeGroupRepository.findByClubId(club.getId()).stream()
                .map(ClubAgeGroup::getAgeGroupType)
                .toList();

        ClubLikeInfoDto likeInfo = clubRepository.findLikeInfoByClubIdAndMemberId(club.getId(), member.getId());

        return ClubResponseDto.toDto(club, leaderName, ageGroupList, likeInfo);
    }

    private List<ClubAgeGroup> createClubAgeGroups(Club club, List<ClubAgeGroupType> ageGroupTypes) {
        return ageGroupTypes.stream()
                .map(age -> ClubAgeGroup.createClubAgeGroup(club, age))
                .toList();
    }
}
