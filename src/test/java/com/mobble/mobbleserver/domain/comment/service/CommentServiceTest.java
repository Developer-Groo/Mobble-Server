package com.mobble.mobbleserver.domain.comment.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.validator.ArticleValidator;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentResponseDto;
import com.mobble.mobbleserver.domain.comment.dto.response.RootCommentResponseDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import com.mobble.mobbleserver.domain.comment.repository.dto.CommentLikeInfoDto;
import com.mobble.mobbleserver.domain.comment.validator.CommentValidator;
import com.mobble.mobbleserver.domain.member.entity.Member;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentValidator commentValidator;

    @Mock
    private ClubMemberValidator clubMemberValidator;

    @Mock
    private ArticleValidator articleValidator;

    @InjectMocks
    private CommentService commentService;

    @Nested
    @DisplayName("댓글 생성")
    class CreateComment {

        @Test
        @DisplayName("루트 댓글 생성 성공")
        void success_when_create_root_comment() {
            // given
            Long clubId = 1L;
            Long memberId = 2L;
            Long articleId = 3L;
            CommentRequestDto dto = new CommentRequestDto("content");

            Club mockClub = mock(Club.class);
            Member mockMember = mock(Member.class);
            Article mockArticle = mock(Article.class);
            ClubMember mockClubMember = mock(ClubMember.class);
            Comment mockRootComment = mock(Comment.class);

            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(clubId);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);
            given(mockClubMember.getMember()).willReturn(mockMember);

            given(articleValidator.findArticleByArticleIdOrThrow(articleId)).willReturn(mockArticle);

            given(commentRepository.save(any())).willReturn(mockRootComment);
            given(mockRootComment.getMember()).willReturn(mockMember);
            given(mockRootComment.getArticle()).willReturn(mockArticle);

            // when
            CommentResponseDto rootComment = commentService.createRootComment(memberId, articleId, dto);

            // then
            assertThat(rootComment).isNotNull();
            verify(commentRepository).save(any());
        }

        @Test
        @DisplayName("대댓글 생성 성공")
        void success_when_create_reply_comment() {
            // given
            Long clubId = 1L;
            Long memberId = 2L;
            Long articleId = 3L;
            Long parentId = 4L;
            CommentRequestDto dto = new CommentRequestDto("content");

            Club mockClub = mock(Club.class);
            Member mockMember = mock(Member.class);
            Article mockArticle = mock(Article.class);
            ClubMember mockClubMember = mock(ClubMember.class);
            Comment mockParentComment = mock(Comment.class);
            Comment mockReplyComment = mock(Comment.class);

            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(clubId);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);
            given(mockClubMember.getMember()).willReturn(mockMember);

            given(articleValidator.findArticleByArticleIdOrThrow(articleId)).willReturn(mockArticle);

            given(commentValidator.findCommentByCommentIdOrThrow(parentId)).willReturn(mockParentComment);

            given(commentRepository.save(any())).willReturn(mockReplyComment);
            given(mockReplyComment.getMember()).willReturn(mockMember);
            given(mockReplyComment.getArticle()).willReturn(mockArticle);

            // when
            CommentResponseDto replyComment = commentService.createReplyComment(memberId, articleId, parentId, dto);

            // then
            assertThat(replyComment).isNotNull();
            verify(commentRepository).save(any());
        }
    }

    @Nested
    @DisplayName("댓글 수정")
    class UpdateComment {

        @Test
        @DisplayName("댓글 수정 성공")
        void success_when_update_comment() {
            // given
            Long clubId = 1L;
            Long articleId = 2L;
            Long memberId = 3L;
            Long commentId = 4L;
            CommentRequestDto dto = new CommentRequestDto("update content");

            Club mockClub = mock(Club.class);
            Article mockArticle = mock(Article.class);
            ClubMember mockClubMember = mock(ClubMember.class);
            Member mockMember = mock(Member.class);
            Comment mockComment = mock(Comment.class);
            Comment mockUpdatedComment = mock(Comment.class);

            given(articleValidator.findArticleByArticleIdOrThrow(articleId)).willReturn(mockArticle);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(clubId);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);
            given(mockClubMember.getMember()).willReturn(mockMember);
            given(mockMember.getId()).willReturn(memberId);

            given(commentValidator.findCommentByCommentIdAndMemberIdOrThrow(commentId, memberId)).willReturn(mockComment);

            given(mockComment.getArticle()).willReturn(mockArticle);
            given(mockArticle.getId()).willReturn(articleId);

            given(mockComment.updateContent(dto.content())).willReturn(mockUpdatedComment);
            given(mockUpdatedComment.getMember()).willReturn(mockMember);
            given(mockUpdatedComment.getArticle()).willReturn(mockArticle);

            // when
            CommentResponseDto response = commentService.updateComment(articleId, commentId, memberId, dto);

            // then
            assertThat(response).isNotNull();
            verify(commentValidator).findCommentByCommentIdAndMemberIdOrThrow(commentId, memberId);
            verify(mockComment).updateContent(dto.content());
        }

        @Test
        @DisplayName("댓글 수정 실패 - 다른 게시글의 댓글")
        void fail_when_update_comment_article_mismatch() {
            // given
            Long clubId = 1L;
            Long articleId = 2L;
            Long otherArticleId = 3L;
            Long commentId = 4L;
            Long memberId = 5L;
            CommentRequestDto dto = new CommentRequestDto("update content");

            Club mockClub = mock(Club.class);
            Article mockRequestedArticle = mock(Article.class);
            Article mockOtherArticle = mock(Article.class);
            ClubMember mockClubMember = mock(ClubMember.class);
            Member mockMember = mock(Member.class);
            Comment mockComment = mock(Comment.class);

            given(articleValidator.findArticleByArticleIdOrThrow(articleId)).willReturn(mockRequestedArticle);
            given(mockRequestedArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(clubId);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);
            given(mockClubMember.getMember()).willReturn(mockMember);
            given(mockMember.getId()).willReturn(memberId);

            given(commentValidator.findCommentByCommentIdAndMemberIdOrThrow(commentId, memberId)).willReturn(mockComment);

            given(mockComment.getArticle()).willReturn(mockOtherArticle);
            given(mockOtherArticle.getId()).willReturn(otherArticleId);

            // when & then
            assertThatThrownBy(
                    () -> commentService.updateComment(articleId, commentId, memberId, dto)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("댓글 수정 실패 - 본인 댓글이 아님")
        void fail_when_update_comment_not_owner() {
            // given
            Long clubId = 1L;
            Long articleId = 2L;
            Long commentId = 3L;
            Long memberId = 4L;
            CommentRequestDto dto = new CommentRequestDto("update content");

            Club mockClub = mock(Club.class);
            Article mockArticle = mock(Article.class);
            ClubMember mockClubMember = mock(ClubMember.class);
            Member mockMember = mock(Member.class);

            given(articleValidator.findArticleByArticleIdOrThrow(articleId)).willReturn(mockArticle);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(clubId);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);
            given(mockClubMember.getMember()).willReturn(mockMember);
            given(mockMember.getId()).willReturn(memberId);

            given(commentValidator.findCommentByCommentIdAndMemberIdOrThrow(commentId, memberId))
                    .willThrow(new IllegalArgumentException("No permission to update this comment"));

            // when & then
            assertThatThrownBy(
                    () -> commentService.updateComment(articleId, commentId, memberId, dto)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("댓글 삭제")
    class DeleteComment {

        @Test
        @DisplayName("댓글 삭제 성공 - 일반 멤버가 자신의 댓글 삭제")
        void success_when_delete_comment() {
            // given
            Long memberId = 1L;
            Long commentId = 2L;
            Long clubId = 3L;
            Long articleId = 4L;

            Comment mockComment = mock(Comment.class);
            Club mockClub = mock(Club.class);
            Article mockArticle = mock(Article.class);
            ClubMember mockClubMember = mock(ClubMember.class);

            given(articleValidator.findArticleByArticleIdOrThrow(articleId)).willReturn(mockArticle);
            given(commentValidator.findCommentByCommentIdAndMemberIdOrThrow(commentId, memberId)).willReturn(mockComment);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(clubId);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);

            given(commentValidator.findCommentByCommentIdAndMemberIdOrThrow(commentId, memberId)).willReturn(mockComment);

            given(mockComment.getArticle()).willReturn(mockArticle);
            given(mockArticle.getId()).willReturn(articleId);

            // when
            commentService.deleteComment(articleId, commentId, memberId);

            // then
            verify(commentRepository).delete(mockComment);
        }
    }

    @Nested
    @DisplayName("댓글 목록 조회")
    class GetCommentList {

        @Test
        @DisplayName("특정 게시글의 댓글 리스트(좋아요 정보 포함) 조회 성공")
        void success_when_get_comment_list_by_article() {
            // given
            Long articleId = 1L;
            Long memberId = 2L;

            Article mockArticle = mock(Article.class);
            Member mockMember = mock(Member.class);
            Comment mockComment = mock(Comment.class);

            given(mockArticle.getId()).willReturn(articleId);
            given(mockMember.getId()).willReturn(memberId);
            given(mockComment.getId()).willReturn(100L);
            given(articleValidator.findArticleByArticleIdOrThrow(articleId)).willReturn(mockArticle);

            given(commentRepository.findCommentsWithRepliesByArticleId(articleId)).willReturn(List.of(mockComment));
            given(mockComment.getMember()).willReturn(mockMember);
            given(mockComment.getArticle()).willReturn(mockArticle);

            CommentLikeInfoDto likeInfoDto = CommentLikeInfoDto.toDto(2, true);
            given(commentRepository.findLikeInfoByCommentIdsAndMemberId(any(), eq(memberId))).willReturn(Map.of(100L, likeInfoDto));

            // when
            List<RootCommentResponseDto> response = commentService.getCommentListByArticle(articleId, memberId);

            // then
            assertThat(response).hasSize(1);

            RootCommentResponseDto dto = response.get(0);
            assertThat(dto.likeCount()).isEqualTo(2);
            assertThat(dto.isLiked()).isTrue();

            verify(commentRepository).findCommentsWithRepliesByArticleId(articleId);
            verify(commentRepository).findLikeInfoByCommentIdsAndMemberId(any(), eq(memberId));
        }
    }
}
