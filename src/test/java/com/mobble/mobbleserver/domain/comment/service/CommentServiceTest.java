package com.mobble.mobbleserver.domain.comment.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.club.entity.Club;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentListResponseDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentResponseDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    // Todo: Article -> Validator 로 전환 필요

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentValidator commentValidator;

    @Mock
    private ClubMemberValidator clubMemberValidator;

    @Mock
    private ArticleRepository articleRepository;

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

            given(articleRepository.findById(articleId)).willReturn(Optional.of(mockArticle));

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

            given(articleRepository.findById(articleId)).willReturn(Optional.of(mockArticle));

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
    @DisplayName("댓글 목록 조회")
    class GetCommentList {

        @Test
        @DisplayName("특정 게시글의 댓글 리스트 조회 성공")
        void success_when_get_comment_list_by_article() {
            // given
            Long articleId = 1L;

            Article mockArticle = mock(Article.class);
            Member mockMember = mock(Member.class);
            Comment mockComment = mock(Comment.class);

            given(mockArticle.getId()).willReturn(articleId);
            given(articleRepository.findById(articleId)).willReturn(Optional.of(mockArticle));

            given(commentRepository.findCommentsWithRepliesByArticleId(articleId)).willReturn(List.of(mockComment));
            given(mockComment.getMember()).willReturn(mockMember);
            given(mockComment.getArticle()).willReturn(mockArticle);

            // when
            List<CommentListResponseDto> response = commentService.getCommentListByArticle(articleId);

            // then
            assertThat(response).isNotNull();
            verify(commentRepository).findCommentsWithRepliesByArticleId(articleId);
        }
    }

    @Nested
    @DisplayName("댓글 수정")
    class UpdateComment {

        @Test
        @DisplayName("댓글 수정 성공")
        void success_when_update_comment() {
            // given
            Long memberId = 1L;
            Long commentId = 2L;
            CommentRequestDto dto = new CommentRequestDto("update content");

            Member mockMember = mock(Member.class);
            Article mockArticle = mock(Article.class);
            Comment mockComment = mock(Comment.class);
            Comment mockUpdatedComment = mock(Comment.class);

            given(commentValidator.findCommentByCommentIdAndMemberIdOrThrow(commentId, memberId)).willReturn(mockComment);
            given(mockComment.updateContent(dto.content())).willReturn(mockUpdatedComment);
            given(mockUpdatedComment.getMember()).willReturn(mockMember);
            given(mockUpdatedComment.getArticle()).willReturn(mockArticle);

            // when
            CommentResponseDto response = commentService.updateComment(commentId, memberId, dto);

            // then
            assertThat(response).isNotNull();
            verify(commentValidator).findCommentByCommentIdAndMemberIdOrThrow(commentId, memberId);
            verify(mockComment).updateContent(dto.content());
        }
    }

    @Nested
    @DisplayName("댓글 삭제")
    class DeleteComment {

        @Test
        @DisplayName("댓글 삭제 성공")
        void success_when_delete_comment() {
            // given
            Long memberId = 1L;
            Long commentId = 2L;

            Comment mockComment = mock(Comment.class);

            given(commentValidator.findCommentByCommentIdAndMemberIdOrThrow(commentId, memberId)).willReturn(mockComment);

            // when
            commentService.deleteComment(commentId, memberId);

            // then
            verify(commentValidator).findCommentByCommentIdAndMemberIdOrThrow(commentId, memberId);
            verify(commentRepository).delete(mockComment);
        }
    }
}