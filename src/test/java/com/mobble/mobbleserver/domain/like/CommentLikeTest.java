package com.mobble.mobbleserver.domain.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentLikeTest {

    private static final Long MEMBER_ID = 3L;
    private static final Long COMMENT_ID = 30L;

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("commentLike 생성 성공")
        void create_success() {
            // given & when
            CommentLike like = CommentLike.create(MEMBER_ID, COMMENT_ID);

            // then
            assertThat(like.getMemberId()).isEqualTo(MEMBER_ID);
            assertThat(like.getCommentId()).isEqualTo(COMMENT_ID);

            assertThat(like.getId()).isNull();
        }
    }
}
