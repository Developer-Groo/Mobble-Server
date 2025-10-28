package com.mobble.mobbleserver.refactor.like.clubLike.entity;

import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.domain.ClubCategory.ClubCategory;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.clubCategory.ClubCategoryTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ClubLikeTest {

    private final Member mockMember = MemberTestFixture.createDefaultMember();
    private final ClubCategory mockClubCategory = ClubCategoryTestFixture.createDefaultCategory();
    private final Club mockClub = ClubTestFixture.createDefaultClub(mockClubCategory);

    @Test
    @DisplayName("ClubLike 생성 성공")
    void success_when_create_article_like() {
        // when
        ClubLike like = ClubLike.createClubLike(mockClub, mockMember);

        //then
        assertThat(like.getClub()).isEqualTo(mockClub);
        assertThat(like.getMember()).isEqualTo(mockMember);
    }

    @Test
    @DisplayName("club 이 null 인 경우 예외 발생")
    void fails_when_article_is_null() {
        // when & then
        assertThatThrownBy(() -> ClubLike.createClubLike(null, mockMember))
                .isInstanceOf(DomainException.class)
                .hasMessage(LikeErrorCode.CLUB_REQUIRED.message());
    }

    @Test
    @DisplayName("member 가 null 인 경우 예외 발생")
    void fails_when_member_is_null() {
        // when & then
        assertThatThrownBy(() -> ClubLike.createClubLike(mockClub, null))
                .isInstanceOf(DomainException.class)
                .hasMessage(LikeErrorCode.MEMBER_REQUIRED.message());
    }
}
