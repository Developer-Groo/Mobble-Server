package com.mobble.mobbleserver.domain.like;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentLikeTest {

    private static final Long MEMBER_ID = 3L;
    private static final Long COMMENT_ID = 30L;

    @Test
    void create_success() {
        CommentLike like = CommentLike.create(MEMBER_ID, COMMENT_ID);

        assertThat(like.getMemberId()).isEqualTo(MEMBER_ID);
        assertThat(like.getCommentId()).isEqualTo(COMMENT_ID);

        assertThat(like.getId()).isNull();
    }

    @Test
    void create_fail_when_member_id_null() {
        assertThatThrownBy(() -> CommentLike.create(null, COMMENT_ID))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("memberId must not be null");
    }

    @Test
    void create_fail_when_comment_id_null() {
        assertThatThrownBy(() -> CommentLike.create(MEMBER_ID, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("commentId must not be null");
    }
}
