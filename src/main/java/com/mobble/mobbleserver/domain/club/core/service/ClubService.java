package com.mobble.mobbleserver.domain.club.core.service;

import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.service.ClubChatRoomService;
import com.mobble.mobbleserver.domain.club.ageGroup.entity.AgeGroup;
import com.mobble.mobbleserver.domain.club.ageGroup.entity.AgeGroupType;
import com.mobble.mobbleserver.domain.club.ageGroup.repository.AgeGroupRepository;
import com.mobble.mobbleserver.domain.club.core.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.domain.club.core.dto.response.ClubResponseDto;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.club.core.repository.ClubRepository;
import com.mobble.mobbleserver.domain.club.core.repository.dto.ClubLikeInfoDto;
import com.mobble.mobbleserver.domain.club.core.validator.ClubValidator;
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
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private final ClubChatRoomService clubChatRoomService;

    private final ClubRepository clubRepository;
    private final ClubCategoryRepository clubCategoryRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final AgeGroupRepository ageGroupRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final ClubLikeRepository clubLikeRepository;

    private final ClubValidator clubValidator;
    private final MemberValidator memberValidator;
    private final ClubMemberValidator clubMemberValidator;

    @Transactional
    public ClubResponseDto createClub(Long memberId, ClubRequestDto dto) {
        ClubCategory category = findCategoryOrThrow(dto.category());
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        Club club = dto.toEntity(category);
        clubRepository.save(club);

        ClubMember clubMember = ClubMember.createClubMember(member, club, ClubMemberRole.LEADER, JoinStatus.APPROVED);
        clubMemberRepository.save(clubMember);

        List<AgeGroup> ageGroups = createClubAgeGroups(club, dto.ageGroup());
        ageGroupRepository.saveAll(ageGroups);

        // Todo: 반환값이 Club 채팅방의 preview 에 필요한 데이터이기 때문에 반환 DTO에 포함 되어야 함
        ClubChatRoomPreviewResponseDto clubChatRoom = clubChatRoomService.createClubChatRoom(club.getId(), member.getId());

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
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        assertLeader(clubMember);

        ClubCategory category = findCategoryOrThrow(dto.category());
        club.updateClub(category, dto.name(), dto.ground(), dto.address(), dto.headcount(), dto.isAutoJoin());

        ageGroupRepository.deleteAllClubAgeGroupByClubId(club.getId());
        List<AgeGroup> newAgeGroups = createClubAgeGroups(club, dto.ageGroup());
        ageGroupRepository.saveAll(newAgeGroups);

        return buildClubResponse(club, member, member.getName());
    }

    @Transactional
    public void deleteClub(Long clubId, Long memberId) {
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Club club = clubMember.getClub();

        assertLeader(clubMember);

        clubChatRoomService.deleteClubChatRoom(club.getId());

        List<Long> articleIds = articleRepository.findArticleIdsByClubId(club.getId());

        commentLikeRepository.deleteAllCommentLikeByComment_Article_IdIn(articleIds);
        commentRepository.deleteAllCommentByArticle_IdIn(articleIds);
        articleLikeRepository.deleteAllArticleLikeByArticle_IdIn(articleIds);
        articleRepository.deleteAllArticleByClub_Id(club.getId());
        clubMemberRepository.deleteAllClubMemberByClubId(club.getId());

        clubLikeRepository.deleteClubLikeAllByClub_Id(club.getId());
        ageGroupRepository.deleteAllClubAgeGroupByClubId(club.getId());

        clubRepository.deleteById(club.getId());
    }

    private ClubCategory findCategoryOrThrow(String categoryName) {
        return clubCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new DomainException(ClubErrorCode.CATEGORY_NOT_FOUND));
    }

    private ClubResponseDto buildClubResponse(
            Club club,
            Member member,
            String leaderName
    ) {
        List<AgeGroupType> ageGroupList = ageGroupRepository.findByClubId(club.getId()).stream()
                .map(AgeGroup::getAgeGroupType)
                .toList();

        List<Long> groundCodes = clubGroundRepository.findByClubId(club.getId())
                .stream()
                .map(cg -> cg.getGround().getCode())
                .collect(Collectors.toList());

        List<GroundResponseDto> groundList = groundRepository.findAllByCodeIn(groundCodes)
                .stream()
                .map(GroundResponseDto::toDto)
                .toList();

        Address address = club.getAddress();
        ClubLikeInfoDto likeInfo = clubRepository.findLikeInfoByClubIdAndMemberId(club.getId(), member.getId());

        return ClubResponseDto.toDto(club, leaderName, address, ageGroupList, groundList, likeInfo);
    }

    private List<AgeGroup> createClubAgeGroups(Club club, List<AgeGroupType> ageGroupTypes) {
        return ageGroupTypes.stream()
                .map(age -> AgeGroup.createAgeGroup(club, age))
                .toList();
    }

    private List<ClubGround> createClubGroundList(List<Long> codeList, Club club) {
        List<Ground> grounds = groundRepository.findAllById(codeList);
        return grounds.stream()
                .map(g -> ClubGround.createClubGround(club, g))
                .toList();
    }

    private void assertLeader(ClubMember clubMember) {
        if (!clubMember.isLeader()) throw new DomainException(ClubMemberErrorCode.NO_PERMISSION);
    }
}
