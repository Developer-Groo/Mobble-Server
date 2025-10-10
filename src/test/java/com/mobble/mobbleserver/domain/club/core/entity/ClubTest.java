//package com.mobble.mobbleserver.domain.club.core.entity;
//
//import com.mobble.mobbleserver.domain.chat.clubChatRoom.entity.ClubChatRoom;
//import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
//import com.mobble.mobbleserver.global.exception.common.DomainException;
//import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
//import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
//import com.mobble.mobbleserver.support.fixture.clubCategory.ClubCategoryTestFixture;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.mock;
//
//class ClubTest {
//
//    private final ClubCategory mockCategory = ClubCategoryTestFixture.createDefaultCategory();
//    private final Club mockClub = ClubTestFixture.createDefaultClub(mockCategory);
//
//    private final String name = "서울 취미 축구 클럽";
//    private final String ground = "잠실종합운동장 보조구장";
//    private final String address = "서울시 송파구 올림픽로 25";
//    private final int headCount = 20;
//    private final boolean isAutoJoin = true;
//
//    @Nested
//    @DisplayName("클럽 생성 테스트")
//    class CreateClub {
//
//        @Test
//        @DisplayName("정상 생성")
//        void create_success() {
//            // given & when
//            Club club = Club.createClub(mockCategory, name, ground, address, headCount, isAutoJoin);
//
//            // then
//            assertThat(club.getClubCategory()).isEqualTo(mockCategory);
//            assertThat(club.getName()).isEqualTo(name);
//            assertThat(club.getGround()).isEqualTo(ground);
//            assertThat(club.getAddress()).isEqualTo(address);
//            assertThat(club.getHeadCount()).isEqualTo(headCount);
//            assertThat(club.isAutoJoin()).isTrue();
//        }
//
//        @Test
//        @DisplayName("category == null → 예외")
//        void create_fail_when_category_null() {
//            assertThatThrownBy(() -> Club.createClub(null, name, ground, address, headCount, isAutoJoin))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(ClubErrorCode.CATEGORY_REQUIRED.message());
//        }
//
//        @Test
//        @DisplayName("name null/blank → 예외")
//        void create_fail_when_name_invalid() {
//            assertThatThrownBy(() -> Club.createClub(mockCategory, null, ground, address, headCount, isAutoJoin))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(ClubErrorCode.NAME_REQUIRED.message());
//
//            assertThatThrownBy(() -> Club.createClub(mockCategory, "   ", ground, address, headCount, isAutoJoin))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(ClubErrorCode.NAME_REQUIRED.message());
//        }
//
//        @Test
//        @DisplayName("ground null/blank → 예외")
//        void create_fail_when_ground_invalid() {
//            assertThatThrownBy(() -> Club.createClub(mockCategory, name, null, address, headCount, isAutoJoin))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(ClubErrorCode.GROUND_REQUIRED.message());
//
//            assertThatThrownBy(() -> Club.createClub(mockCategory, name, "   ", address, headCount, isAutoJoin))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(ClubErrorCode.GROUND_REQUIRED.message());
//        }
//
//        @Test
//        @DisplayName("address null/blank → 예외")
//        void create_fail_when_address_invalid() {
//            assertThatThrownBy(() -> Club.createClub(mockCategory, name, ground, null, headCount, isAutoJoin))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(ClubErrorCode.ADDRESS_REQUIRED.message());
//
//            assertThatThrownBy(() -> Club.createClub(mockCategory, name, ground, "   ", headCount, isAutoJoin))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(ClubErrorCode.ADDRESS_REQUIRED.message());
//        }
//
//        @Test
//        @DisplayName("headCount <= 0 → 예외")
//        void create_fail_when_headcount_invalid() {
//            assertThatThrownBy(() -> Club.createClub(mockCategory, name, ground, address, 0, isAutoJoin))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(ClubErrorCode.HEADCOUNT_REQUIRED.message());
//
//            assertThatThrownBy(() -> Club.createClub(mockCategory, name, ground, address, -1, isAutoJoin))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(ClubErrorCode.HEADCOUNT_REQUIRED.message());
//        }
//    }
//
//    @Nested
//    @DisplayName("클럽 수정 테스트")
//    class UpdateClub {
//
//        @Test
//        @DisplayName("정상 수정")
//        void update_success() {
//            // given
//            ClubCategory newCategory = ClubCategoryTestFixture.createDefaultCategory();
//            String newName = "서울 주말 풋살 클럽";
//            String newGround = "상암 월드컵경기장 풋살장";
//            String newAddress = "서울시 마포구 월드컵로 240";
//            int newHeadCount = 30;
//            boolean newIsAutoJoin = false;
//
//            // when
//            mockClub.updateClub(newCategory, newName, newGround, newAddress, newHeadCount, newIsAutoJoin);
//
//            // then
//            assertThat(mockClub.getClubCategory()).isEqualTo(newCategory);
//            assertThat(mockClub.getName()).isEqualTo(newName);
//            assertThat(mockClub.getGround()).isEqualTo(newGround);
//            assertThat(mockClub.getAddress()).isEqualTo(newAddress);
//            assertThat(mockClub.getHeadCount()).isEqualTo(newHeadCount);
//            assertThat(mockClub.isAutoJoin()).isFalse();
//        }
//    }
//
//    @Nested
//    @DisplayName("연관 관계 편의 메서드")
//    class AssociationHelper {
//
//        @Test
//        @DisplayName("setClubChatRoomInternal 정상 동작")
//        void setClubChatRoomInternal_success() {
//            // given
//            Club club = Club.createClub(mockCategory, name, ground, address, headCount, isAutoJoin);
//            ClubChatRoom chatRoom = mock(ClubChatRoom.class);
//
//            // when
//            club.setClubChatRoomInternal(chatRoom);
//
//            // then
//            assertThat(club.getClubChatRoom()).isSameAs(chatRoom);
//        }
//    }
//}
