package com.mobble.mobbleserver.domain.like.counter;

import com.mobble.mobbleserver.domain.like.LikeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LikeCounterTest {

    private static final LikeType LIKE_TYPE = LikeType.ARTICLE;
    private static final Long TARGET_ID = 10L;

    @Nested
    @DisplayName("create")
    class create {

        @Test
        @DisplayName("likeCounter 생성 성공 (count = 0)")
        void create_success() {
            // when
            LikeCounter counter = LikeCounter.create(LIKE_TYPE, TARGET_ID);

            // then
            assertThat(counter.getLikeType()).isEqualTo(LIKE_TYPE);
            assertThat(counter.getTargetId()).isEqualTo(TARGET_ID);
            assertThat(counter.getCount()).isZero();

            assertThat(counter.getId()).isNull();
        }
    }
}
