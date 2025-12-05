package com.mobble.mobbleserver.domain.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ArticleLikeTest {

    private static final Long MEMBER_ID = 1L;
    private static final Long ARTICLE_ID = 10L;

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("articleLike 생성 성공")
        void create_success() {
            ArticleLike like = ArticleLike.create(MEMBER_ID, ARTICLE_ID);

            assertThat(like.getMemberId()).isEqualTo(MEMBER_ID);
            assertThat(like.getArticleId()).isEqualTo(ARTICLE_ID);

            assertThat(like.getId()).isNull();
        }

        @Test
        @DisplayName("memberId == null -> 예외")
        void create_fail_when_member_id_null() {
            assertThatThrownBy(() -> ArticleLike.create(null, ARTICLE_ID))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("memberId must not be null");
        }

        @Test
        @DisplayName("articleId == null -> 예외")
        void create_fail_when_article_id_null() {
            assertThatThrownBy(() -> ArticleLike.create(MEMBER_ID, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("articleId must not be null");
        }
    }
}
