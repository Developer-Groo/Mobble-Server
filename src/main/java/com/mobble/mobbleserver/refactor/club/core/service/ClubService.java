package com.mobble.mobbleserver.refactor.club.core.service;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.infrastructure.persistence.comment.JpaCommentRepository;
import com.mobble.mobbleserver.refactor.adress.dto.request.AddressRequestDto;
import com.mobble.mobbleserver.refactor.adress.entity.Address;
import com.mobble.mobbleserver.refactor.adress.repository.AddressRepository;
import com.mobble.mobbleserver.refactor.article.repository.ArticleRepository;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.refactor.chat.clubChatRoom.service.ClubChatRoomService;
import com.mobble.mobbleserver.refactor.club.ageGroup.entity.AgeGroup;
import com.mobble.mobbleserver.refactor.club.ageGroup.entity.AgeGroupType;
import com.mobble.mobbleserver.refactor.club.ageGroup.repository.AgeGroupRepository;
import com.mobble.mobbleserver.refactor.club.clubGround.entity.ClubGround;
import com.mobble.mobbleserver.refactor.club.clubGround.repository.ClubGroundRepository;
import com.mobble.mobbleserver.refactor.club.core.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.refactor.club.core.dto.response.ClubResponseDto;
import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.club.core.repository.ClubRepository;
import com.mobble.mobbleserver.refactor.club.core.repository.dto.ClubLikeInfoDto;
import com.mobble.mobbleserver.refactor.club.core.validator.ClubValidator;
import com.mobble.mobbleserver.refactor.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.refactor.clubCategory.repository.ClubCategoryRepository;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.refactor.clubMember.entity.JoinStatus;
import com.mobble.mobbleserver.refactor.clubMember.repository.ClubMemberRepository;
import com.mobble.mobbleserver.refactor.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.refactor.ground.dto.response.GroundResponseDto;
import com.mobble.mobbleserver.refactor.ground.entity.Ground;
import com.mobble.mobbleserver.refactor.ground.repository.GroundRepository;
import com.mobble.mobbleserver.refactor.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.refactor.like.clubLike.repository.ClubLikeRepository;
import com.mobble.mobbleserver.refactor.like.commentLike.repository.CommentLikeRepository;
import com.mobble.mobbleserver.refactor.member.entity.Member;
import com.mobble.mobbleserver.refactor.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    private final JpaCommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final ClubLikeRepository clubLikeRepository;
    private final AddressRepository addressRepository;
    private final GroundRepository groundRepository;
    private final ClubGroundRepository clubGroundRepository;

    private final ClubValidator clubValidator;
    private final MemberValidator memberValidator;
    private final ClubMemberValidator clubMemberValidator;

    @Transactional
    public ClubResponseDto createClub(Long memberId, ClubRequestDto dto) {
        ClubCategory category = findCategoryOrThrow(dto.category());
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        Club club = dto.toEntity(category);
        clubRepository.save(club);

        Address address = dto.addressDto().toEntity(club);
        addressRepository.save(address);
        club.setAddress(address);

        List<Long> codeList = dto.groundCodes();
        List<ClubGround> clubGrounds = createClubGroundList(codeList, club);
        clubGroundRepository.saveAll(clubGrounds);

        ClubMember clubMember = ClubMember.createClubMember(member, club, ClubMemberRole.LEADER, JoinStatus.APPROVED);
        clubMemberRepository.save(clubMember);

        List<AgeGroup> ageGroups = createClubAgeGroups(club, dto.ageGroup());
        ageGroupRepository.saveAll(ageGroups);

        // Todo: 반환값이 Club 채팅방의 preview 에 필요한 데이터이기 때문에 반환 DTO에 포함 되어야 함
        ClubChatRoomPreviewResponseDto clubChatRoom = clubChatRoomService.createClubChatRoom(club.getId(),
                member.getId());

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

        Address address = club.getAddress();
        AddressRequestDto addrDto = dto.addressDto();

        address.updateAddress(addrDto);
        club.updateClub(category, dto.name(), address, dto.headcount(), dto.isAutoJoin());

        ageGroupRepository.deleteAllClubAgeGroupByClubId(club.getId());
        clubGroundRepository.deleteAllByClubId(club.getId());

        List<AgeGroup> newAgeGroups = createClubAgeGroups(club, dto.ageGroup());
        List<ClubGround> newClubGrounds = createClubGroundList(dto.groundCodes(), club);
        ageGroupRepository.saveAll(newAgeGroups);
        clubGroundRepository.saveAll(newClubGrounds);

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
