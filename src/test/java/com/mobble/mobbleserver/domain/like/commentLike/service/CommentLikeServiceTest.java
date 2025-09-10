package com.mobble.mobbleserver.domain.like.commentLike.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.validator.CommentValidator;
import com.mobble.mobbleserver.domain.like.commentLike.entity.CommentLike;
import com.mobble.mobbleserver.domain.like.commentLike.repository.CommentLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberValidationErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentLikeServiceTest {

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private CommentValidator commentValidator;

    @Mock
    private ClubMemberValidator clubMemberValidator;

    @InjectMocks
    CommentLikeService commentLikeService;

    private static final Long COMMENT_ID = 1L;
    private static final Long MEMBER_ID = 2L;

    private Club mockClub;
    private Article mockArticle;
    private Comment mockComment;
    private Member mockMember;
    private ClubMember mockClubMember;
    private CommentLike mockCommentLike;

    @BeforeEach
    void setUP() {
        mockClub = mock(Club.class);
        mockArticle = mock(Article.class);
        mockComment = mock(Comment.class);
        mockMember = mock(Member.class);
        mockClubMember = mock(ClubMember.class);
        mockCommentLike = mock(CommentLike.class);
    }

    @DisplayName("댓글 좋아요가 없으면 생성")
    @Test
    void success_when_not_liked() {
        // given
        Long clubId = 10L;

        given(mockComment.getId()).willReturn(COMMENT_ID);
        given(mockMember.getId()).willReturn(MEMBER_ID);

        given(mockComment.getArticle()).willReturn(mockArticle);
        given(mockArticle.getClub()).willReturn(mockClub);
        given(mockClub.getId()).willReturn(clubId);

        given(commentValidator.findCommentByCommentIdOrThrow(COMMENT_ID)).willReturn(mockComment);
        given(commentLikeRepository.findLikedByCommentIdAndMemberId(COMMENT_ID, MEMBER_ID)).willReturn(Optional.empty());
        given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, MEMBER_ID)).willReturn(mockClubMember);

        // when
        boolean result = commentLikeService.toggleLike(COMMENT_ID, mockMember).isLiked();

        // then
        assertThat(result).isTrue();
        verify(commentLikeRepository).save(any(CommentLike.class));
    }

    @DisplayName("댓글 좋아요가 있으면 삭제")
    @Test
    void success_when_has_liked() {
        // given
        given(mockComment.getId()).willReturn(COMMENT_ID);
        given(mockMember.getId()).willReturn(MEMBER_ID);

        given(commentValidator.findCommentByCommentIdOrThrow(COMMENT_ID)).willReturn(mockComment);
        given(commentLikeRepository.findLikedByCommentIdAndMemberId(COMMENT_ID, MEMBER_ID)).willReturn(Optional.of(mockCommentLike));

        // when
        boolean result = commentLikeService.toggleLike(COMMENT_ID, mockMember).isLiked();

        // then
        assertThat(result).isFalse();
        verify(commentLikeRepository).delete(mockCommentLike);
        verify(commentLikeRepository, never()).save(any());
    }

    @DisplayName("좋아요 할 댓글이 존재하지 않으면 예외 발생")
    @Test
    void fail_when_comment_not_found() {
        // given
        given(commentValidator.findCommentByCommentIdOrThrow(COMMENT_ID)).willThrow(new DomainException(LikeErrorCode.COMMENT_REQUIRED));

        // when & then
        assertThatThrownBy(() -> commentLikeService.toggleLike(COMMENT_ID, mockMember))
                .isInstanceOf(DomainException.class)
                .hasMessage(LikeErrorCode.COMMENT_REQUIRED.message());
    }

    @DisplayName("클럽 멤버가 아니면 예외 발생")
    @Test
    void fail_when_not_club_member() {
        // given
        Long clubId = 10L;

        given(mockComment.getId()).willReturn(COMMENT_ID);
        given(mockMember.getId()).willReturn(MEMBER_ID);
        given(mockComment.getArticle()).willReturn(mockArticle);
        given(mockArticle.getClub()).willReturn(mockClub);
        given(mockClub.getId()).willReturn(clubId);

        given(commentValidator.findCommentByCommentIdOrThrow(COMMENT_ID)).willReturn(mockComment);
        given(commentLikeRepository.findLikedByCommentIdAndMemberId(COMMENT_ID, MEMBER_ID)).willReturn(Optional.empty());
        given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, MEMBER_ID)).willThrow(new DomainException(ClubMemberValidationErrorCode.CLUB_MEMBER_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> commentLikeService.toggleLike(COMMENT_ID, mockMember))
                .isInstanceOf(DomainException.class)
                .hasMessage(ClubMemberValidationErrorCode.CLUB_MEMBER_NOT_FOUND.message());
    }
}
