package com.mobble.mobbleserver.domain.comment.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.validator.ArticleValidator;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentResponseDto;
import com.mobble.mobbleserver.domain.comment.dto.response.RootCommentResponseDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import com.mobble.mobbleserver.domain.comment.repository.dto.CommentLikeInfoDto;
import com.mobble.mobbleserver.domain.comment.validator.CommentValidator;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.comment.CommentErrorCode;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

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

    private static final Long CLUB_ID = 1L;
    private static final Long MEMBER_ID = 2L;
    private static final Long ARTICLE_ID = 3L;
    private static final Long COMMENT_ID = 4L;
    private static final Long PARENT_COMMENT_ID = 5L;
    private static final Long LEADER_MEMBER_ID = 6L;
    private static final Long MANAGER_MEMBER_ID = 7L;

    private Club mockClub;
    private Article mockArticle;
    private Member mockMember;
    private Comment mockComment;
    private ClubMember mockClubMember;
    private ClubMember mockLeader;
    private ClubMember mockManager;

    @BeforeEach
    void setUp() {
        mockClub = mock(Club.class);
        mockArticle = mock(Article.class);
        mockMember = mock(Member.class);
        mockComment = mock(Comment.class);
        mockClubMember = mock(ClubMember.class);
        mockLeader = mock(ClubMember.class);
        mockManager = mock(ClubMember.class);
    }

    @Nested
    @DisplayName("댓글 생성")
    class CreateComment {

        @Test
        @DisplayName("루트 댓글 생성 성공")
        void success_when_create_root_comment() {
            // given
            Comment mockRootComment = mock(Comment.class);
            CommentRequestDto dto = new CommentRequestDto("content");

            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(CLUB_ID);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(mockClubMember);
            given(mockClubMember.getMember()).willReturn(mockMember);

            given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockArticle);

            given(commentRepository.save(any())).willReturn(mockRootComment);
            given(mockRootComment.getMember()).willReturn(mockMember);
            given(mockRootComment.getArticle()).willReturn(mockArticle);

            // when
            CommentResponseDto rootComment = commentService.createRootComment(MEMBER_ID, ARTICLE_ID, dto);

            // then
            assertThat(rootComment).isNotNull();
            verify(commentRepository).save(any());
        }

        @Test
        @DisplayName("대댓글 생성 성공")
        void success_when_create_reply_comment() {
            // given
            Comment mockParentComment = mock(Comment.class);
            Comment mockReplyComment = mock(Comment.class);
            CommentRequestDto dto = new CommentRequestDto("content");

            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(CLUB_ID);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(mockClubMember);
            given(mockClubMember.getMember()).willReturn(mockMember);

            given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockArticle);

            given(commentValidator.findCommentByCommentIdOrThrow(PARENT_COMMENT_ID)).willReturn(mockParentComment);

            given(commentRepository.save(any())).willReturn(mockReplyComment);
            given(mockReplyComment.getMember()).willReturn(mockMember);
            given(mockReplyComment.getArticle()).willReturn(mockArticle);

            // when
            CommentResponseDto replyComment = commentService.createReplyComment(MEMBER_ID, ARTICLE_ID, PARENT_COMMENT_ID, dto);

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
            Comment mockUpdatedComment = mock(Comment.class);
            CommentRequestDto dto = new CommentRequestDto("update content");

            given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockArticle);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(CLUB_ID);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(mockClubMember);
            given(mockClubMember.getMember()).willReturn(mockMember);
            given(mockMember.getId()).willReturn(MEMBER_ID);

            given(commentValidator.findCommentByCommentIdAndMemberIdOrThrow(COMMENT_ID, MEMBER_ID)).willReturn(mockComment);

            given(mockComment.getArticle()).willReturn(mockArticle);
            given(mockArticle.getId()).willReturn(ARTICLE_ID);

            given(mockComment.updateContent(dto.content())).willReturn(mockUpdatedComment);
            given(mockUpdatedComment.getMember()).willReturn(mockMember);
            given(mockUpdatedComment.getArticle()).willReturn(mockArticle);

            // when
            CommentResponseDto response = commentService.updateComment(ARTICLE_ID, COMMENT_ID, MEMBER_ID, dto);

            // then
            assertThat(response).isNotNull();
            verify(commentValidator).findCommentByCommentIdAndMemberIdOrThrow(COMMENT_ID, MEMBER_ID);
            verify(mockComment).updateContent(dto.content());
        }

        @Test
        @DisplayName("댓글 수정 실패 - 다른 게시글의 댓글")
        void fail_when_update_comment_article_mismatch() {
            // given
            Long otherArticleId = 100L;
            Article mockRequestedArticle = mock(Article.class);
            Article mockOtherArticle = mock(Article.class);
            CommentRequestDto dto = new CommentRequestDto("update content");

            given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockRequestedArticle);
            given(mockRequestedArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(CLUB_ID);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(mockClubMember);
            given(mockClubMember.getMember()).willReturn(mockMember);
            given(mockMember.getId()).willReturn(MEMBER_ID);

            given(commentValidator.findCommentByCommentIdAndMemberIdOrThrow(COMMENT_ID, MEMBER_ID)).willReturn(mockComment);

            given(mockComment.getArticle()).willReturn(mockOtherArticle);
            given(mockOtherArticle.getId()).willReturn(otherArticleId);

            // when & then
            assertThatThrownBy(() -> commentService.updateComment(ARTICLE_ID, COMMENT_ID, MEMBER_ID, dto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(CommentErrorCode.ARTICLE_REQUIRED.message());
        }
    }

    @Nested
    @DisplayName("댓글 삭제")
    class DeleteComment {

        @Test
        @DisplayName("댓글 삭제 성공 - 일반 멤버가 자신의 댓글 삭제")
        void success_when_delete_comment() {
            // given
            given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockArticle);
            given(commentValidator.findCommentByCommentIdAndMemberIdOrThrow(COMMENT_ID, MEMBER_ID)).willReturn(mockComment);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(CLUB_ID);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(mockClubMember);

            given(commentValidator.findCommentByCommentIdAndMemberIdOrThrow(COMMENT_ID, MEMBER_ID)).willReturn(mockComment);

            given(mockComment.getArticle()).willReturn(mockArticle);
            given(mockArticle.getId()).willReturn(ARTICLE_ID);

            // when
            commentService.deleteComment(ARTICLE_ID, COMMENT_ID, MEMBER_ID);

            // then
            verify(commentRepository).delete(mockComment);
        }

        @Test
        @DisplayName("댓글 삭제 성공 - 리더가 타인 댓글 삭제")
        void success_when_leader_deletes_others_comment() {
            // given
            given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockArticle);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(CLUB_ID);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, LEADER_MEMBER_ID)).willReturn(mockLeader);
            given(mockLeader.getClubMemberRole()).willReturn(ClubMemberRole.LEADER);

            given(commentValidator.findCommentByCommentIdOrThrow(COMMENT_ID)).willReturn(mockComment);

            given(mockComment.getArticle()).willReturn(mockArticle);
            given(mockArticle.getId()).willReturn(ARTICLE_ID);

            // when
            commentService.deleteComment(ARTICLE_ID, COMMENT_ID, LEADER_MEMBER_ID);

            // then
            verify(commentRepository).delete(mockComment);
        }

        @Test
        @DisplayName("댓글 삭제 성공 - 매니저가 타인 댓글 삭제")
        void success_when_manager_deletes_others_comment() {
            // given
            given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockArticle);
            given(mockArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(CLUB_ID);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MANAGER_MEMBER_ID)).willReturn(mockManager);
            given(mockManager.getClubMemberRole()).willReturn(ClubMemberRole.MANAGER);

            given(commentValidator.findCommentByCommentIdOrThrow(COMMENT_ID)).willReturn(mockComment);

            given(mockComment.getArticle()).willReturn(mockArticle);
            given(mockArticle.getId()).willReturn(ARTICLE_ID);

            // when
            commentService.deleteComment(ARTICLE_ID, COMMENT_ID, MANAGER_MEMBER_ID);

            // then
            verify(commentRepository).delete(mockComment);
        }

        @Test
        @DisplayName("댓글 삭제 실패 - 리더라도 다른 게시글의 댓글이면 실패")
        void fail_when_leader_deletes_comment_of_another_article() {
            // given
            Long requestArticleId = 50L;
            Long realArticleId = 100L;

            Article mockRequestedArticle = mock(Article.class);
            Article mockOtherArticle = mock(Article.class);

            given(articleValidator.findArticleByArticleIdOrThrow(requestArticleId)).willReturn(mockRequestedArticle);
            given(mockRequestedArticle.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(CLUB_ID);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, LEADER_MEMBER_ID)).willReturn(mockLeader);
            given(mockLeader.getClubMemberRole()).willReturn(ClubMemberRole.LEADER);

            given(commentValidator.findCommentByCommentIdOrThrow(COMMENT_ID)).willReturn(mockComment);

            given(mockComment.getArticle()).willReturn(mockOtherArticle);
            given(mockOtherArticle.getId()).willReturn(realArticleId);

            // when & then
            assertThatThrownBy(() -> commentService.deleteComment(requestArticleId, COMMENT_ID, LEADER_MEMBER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(CommentErrorCode.ARTICLE_REQUIRED.message());

            verify(commentRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("댓글 목록 조회")
    class GetCommentList {

        @Test
        @DisplayName("특정 게시글의 댓글 리스트(좋아요 정보 포함) 조회 성공")
        void success_when_get_comment_list_by_article() {
            // given
            given(mockArticle.getId()).willReturn(ARTICLE_ID);
            given(mockMember.getId()).willReturn(MEMBER_ID);
            given(mockComment.getId()).willReturn(100L);
            given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockArticle);

            given(commentRepository.findCommentsWithRepliesByArticleId(ARTICLE_ID)).willReturn(List.of(mockComment));
            given(mockComment.getMember()).willReturn(mockMember);
            given(mockComment.getArticle()).willReturn(mockArticle);

            CommentLikeInfoDto likeInfoDto = CommentLikeInfoDto.toDto(2, true);
            given(commentRepository.findLikeInfoByCommentIdsAndMemberId(any(), eq(MEMBER_ID))).willReturn(Map.of(100L, likeInfoDto));

            // when
            List<RootCommentResponseDto> response = commentService.getCommentListByArticle(ARTICLE_ID, MEMBER_ID);

            // then
            assertThat(response).hasSize(1);

            RootCommentResponseDto dto = response.get(0);
            assertThat(dto.likeCount()).isEqualTo(2);
            assertThat(dto.isLiked()).isTrue();

            verify(commentRepository).findCommentsWithRepliesByArticleId(ARTICLE_ID);
            verify(commentRepository).findLikeInfoByCommentIdsAndMemberId(any(), eq(MEMBER_ID));
        }
    }
}
