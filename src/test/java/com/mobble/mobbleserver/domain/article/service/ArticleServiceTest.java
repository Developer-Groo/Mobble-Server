package com.mobble.mobbleserver.domain.article.service;

import com.mobble.mobbleserver.domain.article.dto.request.ArticleRequestDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleResponseDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleSummaryResponseDto;
import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.article.repository.dto.ArticleLikeInfoDto;
import com.mobble.mobbleserver.domain.article.validator.ArticleValidator;
import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.club.club.validator.ClubValidator;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.comment.dto.response.RootCommentResponseDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import com.mobble.mobbleserver.domain.comment.service.CommentService;
import com.mobble.mobbleserver.domain.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.like.commentLike.repository.CommentLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private CommentService commentService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private ArticleLikeRepository articleLikeRepository;

    @Mock
    private ArticleValidator articleValidator;

    @Mock
    private ClubValidator clubValidator;

    @Mock
    private ClubMemberValidator clubMemberValidator;

    @Mock
    private MemberValidator memberValidator;

    @InjectMocks
    private ArticleService articleService;

    @Nested
    @DisplayName("게시글 생성")
    class CreateArticle {

        @Test
        @DisplayName("게시글 생성 성공")
        void success_when_create_article() {
            // given
            Long memberId = 1L;
            Long clubId = 2L;
            ArticleRequestDto reqDto = new ArticleRequestDto("title", ArticleType.FREE, "content");

            Member mockMember = mock(Member.class);
            given(mockMember.getId()).willReturn(memberId);
            given(mockMember.getName()).willReturn("작성자");

            Club mockClub = mock(Club.class);
            given(mockClub.getId()).willReturn(clubId);

            ClubMember mockClubMember = mock(ClubMember.class);
            given(mockClubMember.canPost(ArticleType.FREE)).willReturn(true);

            given(memberValidator.findMemberByMemberIdOrThrow(memberId)).willReturn(mockMember);
            given(clubValidator.findClubByClubIdOrThrow(clubId)).willReturn(mockClub);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);

            given(articleRepository.save(any()))
                    .willAnswer(inv -> inv.getArgument(0));

            // when
            ArticleResponseDto articleResponseDto = articleService.createArticle(memberId, clubId, reqDto);

            // then
            assertThat(articleResponseDto).isNotNull();
            verify(articleRepository).save(any());
        }

        @Test
        @DisplayName("공지글 생성 실패 - MEMBER 권한")
        void fail_when_member_creates_notice() {
            // given
            Long memberId = 1L;
            Long clubId = 2L;
            ArticleRequestDto reqDto = new ArticleRequestDto("title", ArticleType.NOTICE, "content");

            Member mockMember = mock(Member.class);
            Club mockClub = mock(Club.class);
            ClubMember mockClubMember = mock(ClubMember.class);

            given(memberValidator.findMemberByMemberIdOrThrow(memberId)).willReturn(mockMember);
            given(clubValidator.findClubByClubIdOrThrow(clubId)).willReturn(mockClub);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);

            given(mockClubMember.canPost(ArticleType.NOTICE)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> articleService.createArticle(memberId, clubId, reqDto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.NOTICE_NO_PERMISSION.message());
        }
    }

    @Nested
    @DisplayName("게시글 조회")
    class GetArticle {

        @Test
        @DisplayName("해당 클럽의 게시글 목록 조회")
        void success_when_find_by_clubId() {
            // given
            Long clubId = 1L;
            Long memberId = 2L;
            ArticleType articleType = ArticleType.FREE;

            Club mockClub = mock(Club.class);
            Article mockArticle = mock(Article.class);
            Member mockMember = mock(Member.class);

            given(clubValidator.findClubByClubIdOrThrow(clubId)).willReturn(mockClub);

            given(articleRepository.findArticlesByClubId(clubId, articleType))
                    .willReturn(List.of(mockArticle));
            given(mockArticle.getId()).willReturn(10L);

            given(mockArticle.getMember()).willReturn(mockMember);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(clubId);

            given(articleRepository.findLikeInfoByArticleIdsAndMemberId(anyList(), eq(memberId)))
                    .willReturn(Map.of(10L, new ArticleLikeInfoDto(5, true)));
            given(commentRepository.countCommentsByArticleIds(anyList()))
                    .willReturn(Map.of(10L, 3));

            // when
            List<ArticleSummaryResponseDto> response =
                    articleService.findArticlesByClubId(clubId, articleType, memberId);

            // then
            assertThat(response).hasSize(1);
            ArticleSummaryResponseDto dto = response.get(0);
            assertThat(dto.likeCount()).isEqualTo(5);
            assertThat(dto.isLiked()).isTrue();
            assertThat(dto.commentCount()).isEqualTo(3);

            verify(articleRepository).findArticlesByClubId(clubId, articleType);
            verify(articleRepository).findLikeInfoByArticleIdsAndMemberId(anyList(), eq(memberId));
            verify(commentRepository).countCommentsByArticleIds(anyList());
        }

        @Test
        @DisplayName("게시글 단건 조회 성공")
        void success_when_find_by_articleId() {
            // given
            Long articleId = 1L;
            Long memberId = 2L;

            Article mockArticle = mock(Article.class);
            Club mockClub = mock(Club.class);
            Member mockMember = mock(Member.class);

            given(articleValidator.findArticleByArticleIdOrThrow(articleId))
                    .willReturn(mockArticle);
            given(mockArticle.getId()).willReturn(articleId);

            given(articleRepository.existsArticleByIdAndMemberId(articleId, memberId))
                    .willReturn(true);

            Map<Long, ArticleLikeInfoDto> likeInfoMap =
                    Map.of(articleId, new ArticleLikeInfoDto(3, true));
            given(articleRepository.findLikeInfoByArticleIdsAndMemberId(List.of(articleId), memberId))
                    .willReturn(likeInfoMap);

            RootCommentResponseDto mockComment = mock(RootCommentResponseDto.class);
            given(commentService.getCommentListByArticle(articleId, memberId))
                    .willReturn(List.of(mockComment, mockComment));

            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(100L);
            given(mockArticle.getMember()).willReturn(mockMember);
            given(mockMember.getId()).willReturn(memberId);
            given(mockArticle.getTitle()).willReturn("title");
            given(mockArticle.getContent()).willReturn("content");

            // when
            ArticleResponseDto response = articleService.findArticleById(articleId, memberId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.likeCount()).isEqualTo(3);
            assertThat(response.isLiked()).isTrue();
            assertThat(response.commentCount()).isEqualTo(2);
            assertThat(response.isMine()).isTrue();

            verify(articleValidator).findArticleByArticleIdOrThrow(articleId);
            verify(articleRepository).findLikeInfoByArticleIdsAndMemberId(List.of(articleId), memberId);
            verify(commentService).getCommentListByArticle(articleId, memberId);
        }

        @Test
        @DisplayName("해당 클럽의 게시글 목록 조회 실패 - 클럽 없음")
        void fail_when_find_by_clubId_not_found_club() {
            //given
            Long clubId = 1L;
            Long memberId = 2L;
            ArticleType articleType = ArticleType.FREE;

            DomainException ex = new DomainException(ClubErrorCode.NOT_FOUND);
            willThrow(ex).given(clubValidator).findClubByClubIdOrThrow(clubId);

            // when & then
            assertThatThrownBy(() -> articleService.findArticlesByClubId(clubId, articleType, memberId))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ClubErrorCode.NOT_FOUND.message());

            verify(articleRepository, never()).findArticlesByClubId(anyLong(), any());
        }

        @Test
        @DisplayName("게시글 단건 조회 실패 - 게시글 없음")
        void fail_when_find_by_articleId_not_found() {
            // given
            Long articleId = 1L;
            Long memberId = 2L;

            willThrow(new DomainException(ArticleErrorCode.NOT_FOUND))
                    .given(articleValidator).findArticleByArticleIdOrThrow(articleId);

            // when & then
            assertThatThrownBy(() -> articleService.findArticleById(articleId, memberId))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.NOT_FOUND.message());

            verify(articleRepository, never()).findLikeInfoByArticleIdsAndMemberId(anyList(), anyLong());
            verify(commentService, never()).getCommentListByArticle(anyLong(), anyLong());
        }
    }

    @Nested
    @DisplayName("게시글 수정")
    class UpdateArticle {

        @Test
        @DisplayName("게시글 수정")
        void success_when_update_article() {
            //given
            Long memberId = 1L;
            Long articleId = 2L;
            Long clubId = 3L;

            Article mockArticle = mock(Article.class);
            Member mockMember = mock(Member.class);
            Club mockClub = mock(Club.class);
            ClubMember mockClubMember = mock(ClubMember.class);
            ArticleRequestDto reqDto = new ArticleRequestDto("newTitle", ArticleType.FREE, "newContent");

            given(articleValidator.findArticleByArticleIdAndMemberIdOrThrow(articleId, memberId)).willReturn(mockArticle);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockArticle.getMember()).willReturn(mockMember);
            given(mockClub.getId()).willReturn(clubId);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);
            given(mockClubMember.canPost(ArticleType.FREE)).willReturn(true);
            given(mockMember.getId()).willReturn(memberId);

            given(mockArticle.getId()).willReturn(articleId);
            given(mockArticle.getTitle()).willReturn("newTitle");
            given(mockArticle.getContent()).willReturn("newContent");
            given(mockArticle.getArticleType()).willReturn(ArticleType.FREE);

            given(articleRepository.findLikeInfoByArticleIdsAndMemberId(List.of(articleId), memberId))
                    .willReturn(Map.of(articleId, new ArticleLikeInfoDto(7, true)));
            given(commentService.getCommentListByArticle(articleId, memberId)).willReturn(List.of());

            given(articleRepository.existsArticleByIdAndMemberId(articleId, memberId)).willReturn(true);

            // when
            ArticleResponseDto response = articleService.updateArticle(articleId, memberId, reqDto);

            // then
            assertThat(response).isNotNull();
            assertThat(response.likeCount()).isEqualTo(7);
            assertThat(response.isLiked()).isTrue();
            assertThat(response.commentCount()).isZero();
            assertThat(response.isMine()).isTrue();

            verify(articleValidator).findArticleByArticleIdAndMemberIdOrThrow(articleId, memberId);
            verify(clubMemberValidator).findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
            verify(mockClubMember).canPost(ArticleType.FREE);
            verify(mockArticle).updateArticle(ArticleType.FREE, "newTitle", "newContent");
            verify(articleRepository).findLikeInfoByArticleIdsAndMemberId(List.of(articleId), memberId);
            verify(commentService).getCommentListByArticle(articleId, memberId);
        }

        @Test
        @DisplayName("수정 실패 - 타인이 수정")
        void fail_when_not_owner() {
            // given
            Long memberId = 10L;
            Long articleId = 20L;

            willThrow(new DomainException(ArticleErrorCode.NOT_FOUND_TO_MEMBER))
                    .given(articleValidator)
                    .findArticleByArticleIdAndMemberIdOrThrow(articleId, memberId);

            // when
            ArticleRequestDto reqDto = new ArticleRequestDto("t", ArticleType.FREE, "c");

            // then
            assertThatThrownBy(() -> articleService.updateArticle(articleId, memberId, reqDto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.NOT_FOUND_TO_MEMBER.message());

            verifyNoInteractions(clubMemberValidator, commentService);
            verify(articleRepository, never()).findLikeInfoByArticleIdsAndMemberId(anyList(), anyLong());
        }


        @Test
        @DisplayName("수정 실패 - 작성자 본인이 NOTICE로 수정하려 하나 권한 없음")
        void fail_when_owner_updates_to_notice_without_permission() {
            // given
            Long memberId = 1L;
            Long articleId = 2L;
            Long clubId = 3L;

            Article mockArticle = mock(Article.class);
            Club mockClub = mock(Club.class);
            ClubMember mockClubMember = mock(ClubMember.class);

            ArticleRequestDto reqDto = new ArticleRequestDto("newTitle", ArticleType.NOTICE, "newContent");

            given(articleValidator.findArticleByArticleIdAndMemberIdOrThrow(articleId, memberId))
                    .willReturn(mockArticle);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(clubId);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId))
                    .willReturn(mockClubMember);

            given(mockClubMember.canPost(ArticleType.NOTICE)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> articleService.updateArticle(articleId, memberId, reqDto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.NOTICE_NO_PERMISSION.message());

            verify(mockArticle, never()).updateArticle(any(), anyString(), anyString());
        }

    }

    @Nested
    @DisplayName("게시글 삭제")
    class DeleteArticle {

        @Test
        @DisplayName("게시글 삭제 성공 - 작성자 본인")
        void success_when_delete_article_by_author() {
            // given
            Long memberId = 1L;
            Long articleId = 2L;
            Long clubId = 3L;

            Article mockArticle = mock(Article.class);
            Club mockClub = mock(Club.class);
            ClubMember mockClubMember = mock(ClubMember.class);

            given(articleValidator.findArticleByArticleIdOrThrow(articleId)).willReturn(mockArticle);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(clubId);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);

            given(articleRepository.existsArticleByIdAndMemberId(articleId, memberId))
                    .willReturn(true);

            Comment c1 = mock(Comment.class);
            Comment c2 = mock(Comment.class);
            List<Comment> comments = List.of(c1, c2);
            given(commentRepository.findCommentsWithRepliesByArticleId(articleId)).willReturn(comments);

            // when
            articleService.deleteArticle(articleId, memberId);

            // then
            verify(articleValidator).findArticleByArticleIdOrThrow(articleId);
            verify(clubMemberValidator).findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
            verify(commentRepository).findCommentsWithRepliesByArticleId(articleId);

            verify(commentLikeRepository).deleteAllByArticleId(articleId);
            verify(commentRepository).deleteAll(comments);
            verify(articleLikeRepository).deleteAllByArticleId(articleId);
            verify(articleRepository).delete(mockArticle);
        }

        @Test
        @DisplayName("게시글 삭제 성공 - 리더가 타인 글 삭제")
        void success_when_leader_deletes_others_article() {
            // given
            Long memberId = 10L;
            Long articleId = 20L;
            Long clubId = 30L;

            Article mockArticle = mock(Article.class);
            Club mockClub = mock(Club.class);
            ClubMember mockClubMember = mock(ClubMember.class);

            given(articleValidator.findArticleByArticleIdOrThrow(articleId)).willReturn(mockArticle);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(clubId);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);

            given(articleRepository.existsArticleByIdAndMemberId(articleId, memberId))
                    .willReturn(false);
            given(mockClubMember.getClubMemberRole()).willReturn(ClubMemberRole.LEADER);

            List<Comment> comments = List.of();
            given(commentRepository.findCommentsWithRepliesByArticleId(articleId)).willReturn(comments);

            // then
            articleService.deleteArticle(articleId, memberId);

            // then
            verify(commentLikeRepository).deleteAllByArticleId(articleId);
            verify(commentRepository).deleteAll(comments);
            verify(articleLikeRepository).deleteAllByArticleId(articleId);
            verify(articleRepository).delete(mockArticle);
        }

        @Test
        @DisplayName("게시글 삭제 실패 - 멤버가 타인 글 삭제 시 권한 없음")
        void fail_when_member_deletes_others_article() {
            // given
            Long memberId = 100L;
            Long articleId = 200L;
            Long clubId = 300L;

            Article mockArticle = mock(Article.class);
            Club mockClub = mock(Club.class);
            ClubMember mockClubMember = mock(ClubMember.class);

            given(articleValidator.findArticleByArticleIdOrThrow(articleId)).willReturn(mockArticle);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(clubId);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);

            given(articleRepository.existsArticleByIdAndMemberId(articleId, memberId))
                    .willReturn(false);
            given(mockClubMember.getClubMemberRole()).willReturn(ClubMemberRole.MEMBER);

            // when & then
            assertThatThrownBy(() -> articleService.deleteArticle(articleId, memberId))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.NO_PERMISSION.message());

            verify(commentRepository, never()).findCommentsWithRepliesByArticleId(anyLong());
            verify(commentLikeRepository, never()).deleteAllByArticleId(anyLong());
            verify(commentRepository, never()).deleteAll(anyList());
            verify(articleLikeRepository, never()).deleteAllByArticleId(anyLong());
            verify(articleRepository, never()).delete(any());
        }
    }
}
