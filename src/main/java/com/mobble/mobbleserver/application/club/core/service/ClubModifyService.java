package com.mobble.mobbleserver.application.club.core.service;

import com.mobble.mobbleserver.application.address.port.required.AddressWritePort;
import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.chat.room.port.provided.club.ClubChatRoomCreatePort;
import com.mobble.mobbleserver.application.chat.room.port.provided.common.ChatRoomExitPort;
import com.mobble.mobbleserver.application.club.ageGroup.port.required.AgeGroupReadPort;
import com.mobble.mobbleserver.application.club.ageGroup.port.required.AgeGroupWritePort;
import com.mobble.mobbleserver.application.clbuCategory.port.ClubCategoryReadPort;
import com.mobble.mobbleserver.application.club.core.port.provided.ClubCreatePort;
import com.mobble.mobbleserver.application.club.core.port.provided.ClubDeletePort;
import com.mobble.mobbleserver.application.club.core.port.provided.ClubUpdatePort;
import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.club.core.port.required.ClubWritePort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberWritePort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.ground.required.GroundReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.address.Address;
import com.mobble.mobbleserver.domain.club.ageGroup.AgeGroup;
import com.mobble.mobbleserver.domain.club.ageGroup.AgeGroupType;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import com.mobble.mobbleserver.domain.ground.Ground;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.infrastructure.persistence.club.core.projection.ClubLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.web.address.dto.request.AddressRequestDto;
import com.mobble.mobbleserver.infrastructure.web.chat.room.club.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.response.ClubResponseDto;
import com.mobble.mobbleserver.infrastructure.web.ground.dto.response.GroundResponseDto;
import com.mobble.mobbleserver.refactor.club.clubGround.entity.ClubGround;
import com.mobble.mobbleserver.refactor.club.clubGround.repository.ClubGroundRepository;
import com.mobble.mobbleserver.domain.ClubCategory.ClubCategory;
import com.mobble.mobbleserver.refactor.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.refactor.like.clubLike.repository.ClubLikeRepository;
import com.mobble.mobbleserver.refactor.like.commentLike.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubModifyService implements ClubCreatePort, ClubUpdatePort, ClubDeletePort {

    private final ClubWritePort clubWritePort;
    private final ClubMemberWritePort clubMemberWritePort;
    private final AddressWritePort addressWritePort;

    private final ClubReadPort clubReadPort;
    private final MemberReadPort memberReadPort;
    private final ClubMemberReadPort clubMemberReadPort;
    private final ArticleReadPort articleReadPort;
    private final CommentReadPort commentReadPort;
    private final ClubCategoryReadPort clubCategoryReadPort;
    private final GroundReadPort groundReadPort;
    private final AgeGroupReadPort ageGroupReadPort;
    private final AgeGroupWritePort ageGroupWritePort;

    private final ClubChatRoomCreatePort clubChatRoomCreatePort;
    private final ChatRoomExitPort chatRoomExitPort;

    private final ClubGroundRepository clubGroundRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final ClubLikeRepository clubLikeRepository;

    @Override
    public ClubResponseDto createClub(Long memberId, ClubRequestDto dto) {
        ClubCategory category = findCategoryOrThrow(dto.category());
        Member member = findMemberByMemberIdOrThrow(memberId);

        Club club = dto.toEntity(category);
        clubWritePort.save(club);

        Address address = dto.addressDto().toEntity(club);
        addressWritePort.save(address);
        club.setAddress(address);

        List<Long> codeList = dto.groundCodes();
        List<ClubGround> clubGrounds = createClubGroundList(codeList, club);
        clubGroundRepository.saveAll(clubGrounds);

        ClubMember clubMember = ClubMember.createClubMember(member, club, ClubMemberRole.LEADER, JoinStatus.APPROVED);
        clubMemberWritePort.save(clubMember);

        List<AgeGroup> ageGroups = createClubAgeGroups(club, dto.ageGroup());
        ageGroupWritePort.saveAll(ageGroups);

        // Todo: 반환값이 Club 채팅방의 preview 에 필요한 데이터이기 때문에 반환 DTO에 포함 되어야 함
        ClubChatRoomPreviewResponseDto clubChatRoom = clubChatRoomCreatePort.createClubChatRoom(club.getId(), member.getId());

        return buildClubResponse(club, member, member.getName());
    }

    @Override
    public ClubResponseDto updateClub(Long clubId, Long memberId, ClubRequestDto dto) {
        Club club = findClubByClubIdOrThrow(clubId);
        Member member = findMemberByMemberIdOrThrow(memberId);
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        assertLeader(clubMember);

        ClubCategory category = findCategoryOrThrow(dto.category());

        Address address = club.getAddress();
        AddressRequestDto addrDto = dto.addressDto();

        address.updateAddress(addrDto);
        club.updateClub(category, dto.name(), address, dto.headcount(), dto.isAutoJoin());

        ageGroupWritePort.deleteAllClubAgeGroupByClubId(club.getId());
        clubGroundRepository.deleteAllByClubId(club.getId());

        List<AgeGroup> newAgeGroups = createClubAgeGroups(club, dto.ageGroup());
        List<ClubGround> newClubGrounds = createClubGroundList(dto.groundCodes(), club);
        ageGroupWritePort.saveAll(newAgeGroups);
        clubGroundRepository.saveAll(newClubGrounds);

        return buildClubResponse(club, member, member.getName());
    }

    @Override
    public void deleteClub(Long clubId, Long memberId) {
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Club club = clubMember.getClub();

        assertLeader(clubMember);

        chatRoomExitPort.delete(club.getId());

        List<Long> articleIds = articleReadPort.findArticleIdsByClubId(club.getId());

        commentLikeRepository.deleteAllCommentLikeByComment_Article_IdIn(articleIds);
        commentReadPort.deleteAllCommentByArticle_IdIn(articleIds);
        articleLikeRepository.deleteAllArticleLikeByArticle_IdIn(articleIds);
        articleReadPort.deleteAllArticleByClub_Id(club.getId());
        clubMemberWritePort.deleteAllClubMemberByClubId(club.getId());

        clubLikeRepository.deleteClubLikeAllByClub_Id(club.getId());
        ageGroupWritePort.deleteAllClubAgeGroupByClubId(club.getId());

        clubWritePort.delete(club);
    }

    private Club findClubByClubIdOrThrow(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new DomainException((ClubErrorCode.NOT_FOUND)));
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    private ClubMember findClubMemberByClubIdAndMemberIdOrThrow(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB));
    }

    private void assertLeader(ClubMember clubMember) {
        if (!clubMember.isLeader()) throw new DomainException(ClubMemberErrorCode.NO_PERMISSION);
    }

    private ClubCategory findCategoryOrThrow(String categoryName) {
        return clubCategoryReadPort.findByName(categoryName)
                .orElseThrow(() -> new DomainException(ClubErrorCode.CATEGORY_NOT_FOUND));
    }

    private List<AgeGroup> createClubAgeGroups(Club club, List<AgeGroupType> ageGroupTypes) {
        return ageGroupTypes.stream()
                .map(age -> AgeGroup.createAgeGroup(club, age))
                .toList();
    }

    private List<ClubGround> createClubGroundList(List<Long> codeList, Club club) {
        List<Ground> grounds = groundReadPort.findAllById(codeList);
        return grounds.stream()
                .map(g -> ClubGround.createClubGround(club, g))
                .toList();
    }

    private ClubResponseDto buildClubResponse(Club club, Member member, String leaderName) {
        List<AgeGroupType> ageGroupList = ageGroupReadPort.findByClubId(club.getId()).stream()
                .map(AgeGroup::getAgeGroupType)
                .toList();

        List<Long> groundCodes = clubGroundRepository.findByClubId(club.getId())
                .stream()
                .map(cg -> cg.getGround().getCode())
                .collect(Collectors.toList());

        List<GroundResponseDto> groundList = groundReadPort.findAllById(groundCodes)
                .stream()
                .map(GroundResponseDto::toDto)
                .toList();

        Address address = club.getAddress();
        ClubLikeInfoDto likeInfo = clubReadPort.findLikeInfoByClubIdAndMemberId(club.getId(), member.getId());

        return ClubResponseDto.toDto(club, leaderName, address, ageGroupList, groundList, likeInfo);
    }
}
