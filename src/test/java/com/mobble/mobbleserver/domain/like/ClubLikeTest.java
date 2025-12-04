package com.mobble.mobbleserver.domain.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClubLikeTest {

    private static final Long MEMBER_ID = 2L;
    private static final Long CLUB_ID = 20L;

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("ClubLike 생성 성공")
        void create_Success() {
            // given & when
            ArticleLike like = ArticleLike.create(MEMBER_ID, CLUB_ID);

            // then
            assertThat(like.getMemberId()).isEqualTo(MEMBER_ID);
            assertThat(like.getArticleId()).isEqualTo(CLUB_ID);

            assertThat(like.getId()).isNull();
        }
    }
}
