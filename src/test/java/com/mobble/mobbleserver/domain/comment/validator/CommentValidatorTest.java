package com.mobble.mobbleserver.domain.comment.validator;

import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.comment.CommentErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentValidatorTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentValidator commentValidator;

    private static final Long COMMENT_ID = 1L;
    private static final Long MEMBER_ID = 2L;

    @Test
    @DisplayName("댓글 ID 로 조회 성공")
    void success_when_find_comment_or_Throw() {
        // given
        Comment mockComment = mock(Comment.class);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(mockComment));

        // when
        Comment comment = commentValidator.findCommentByCommentIdOrThrow(COMMENT_ID);

        // then
        assertThat(comment).isEqualTo(mockComment);
    }

    @Test
    @DisplayName("댓글 ID 로 조회 실패 시 예외 발생")
    void fails_when_find_comment_or_throw() {
        // given
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentValidator.findCommentByCommentIdOrThrow(COMMENT_ID))
                .isInstanceOf(DomainException.class)
                .hasMessage(CommentErrorCode.NOT_FOUND.message());
    }

    @Test
    @DisplayName("댓글 ID 와 멤버 ID 로 조회 성공")
    void success_when_find_comment_by_id_and_member_or_throw() {
        // given
        Comment mockComment = mock(Comment.class);

        when(commentRepository.findByIdAndMemberId(COMMENT_ID, MEMBER_ID)).thenReturn(Optional.of(mockComment));

        // when
        Comment comment = commentValidator.findCommentByCommentIdAndMemberIdOrThrow(COMMENT_ID, MEMBER_ID);

        // then
        assertThat(comment).isEqualTo(mockComment);
    }

    @Test
    @DisplayName("댓글 ID 와 멤버 ID 로 조회 실패 시 예외 발생")
    void fail_find_comment_by_id_and_member_or_throw() {
        // given
        when(commentRepository.findByIdAndMemberId(COMMENT_ID, MEMBER_ID)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentValidator.findCommentByCommentIdAndMemberIdOrThrow(COMMENT_ID, MEMBER_ID))
                .isInstanceOf(DomainException.class)
                .hasMessage(CommentErrorCode.NO_PERMISSION.message());
    }
}
