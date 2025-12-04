package com.mobble.mobbleserver.domain.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClubLikeTest {

    private static final Long MEMBER_ID = 2L;
    private static final Long CLUB_ID = 20L;

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("ClubLike 생성 성공")
        void create_success() {
            // given & when
            ClubLike like = ClubLike.create(MEMBER_ID, CLUB_ID);

            // then
            assertThat(like.getMemberId()).isEqualTo(MEMBER_ID);
            assertThat(like.getClubId()).isEqualTo(CLUB_ID);

            assertThat(like.getId()).isNull();
        }

        @Test
        @DisplayName("memberId == null -> 예외")
        void create_fail_when_member_id_null() {
            // when & then
            assertThatThrownBy(() -> ClubLike.create(null, CLUB_ID))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("memberId must not be null");
        }

        @Test
        @DisplayName("clubId == null -> 예외")
        void create_fail_when_club_id_null() {
            // when & then
            assertThatThrownBy(() -> ClubLike.create(MEMBER_ID, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("clubId must not be null");
        }
    }
}
