package com.mobble.mobbleserver.domain.like.counter;

import com.mobble.mobbleserver.domain.like.LikeType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LikeCounterTest {

    private static final LikeType LIKE_TYPE = LikeType.ARTICLE;
    private static final Long TARGET_ID = 10L;

    @Nested
    class create {

        @Test
        void create_success() {
            LikeCounter counter = LikeCounter.create(LIKE_TYPE, TARGET_ID);

            assertThat(counter.getLikeType()).isEqualTo(LIKE_TYPE);
            assertThat(counter.getTargetId()).isEqualTo(TARGET_ID);
            assertThat(counter.getCount()).isZero();

            assertThat(counter.getId()).isNull();
        }

        @Test
        void create_fail_when_like_type_null() {
            assertThatThrownBy(() -> LikeCounter.create(null, TARGET_ID))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("likeType must not be null");
        }

        @Test
        void create_fail_when_target_id_null() {
            assertThatThrownBy(() -> LikeCounter.create(LIKE_TYPE, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("targetId must not be null");

        }
    }
}
