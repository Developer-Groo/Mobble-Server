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
}
