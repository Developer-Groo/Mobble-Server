package com.mobble.mobbleserver.domain.like;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleLikeTest {

    private static final Long MEMBER_ID = 1L;
    private static final Long ARTICLE_ID = 10L;

    @Test
    void create_success() {
        ArticleLike like = ArticleLike.create(MEMBER_ID, ARTICLE_ID);

        assertThat(like.getMemberId()).isEqualTo(MEMBER_ID);
        assertThat(like.getArticleId()).isEqualTo(ARTICLE_ID);

        assertThat(like.getId()).isNull();
    }

    @Test
    void create_fail_when_member_id_null() {
        assertThatThrownBy(() -> ArticleLike.create(null, ARTICLE_ID))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("memberId must not be null");
    }

    @Test
    void create_fail_when_article_id_null() {
        assertThatThrownBy(() -> ArticleLike.create(MEMBER_ID, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("articleId must not be null");
    }
}
