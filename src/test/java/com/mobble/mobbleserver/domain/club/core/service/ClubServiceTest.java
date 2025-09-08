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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubServiceTest {

    @Mock
    private ClubChatRoomService clubChatRoomService;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private ClubCategoryRepository clubCategoryRepository;

    @Mock
    private ClubMemberRepository clubMemberRepository;

    @Mock
    private AgeGroupRepository ageGroupRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private ArticleLikeRepository articleLikeRepository;

    @Mock
    private ClubLikeRepository clubLikeRepository;

    @Mock
    private ClubValidator clubValidator;

    @Mock
    private MemberValidator memberValidator;

    @Mock
    private ClubMemberValidator clubMemberValidator;

    @InjectMocks
    private ClubService clubService;

    @Nested
    @DisplayName("클럽 생성")
    class CreateClub {

        @Test
        @DisplayName("성공 - 카테고리/멤버 유효, 채팅방 생성 포함")
        void success_create_club() {
            //given
            Long memberId = 2L;

            Member mockMember = mock(Member.class);
            ClubCategory mockClubCategory = mock(ClubCategory.class);

            List<AgeGroupType> ageGroupList = List.of(AgeGroupType.TEEN, AgeGroupType.TWENTIES);
            ClubRequestDto dto = new ClubRequestDto("name", "SOCCER", "ground", "address", 3, ageGroupList, true);

            given(clubCategoryRepository.findByName(dto.category())).willReturn(Optional.of(mockClubCategory));
            given(memberValidator.findMemberByMemberIdOrThrow(memberId)).willReturn(mockMember);
            given(mockMember.getId()).willReturn(memberId);

            given(clubRepository.save(any(Club.class))).willAnswer(inv -> inv.getArgument(0));
            given(clubMemberRepository.save(any(ClubMember.class))).willAnswer(inv -> inv.getArgument(0));

            AgeGroup ag1 = mock(AgeGroup.class);
            AgeGroup ag2 = mock(AgeGroup.class);
            given(ageGroupRepository.saveAll(anyList())).willReturn(List.of(ag1, ag2));
            given(ageGroupRepository.findByClubId(any())).willReturn(List.of(ag1, ag2));

            given(clubChatRoomService.createClubChatRoom(nullable(Long.class), eq(memberId)))
                    .willReturn(new ClubChatRoomPreviewResponseDto(123L, null, "clubName", "", null, 0, null));

            ClubLikeInfoDto likeInfo = new ClubLikeInfoDto(0, false);
            given(clubRepository.findLikeInfoByClubIdAndMemberId(any(), eq(memberId)))
                    .willReturn(likeInfo);

            // when
            ClubResponseDto res = clubService.createClub(memberId, dto);

            // then
            assertThat(res).isNotNull();
            verify(clubRepository).save(any(Club.class));
            verify(clubMemberRepository).save(any(ClubMember.class));
            verify(ageGroupRepository).saveAll(anyList());
            verify(ageGroupRepository).findByClubId(any());
            verify(clubChatRoomService).createClubChatRoom(nullable(Long.class), eq(memberId));
            verify(clubRepository).findLikeInfoByClubIdAndMemberId(any(), eq(memberId));
        }

        @Test
        @DisplayName("실패 - 잘못된 카테고리")
        void fail_when_category_not_found() {
            // given
            Long memberId = 1L;
            ClubRequestDto dto = mock(ClubRequestDto.class);
            given(dto.category()).willReturn("UNKNOWN");
            given(clubCategoryRepository.findByName("UNKNOWN")).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clubService.createClub(memberId, dto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ClubErrorCode.CATEGORY_NOT_FOUND.message());
        }
    }

    @Nested
    @DisplayName("클럽 조회")
    class GetClub {

        @Test
        @DisplayName("CLUB_ID로 조회")
        void success_find_by_id() {
            // given
            Long clubId = 100L;
            Long memberId = 200L;

            Club club = mock(Club.class);
            ClubCategory category = mock(ClubCategory.class);
            ClubMember leader = mock(ClubMember.class);
            Member leaderMember = mock(Member.class);
            Member me = mock(Member.class);

            given(clubValidator.findClubByClubIdOrThrow(clubId)).willReturn(club);
            given(club.getId()).willReturn(clubId);

            given(club.getClubCategory()).willReturn(category);

            given(clubMemberRepository.findByClubIdAndClubMemberRole(clubId, ClubMemberRole.LEADER))
                    .willReturn(Optional.of(leader));
            given(leader.getMember()).willReturn(leaderMember);

            given(memberValidator.findMemberByMemberIdOrThrow(memberId)).willReturn(me);
            given(me.getId()).willReturn(memberId);

            AgeGroup ag1 = mock(AgeGroup.class);
            AgeGroup ag2 = mock(AgeGroup.class);
            given(ageGroupRepository.findByClubId(clubId)).willReturn(List.of(ag1, ag2));
            given(clubRepository.findLikeInfoByClubIdAndMemberId(clubId, memberId))
                    .willReturn(new ClubLikeInfoDto(9, false));

            // when
            ClubResponseDto res = clubService.findClubById(clubId, memberId);

            // then
            assertThat(res).isNotNull();
            assertThat(res.id()).isEqualTo(clubId);

            verify(clubValidator).findClubByClubIdOrThrow(clubId);
            verify(clubMemberRepository).findByClubIdAndClubMemberRole(clubId, ClubMemberRole.LEADER);
            verify(memberValidator).findMemberByMemberIdOrThrow(memberId);
            verify(ageGroupRepository).findByClubId(clubId);
            verify(clubRepository).findLikeInfoByClubIdAndMemberId(clubId, memberId);
        }
