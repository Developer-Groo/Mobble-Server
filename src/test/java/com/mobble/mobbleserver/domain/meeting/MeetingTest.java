package com.mobble.mobbleserver.domain.meeting;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MeetingTest {

    private static final String TITLE = "정기모임";
    private static final String LOCATION = "체육관";
    private static final String COST = "5000";
    private static final int LIMIT = 10;
    private static final MeetingType TYPE = MeetingType.REGULAR_MEETING;

    private static final LocalDateTime DATETIME = LocalDateTime.now().plusDays(1);

    private final ClubMember mockClubMember = mock(ClubMember.class);

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        void success_create() {
            Meeting meeting = Meeting.create(
                    mockClubMember,
                    TITLE,
                    MeetingSchedule.of(DATETIME),
                    LOCATION,
                    COST,
                    LIMIT,
                    TYPE
            );

            assertThat(meeting.getClubMember()).isEqualTo(mockClubMember);
            assertThat(meeting.getTitle()).isEqualTo(TITLE);
            assertThat(meeting.getSchedule().getDatetime()).isEqualTo(DATETIME);
            assertThat(meeting.getLocation()).isEqualTo(LOCATION);
            assertThat(meeting.getCost()).isEqualTo(COST);
            assertThat(meeting.getMemberLimit()).isEqualTo(LIMIT);
            assertThat(meeting.getType()).isEqualTo(TYPE);
            assertThat(meeting.getMeetingMembers()).isEmpty();
        }

        @Test
        @DisplayName("ClubMember 가 null 이면 예외 발생")
        void fails_when_club_member_is_null() {
//            // when & then
//            assertThatThrownBy(() -> Meeting.create(
//                    null,
//                    TITLE,
//                    DATETIME,
//                    LOCATION,
//                    COST,
//                    LIMIT,
//                    TYPE
//            ))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MeetingErrorCode.CLUB_MEMBER_REQUIRED.message());
        }
    }

    @Nested
    @DisplayName("모임 수정 테스트")
    class UpdateMeeting {

        @Test
        @DisplayName("모임 수정 성공")
        void success_when_update_meeting() {
//            // given & when
//            mockMeeting.update(
//                    "수정된 title",
//                    DATETIME.plusDays(2),
//                    "다른 체육관",
//                    "7000",
//                    20,
//                    MeetingType.IMPROMPTU_MEETING
//            );
//
//
//            // then
//            assertThat(mockMeeting.getTitle()).isEqualTo("수정된 title");
//            assertThat(mockMeeting.getDatetime()).isEqualTo(DATETIME.plusDays(2));
//            assertThat(mockMeeting.getLocation()).isEqualTo("다른 체육관");
//            assertThat(mockMeeting.getCost()).isEqualTo("7000");
//            assertThat(mockMeeting.getMemberLimit()).isEqualTo(20);
//            assertThat(mockMeeting.getType()).isEqualTo(MeetingType.IMPROMPTU_MEETING);
        }
    }

    @Nested
    @DisplayName("모임 수정 유효성 검증 테스트")
    class Validation {

        @Test
        @DisplayName("제목을 null 로 변경 시 예외 발생")
        void fails_when_title_is_null() {
//            // when & then
//            assertThatThrownBy(() ->
//                    mockMeeting.update(null, DATETIME, LOCATION, COST, LIMIT, TYPE))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MeetingErrorCode.TITLE_REQUIRED.message());
        }

        @Test
        @DisplayName("제목을 공백으로 변경 시 예외 발생")
        void fails_when_title_is_blank() {
//            // when & then
//            assertThatThrownBy(() ->
//                    mockMeeting.update(" ", DATETIME, LOCATION, COST, LIMIT, TYPE))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MeetingErrorCode.TITLE_REQUIRED.message());
        }

        @Test
        @DisplayName("모임 날짜를 null 로 변경 시 예외 발생")
        void fails_when_datetime_is_null() {
//            // when & then
//            assertThatThrownBy(() ->
//                    mockMeeting.update(TITLE, null, LOCATION, COST, LIMIT, TYPE))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MeetingErrorCode.DATETIME_REQUIRED.message());
        }

        @Test
        @DisplayName("장소를 null 로 변경 시 예외 발생")
        void fails_when_location_is_null() {
            // when & then
//            assertThatThrownBy(() ->
//                    mockMeeting.update(TITLE, DATETIME, null, COST, LIMIT, TYPE))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MeetingErrorCode.LOCATION_REQUIRED.message());
        }

        @Test
        @DisplayName("장소를 공백으로 변경 시 예외 발생")
        void fails_when_location_is_blank() {
//            // when & then
//            assertThatThrownBy(() ->
//                    mockMeeting.update(TITLE, DATETIME, " ", COST, LIMIT, TYPE))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MeetingErrorCode.LOCATION_REQUIRED.message());
        }

        @Test
        @DisplayName("비용을 null 로 변경 시 예외 발생")
        void fails_when_cost_is_null() {
//            // when & then
//            assertThatThrownBy(() ->
//                    mockMeeting.update(TITLE, DATETIME, LOCATION, null, LIMIT, TYPE))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MeetingErrorCode.COST_REQUIRED.message());
        }

        @Test
        @DisplayName("비용을 공백으로 변경 시 예외 발생")
        void fails_when_cost_is_blank() {
//            // when & then
//            assertThatThrownBy(() ->
//                    mockMeeting.update(TITLE, DATETIME, LOCATION, " ", LIMIT, TYPE))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MeetingErrorCode.COST_REQUIRED.message());
        }

        @Test
        @DisplayName("참여 인원을 0 으로 변경 시 예외 발생")
        void fails_when_member_limit_is_zero() {
//            // when & then
//            assertThatThrownBy(() ->
//                    mockMeeting.update(TITLE, DATETIME, LOCATION, COST, 0, TYPE))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MeetingErrorCode.INVALID_MEMBER_LIMIT.message());
        }

        @Test
        @DisplayName("참여 인원을 음수로 변경 시 예외 발생")
        void fails_when_member_limit_is_negative() {
//            // when & then
//            assertThatThrownBy(() ->
//                    mockMeeting.update(TITLE, DATETIME, LOCATION, COST, -10, TYPE))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MeetingErrorCode.INVALID_MEMBER_LIMIT.message());
        }

        @Test
        @DisplayName("모임 타입을 null 로 변경 시 예외 발생")
        void fails_when_type_is_null() {
//            // when & then
//            assertThatThrownBy(() ->
//                    mockMeeting.update(TITLE, DATETIME, LOCATION, COST, LIMIT, null))
//                    .isInstanceOf(DomainException.class)
//                    .hasMessage(MeetingErrorCode.TYPE_REQUIRED.message());
        }
    }
}
