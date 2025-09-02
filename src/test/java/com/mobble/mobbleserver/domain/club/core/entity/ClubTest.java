package com.mobble.mobbleserver.domain.club.core.entity;

import com.mobble.mobbleserver.domain.chat.clubChatRoom.entity.ClubChatRoom;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.clubCategory.ClubCategoryTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ClubTest {

    private final ClubCategory mockCategory = ClubCategoryTestFixture.createDefaultCategory();
    private final Club mockClub = ClubTestFixture.createDefaultClub(mockCategory);

    private final String name = "서울 취미 축구 클럽";
    private final String ground = "잠실종합운동장 보조구장";
    private final String address = "서울시 송파구 올림픽로 25";
    private final int headCount = 20;
    private final boolean isAutoJoin = true;

    @Nested
    @DisplayName("클럽 생성 테스트")
    class CreateClub {

        @Test
        @DisplayName("정상 생성")
        void create_success() {
            // given & when
            Club club = Club.createClub(mockCategory, name, ground, address, headCount, isAutoJoin);

            // then
            assertThat(club.getClubCategory()).isEqualTo(mockCategory);
            assertThat(club.getName()).isEqualTo(name);
            assertThat(club.getGround()).isEqualTo(ground);
            assertThat(club.getAddress()).isEqualTo(address);
            assertThat(club.getHeadCount()).isEqualTo(headCount);
            assertThat(club.isAutoJoin()).isTrue();
        }

        @Test
        @DisplayName("category == null → 예외")
        void create_fail_when_category_null() {
            assertThatThrownBy(() -> Club.createClub(null, name, ground, address, headCount, isAutoJoin))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ClubErrorCode.CATEGORY_REQUIRED.message());
        }
