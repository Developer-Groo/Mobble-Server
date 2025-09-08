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
import org.junit.jupiter.api.BeforeEach;
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

    private static final Long CLUB_ID = 1L;
    private static final Long MEMBER_ID = 2L;
    private static final Long LEADER_MEMBER_ID = 2L;

    private Club mockClub;
    private Member mockMember;
    private Member mockLeaderMember;
    private ClubMember mockClubMember;
    private ClubMember mockLeaderClubMember;
    private ClubCategory mockClubCategory;

    @BeforeEach
    void setUp() {
        mockClub = mock(Club.class);
        mockMember = mock(Member.class);
        mockLeaderMember = mock(Member.class);
        mockClubMember = mock(ClubMember.class);
        mockLeaderClubMember = mock(ClubMember.class);
        mockClubCategory = mock(ClubCategory.class);
    }

    @Nested
    @DisplayName("클럽 생성")
    class CreateClub {

        @Test
        @DisplayName("성공 - 카테고리/멤버 유효, 채팅방 생성 포함")
        void success_create_club() {
            //given
            List<AgeGroupType> ageGroupList = List.of(AgeGroupType.TEEN, AgeGroupType.TWENTIES);
            ClubRequestDto dto = new ClubRequestDto("name", "SOCCER", "ground", "address", 3, ageGroupList, true);

            given(clubCategoryRepository.findByName(dto.category())).willReturn(Optional.of(mockClubCategory));
            given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(mockMember);
            given(mockMember.getId()).willReturn(MEMBER_ID);

            given(clubRepository.save(any(Club.class))).willAnswer(inv -> inv.getArgument(0));
            given(clubMemberRepository.save(any(ClubMember.class))).willAnswer(inv -> inv.getArgument(0));

            AgeGroup ag1 = mock(AgeGroup.class);
            AgeGroup ag2 = mock(AgeGroup.class);
            given(ageGroupRepository.saveAll(anyList())).willReturn(List.of(ag1, ag2));
            given(ageGroupRepository.findByClubId(any())).willReturn(List.of(ag1, ag2));

            given(clubChatRoomService.createClubChatRoom(nullable(Long.class), eq(MEMBER_ID)))
                    .willReturn(new ClubChatRoomPreviewResponseDto(123L, null, "clubName", "", null, 0, null));

            ClubLikeInfoDto likeInfo = new ClubLikeInfoDto(0, false);
            given(clubRepository.findLikeInfoByClubIdAndMemberId(any(), eq(MEMBER_ID)))
                    .willReturn(likeInfo);

            // when
            ClubResponseDto res = clubService.createClub(MEMBER_ID, dto);

            // then
            assertThat(res).isNotNull();
            verify(clubRepository).save(any(Club.class));
            verify(clubMemberRepository).save(any(ClubMember.class));
            verify(ageGroupRepository).saveAll(anyList());
            verify(ageGroupRepository).findByClubId(any());
            verify(clubChatRoomService).createClubChatRoom(nullable(Long.class), eq(MEMBER_ID));
            verify(clubRepository).findLikeInfoByClubIdAndMemberId(any(), eq(MEMBER_ID));
        }

        @Test
        @DisplayName("실패 - 잘못된 카테고리")
        void fail_when_category_not_found() {
            // given
            ClubRequestDto dto = mock(ClubRequestDto.class);
            given(dto.category()).willReturn("UNKNOWN");
            given(clubCategoryRepository.findByName("UNKNOWN")).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clubService.createClub(MEMBER_ID, dto))
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
            Member leaderMember = mock(Member.class);
            Member me = mock(Member.class);

            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(mockClub.getId()).willReturn(CLUB_ID);

            given(mockClub.getClubCategory()).willReturn(mockClubCategory);

            given(clubMemberRepository.findByClubIdAndClubMemberRole(CLUB_ID, ClubMemberRole.LEADER))
                    .willReturn(Optional.of(mockLeaderClubMember));
            given(mockLeaderClubMember.getMember()).willReturn(mockLeaderMember);

            given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(me);
            given(me.getId()).willReturn(MEMBER_ID);

            AgeGroup ag1 = mock(AgeGroup.class);
            AgeGroup ag2 = mock(AgeGroup.class);
            given(ageGroupRepository.findByClubId(CLUB_ID)).willReturn(List.of(ag1, ag2));
            given(clubRepository.findLikeInfoByClubIdAndMemberId(CLUB_ID, MEMBER_ID))
                    .willReturn(new ClubLikeInfoDto(9, false));

            // when
            ClubResponseDto res = clubService.findClubById(CLUB_ID, MEMBER_ID);

            // then
            assertThat(res).isNotNull();
            assertThat(res.id()).isEqualTo(CLUB_ID);

            verify(clubValidator).findClubByClubIdOrThrow(CLUB_ID);
            verify(clubMemberRepository).findByClubIdAndClubMemberRole(CLUB_ID, ClubMemberRole.LEADER);
            verify(memberValidator).findMemberByMemberIdOrThrow(MEMBER_ID);
            verify(ageGroupRepository).findByClubId(CLUB_ID);
            verify(clubRepository).findLikeInfoByClubIdAndMemberId(CLUB_ID, MEMBER_ID);
        }

        @Test
        @DisplayName("단건 조회 실패 - 클럽 없음")
        void fail_when_not_found() {
            // given
            DomainException ex = new DomainException(ClubErrorCode.NOT_FOUND);
            willThrow(ex).given(clubValidator).findClubByClubIdOrThrow(CLUB_ID);

            // when & then
            assertThatThrownBy(() -> clubService.findClubById(CLUB_ID, MEMBER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ClubErrorCode.NOT_FOUND.message());

            verifyNoInteractions(memberValidator);
        }
    }

    @Nested
    @DisplayName("클럽 수정")
    class UpdateClub {

        @Test
        @DisplayName("수정 성공 - 리더가 수정, 연령대 재구성")
        void success_update_by_leader() {
            // given
            List<AgeGroupType> ages = List.of(AgeGroupType.THIRTIES, AgeGroupType.FORTIES);
            ClubRequestDto dto = new ClubRequestDto("name", "SOCCER", "ground", "address", 3, ages, true);

            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(mockMember);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(mockClubMember);
            given(clubCategoryRepository.findByName(dto.category())).willReturn(Optional.of(mockClubCategory));
            given(mockClubMember.isLeader()).willReturn(true);

            given(mockClub.getId()).willReturn(CLUB_ID);
            given(mockMember.getId()).willReturn(MEMBER_ID);

            given(mockClub.getClubCategory()).willReturn(mockClubCategory);
            given(mockClubCategory.getName()).willReturn("SOCCER");
            given(mockClub.getName()).willReturn("name");
            given(mockClub.getGround()).willReturn("ground");
            given(mockClub.getAddress()).willReturn("address");
            given(mockClub.getHeadCount()).willReturn(3);
            given(mockClub.isAutoJoin()).willReturn(true);

            AgeGroup ag1 = mock(AgeGroup.class);
            AgeGroup ag2 = mock(AgeGroup.class);

            given(ageGroupRepository.findByClubId(CLUB_ID)).willReturn(List.of(ag1, ag2));
            given(ag1.getAgeGroupType()).willReturn(AgeGroupType.THIRTIES);
            given(ag2.getAgeGroupType()).willReturn(AgeGroupType.FORTIES);
            given(clubRepository.findLikeInfoByClubIdAndMemberId(CLUB_ID, MEMBER_ID))
                    .willReturn(new ClubLikeInfoDto(1, false));

            // when
            ClubResponseDto res = clubService.updateClub(CLUB_ID, MEMBER_ID, dto);

            // then
            assertThat(res).isNotNull();
            verify(ageGroupRepository).saveAll(anyList());
            verify(ageGroupRepository).deleteAllClubAgeGroupByClubId(CLUB_ID);
        }
    }

    @Test
    @DisplayName("수정 실패 - 리더가 아님")
    void fail_when_not_leader() {
        // given
        ClubRequestDto dto = mock(ClubRequestDto.class);

        given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
        given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(mockMember);
        given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(mockClubMember);
        given(mockClubMember.isLeader()).willReturn(false);

        // when & then
        assertThatThrownBy(() -> clubService.updateClub(CLUB_ID, MEMBER_ID, dto))
                .isInstanceOf(DomainException.class)
                .hasMessage(ClubMemberErrorCode.NO_PERMISSION.message());

        verify(clubRepository, never()).findLikeInfoByClubIdAndMemberId(anyLong(), anyLong());
        verify(ageGroupRepository, never()).deleteAllClubAgeGroupByClubId(anyLong());
    }

    @Nested
    @DisplayName("클럽 삭제")
    class DeleteClub {

        @Test
        @DisplayName("삭제 성공 - 리더가 삭제, 연쇄 삭제 순서 검증")
        void success_delete_by_leader() {
            // given
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(mockClubMember);

            given(mockClubMember.isLeader()).willReturn(true);
            given(mockClubMember.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(CLUB_ID);

            List<Long> articleIds = List.of(100L, 200L);
            given(articleRepository.findArticleIdsByClubId(CLUB_ID)).willReturn(articleIds);

            // when
            clubService.deleteClub(CLUB_ID, MEMBER_ID);

            // then
            InOrder inOrder = inOrder(
                    clubChatRoomService,
                    clubLikeRepository,
                    articleRepository,
                    commentLikeRepository,
                    commentRepository,
                    articleLikeRepository,
                    clubMemberRepository,
                    ageGroupRepository,
                    clubRepository
            );

            inOrder.verify(clubChatRoomService).deleteClubChatRoom(CLUB_ID);
            inOrder.verify(articleRepository).findArticleIdsByClubId(CLUB_ID);
            inOrder.verify(commentLikeRepository).deleteAllCommentLikeByComment_Article_IdIn(articleIds);
            inOrder.verify(commentRepository).deleteAllCommentByArticle_IdIn(articleIds);
            inOrder.verify(articleLikeRepository).deleteAllArticleLikeByArticle_IdIn(articleIds);
            inOrder.verify(articleRepository).deleteAllArticleByClub_Id(CLUB_ID);
            inOrder.verify(clubMemberRepository).deleteAllClubMemberByClubId(CLUB_ID);
            inOrder.verify(clubLikeRepository).deleteClubLikeAllByClub_Id(CLUB_ID);
            inOrder.verify(ageGroupRepository).deleteAllClubAgeGroupByClubId(CLUB_ID);
            inOrder.verify(clubRepository).deleteById(CLUB_ID);
        }

        @Test
        @DisplayName("삭제 실패 - 리더가 아님")
        void fail_delete_when_not_leader() {
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(mockClubMember);
            given(mockClubMember.isLeader()).willReturn(false);

            assertThatThrownBy(() -> clubService.deleteClub(CLUB_ID, MEMBER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ClubMemberErrorCode.NO_PERMISSION.message());

            verifyNoInteractions(clubChatRoomService, articleRepository, commentLikeRepository,
                    commentRepository, articleLikeRepository, clubMemberRepository, ageGroupRepository,
                    clubRepository);
        }
    }
}
