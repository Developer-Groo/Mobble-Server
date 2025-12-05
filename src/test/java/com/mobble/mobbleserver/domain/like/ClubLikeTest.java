package com.mobble.mobbleserver.domain.like;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClubLikeTest {

    private static final Long MEMBER_ID = 2L;
    private static final Long CLUB_ID = 20L;

    @Test
    void create_success() {
        ClubLike like = ClubLike.create(MEMBER_ID, CLUB_ID);

        assertThat(like.getMemberId()).isEqualTo(MEMBER_ID);
        assertThat(like.getClubId()).isEqualTo(CLUB_ID);

        assertThat(like.getId()).isNull();
    }

    @Test
    void create_fail_when_member_id_null() {
        assertThatThrownBy(() -> ClubLike.create(null, CLUB_ID))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("memberId must not be null");
    }

    @Test
    void create_fail_when_club_id_null() {
        assertThatThrownBy(() -> ClubLike.create(MEMBER_ID, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("clubId must not be null");
    }
}
