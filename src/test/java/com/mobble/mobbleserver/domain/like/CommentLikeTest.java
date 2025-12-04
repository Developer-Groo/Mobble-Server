package com.mobble.mobbleserver.domain.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        @Test
        @DisplayName("memberId == null -> 예외")
        void create_fail_when_member_id_null() {
            // when & then
            assertThatThrownBy(() -> CommentLike.create(null, COMMENT_ID))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("memberId must not be null");
        }

        @Test
        @DisplayName("commentId == null -> 예외")
        void create_fail_when_comment_id_null() {
            // when & then
            assertThatThrownBy(() -> CommentLike.create(MEMBER_ID, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("commentId must not be null");
        }
    }
}
