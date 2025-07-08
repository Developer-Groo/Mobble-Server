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

    @Test
    @DisplayName("댓글 ID 로 조회 성공")
    void success_when_find_comment_or_Throw() {
        // given
        Long commentId = 1L;
        Comment mockComment = mock(Comment.class);
        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(mockComment));

        // when
        Comment comment = commentValidator.findCommentOrThrow(commentId);

        // then
        assertThat(comment).isEqualTo(mockComment);
    }

    @Test
    @DisplayName("댓글 ID 로 조회 실패 시 예외 발생")
    void fails_when_find_comment_or_throw() {
        // given
        Long commentId = 1L;
        when(commentRepository.findById(commentId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentValidator.findCommentOrThrow(commentId))
                .isInstanceOf(DomainException.class)
                .hasMessage(CommentErrorCode.NOT_FOUND.message());
    }

    @Test
    @DisplayName("댓글 ID 와 멤버 ID 로 조회 성공")
    void success_when_find_comment_by_id_and_member_or_throw() {
        // given
        Long commentId = 1L;
        Long memberId = 2L;
        Comment mockComment = mock(Comment.class);
        when(commentRepository.findByIdAndMemberId(commentId, memberId))
                .thenReturn(Optional.of(mockComment));

        // when
        Comment comment = commentValidator.findCommentByIdAndMemberOrThrow(memberId, commentId);

        // then
        assertThat(comment).isEqualTo(mockComment);
    }

    @Test
    @DisplayName("댓글 ID 와 멤버 ID 로 조회 실패 시 예외 발생")
    void fail_find_comment_by_id_and_member_or_throw() {
        // given
        Long commentId = 1L;
        Long memberId = 2L;
        when(commentRepository.findByIdAndMemberId(commentId, memberId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentValidator.findCommentByIdAndMemberOrThrow(memberId, commentId))
                .isInstanceOf(DomainException.class)
                .hasMessage(CommentErrorCode.NO_PERMISSION.message());
    }
}