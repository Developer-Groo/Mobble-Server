package com.mobble.mobbleserver.domain.like.commentLike.entity;

import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.support.fixture.comment.CommentTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CommentLikeTest {

    private final Member mockMember = MemberTestFixture.createDefaultMember();
    private final Comment mockComment = CommentTestFixture.createDefaultRootComment();

    @Test
    @DisplayName("CommentLike 생성 성공")
    void success_when_create_comment_like() {
        // when
        CommentLike like = CommentLike.createCommentLike(mockComment, mockMember);

        // then
        assertThat(like.getComment()).isEqualTo(mockComment);
        assertThat(like.getMember()).isEqualTo(mockMember);
    }

    @Test
    @DisplayName("comment 가 null 인 경우 예외 발생")
    void fails_when_comment_is_null() {
        // when & then
        assertThatThrownBy(() -> CommentLike.createCommentLike(null, mockMember))
                .isInstanceOf(DomainException.class)
                .hasMessage(LikeErrorCode.COMMENT_REQUIRED.message());
    }

    @Test
    @DisplayName("member 가 null 인 경우 예외 발생")
    void fails_when_member_is_null() {
        // when & then
        assertThatThrownBy(() -> CommentLike.createCommentLike(mockComment, null))
                .isInstanceOf(DomainException.class)
                .hasMessage(LikeErrorCode.MEMBER_REQUIRED.message());
    }
}
