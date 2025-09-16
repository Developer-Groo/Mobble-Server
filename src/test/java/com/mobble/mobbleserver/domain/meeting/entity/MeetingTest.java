package com.mobble.mobbleserver.domain.meeting.entity;

import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.entity.JoinStatus;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingErrorCode;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.clubMember.ClubMemberTestFixture;
import com.mobble.mobbleserver.support.fixture.meeting.MeetingTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MeetingTest {

    private final Member mockMember = MemberTestFixture.createDefaultMember();
    private final ClubCategory mockClubCategory = ClubCategory.createClubCategory("SOCCER");
    private final Club mockClub = ClubTestFixture.createDefaultClub(mockClubCategory);
    private final ClubMember mockClubMember = ClubMemberTestFixture.createDefaultClubMember(
            mockMember,
            mockClub,
            ClubMemberRole.LEADER,
            JoinStatus.APPROVED
    );
    private final Meeting mockMeeting = MeetingTestFixture.createDefaultMeeting();

    private static final String TITLE = "정기모임";
    private static final String LOCATION = "체육관";
    private static final String COST = "5000";
    private static final int LIMIT = 10;
    private static final MeetingType TYPE = MeetingType.REGULAR_MEETING;
    private static final LocalDateTime DATETIME = LocalDateTime.of(2025, 10, 10, 19, 0);

    @Test
    @DisplayName("모임 생성 성공")
    void success_when_create_meeting() {
        // given & when
        Meeting meeting = Meeting.createMeeting(
                mockClubMember,
                TITLE,
                DATETIME,
                LOCATION,
                COST,
                LIMIT,
                TYPE
        );

        // then
        assertThat(meeting.getClubMember()).isEqualTo(mockClubMember);
        assertThat(meeting.getTitle()).isEqualTo(TITLE);
        assertThat(meeting.getDatetime()).isEqualTo(DATETIME);
        assertThat(meeting.getLocation()).isEqualTo(LOCATION);
        assertThat(meeting.getCost()).isEqualTo(COST);
        assertThat(meeting.getMemberLimit()).isEqualTo(LIMIT);
        assertThat(meeting.getType()).isEqualTo(TYPE);
        assertThat(meeting.getMeetingMembers()).isEmpty();
    }

    @Test
    @DisplayName("ClubMember 가 null 이면 예외 발생")
    void fails_when_club_member_is_null() {
        // when & then
        assertThatThrownBy(() -> Meeting.createMeeting(
                null,
                TITLE,
                DATETIME,
                LOCATION,
                COST,
                LIMIT,
                TYPE
        ))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingErrorCode.CLUB_MEMBER_REQUIRED.message());
    }

    @Test
    @DisplayName("모임 수정 성공")
    void success_when_update_meeting() {
        // given & when
        mockMeeting.updateMeeting(
                "수정된 title",
                DATETIME.plusDays(2),
                "다른 체육관",
                "7000",
                20,
                MeetingType.IMPROMPTU_MEETING
        );

        // then
        assertThat(mockMeeting.getTitle()).isEqualTo("수정된 title");
        assertThat(mockMeeting.getDatetime()).isEqualTo(DATETIME.plusDays(2));
        assertThat(mockMeeting.getLocation()).isEqualTo("다른 체육관");
        assertThat(mockMeeting.getCost()).isEqualTo("7000");
        assertThat(mockMeeting.getMemberLimit()).isEqualTo(20);
        assertThat(mockMeeting.getType()).isEqualTo(MeetingType.IMPROMPTU_MEETING);
    }

    @Test
    @DisplayName("제목을 null 로 변경 시 예외 발생")
    void fails_when_title_is_null() {
        // when & then
        assertThatThrownBy(() ->
                mockMeeting.updateMeeting(null, DATETIME, LOCATION, COST, LIMIT, TYPE))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingErrorCode.TITLE_REQUIRED.message());
    }

    @Test
    @DisplayName("제목을 공백으로 변경 시 예외 발생")
    void fails_when_title_is_blank() {
        // when & then
        assertThatThrownBy(() ->
                mockMeeting.updateMeeting(" ", DATETIME, LOCATION, COST, LIMIT, TYPE))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingErrorCode.TITLE_REQUIRED.message());
    }

    @Test
    @DisplayName("모임 날짜를 null 로 변경 시 예외 발생")
    void fails_when_datetime_is_null() {
        // when & then
        assertThatThrownBy(() ->
                mockMeeting.updateMeeting(TITLE, null, LOCATION, COST, LIMIT, TYPE))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingErrorCode.DATETIME_REQUIRED.message());
    }

    @Test
    @DisplayName("장소를 null 로 변경 시 예외 발생")
    void fails_when_location_is_null() {
        // when & then
        assertThatThrownBy(() ->
                mockMeeting.updateMeeting(TITLE, DATETIME, null, COST, LIMIT, TYPE))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingErrorCode.LOCATION_REQUIRED.message());
    }

    @Test
    @DisplayName("장소를 공백으로 변경 시 예외 발생")
    void fails_when_location_is_blank() {
        // when & then
        assertThatThrownBy(() ->
                mockMeeting.updateMeeting(TITLE, DATETIME, " ", COST, LIMIT, TYPE))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingErrorCode.LOCATION_REQUIRED.message());
    }

    @Test
    @DisplayName("비용을 null 로 변경 시 예외 발생")
    void fails_when_cost_is_null() {
        // when & then
        assertThatThrownBy(() ->
                mockMeeting.updateMeeting(TITLE, DATETIME, LOCATION, null, LIMIT, TYPE))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingErrorCode.COST_REQUIRED.message());
    }

    @Test
    @DisplayName("비용을 공백으로 변경 시 예외 발생")
    void fails_when_cost_is_blank() {
        // when & then
        assertThatThrownBy(() ->
                mockMeeting.updateMeeting(TITLE, DATETIME, LOCATION, " ", LIMIT, TYPE))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingErrorCode.COST_REQUIRED.message());
    }

    @Test
    @DisplayName("참여 인원을 0 으로 변경 시 예외 발생")
    void fails_when_member_limit_is_zero() {
        // when & then
        assertThatThrownBy(() ->
                mockMeeting.updateMeeting(TITLE, DATETIME, LOCATION, COST, 0, TYPE))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingErrorCode.MEMBER_LIMIT_REQUIRED.message());
    }

    @Test
    @DisplayName("참여 인원을 음수로 변경 시 예외 발생")
    void fails_when_member_limit_is_negative() {
        // when & then
        assertThatThrownBy(() ->
                mockMeeting.updateMeeting(TITLE, DATETIME, LOCATION, COST, -10, TYPE))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingErrorCode.MEMBER_LIMIT_REQUIRED.message());
    }
}
