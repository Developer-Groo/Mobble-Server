package com.mobble.mobbleserver.domain.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleLikeTest {

    private static final Long MEMBER_ID = 1L;
    private static final Long ARTICLE_ID = 10L;

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("articleLike 생성 성공")
        void create_whenValidParameters_thenSuccess() {
            // given & when
            ArticleLike like = ArticleLike.create(MEMBER_ID, ARTICLE_ID);

            // then
            assertThat(like.getMemberId()).isEqualTo(MEMBER_ID);
            assertThat(like.getArticleId()).isEqualTo(ARTICLE_ID);

            assertThat(like.getId()).isNull();
        }
    }
}
