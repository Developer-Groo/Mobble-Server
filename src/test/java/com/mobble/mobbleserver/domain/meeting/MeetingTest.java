package com.mobble.mobbleserver.domain.meeting;

import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.exception.DomainException;
import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.meeting.error.MeetingError;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class MeetingTest {

    private static final String TITLE = "정기모임";
    private static final String LOCATION = "체육관";
    private static final String COST = "5000";
    private static final int LIMIT = 10;
    private static final MeetingType TYPE = MeetingType.REGULAR_MEETING;

    private static final LocalDateTime DATETIME = LocalDateTime.now().plusDays(1);

    private final Club mockClub = mock(Club.class);
    private final Member mockOwner = mock(Member.class);
    private final Image mockImage = mock(Image.class);

    private Meeting createDefaultMeeting() {
        return Meeting.create(
                mockClub,
                mockOwner,
                TITLE,
                mockImage,
                MeetingSchedule.of(DATETIME),
                LOCATION,
                COST,
                LIMIT,
                TYPE
        );
    }

    @Nested
    class Create {

        @Test
        void success_create() {
            Meeting meeting = Meeting.create(
                    mockClub,
                    mockOwner,
                    TITLE,
                    mockImage,
                    MeetingSchedule.of(DATETIME),
                    LOCATION,
                    COST,
                    LIMIT,
                    TYPE
            );

            assertThat(meeting.getClub()).isEqualTo(mockClub);
            assertThat(meeting.getOwner()).isEqualTo(mockOwner);
            assertThat(meeting.getTitle()).isEqualTo(TITLE);
            assertThat(meeting.getMainImage()).isEqualTo(mockImage);
            assertThat(meeting.getSchedule().getDatetime()).isEqualTo(DATETIME);
            assertThat(meeting.getLocation()).isEqualTo(LOCATION);
            assertThat(meeting.getCost()).isEqualTo(COST);
            assertThat(meeting.getMemberLimit()).isEqualTo(LIMIT);
            assertThat(meeting.getType()).isEqualTo(TYPE);
            assertThat(meeting.getMeetingMembers()).isEmpty();
        }

        @Test
        void create_fail_when_club_null() {
            assertThatThrownBy(() -> Meeting.create(
                            null,
                            mockOwner,
                            TITLE,
                            mockImage,
                            MeetingSchedule.of(DATETIME),
                            LOCATION,
                            COST,
                            LIMIT,
                            TYPE
                    )
            )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("club must not be null");
        }

        @Test
        void create_fail_when_owner_null() {
            assertThatThrownBy(() -> Meeting.create(
                            mockClub,
                            null,
                            TITLE,
                            mockImage,
                            MeetingSchedule.of(DATETIME),
                            LOCATION,
                            COST,
                            LIMIT,
                            TYPE
                    )
            )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("owner must not be null");
        }
    }

    @Nested
    class Update {

        @Test
        void update_success() {
            Meeting meeting = createDefaultMeeting();

            meeting.update(
                    "수정된 title",
                    MeetingSchedule.of(DATETIME.plusDays(2)),
                    "다른 체육관",
                    "7000",
                    20,
                    MeetingType.IMPROMPTU_MEETING
            );

            assertThat(meeting.getTitle()).isEqualTo("수정된 title");
            assertThat(meeting.getSchedule().getDatetime()).isEqualTo(DATETIME.plusDays(2));
            assertThat(meeting.getLocation()).isEqualTo("다른 체육관");
            assertThat(meeting.getCost()).isEqualTo("7000");
            assertThat(meeting.getMemberLimit()).isEqualTo(20);
            assertThat(meeting.getType()).isEqualTo(MeetingType.IMPROMPTU_MEETING);
        }
    }

    @Nested
    class Validation {

        @Test
        void update_fail_when_title_null() {
            Meeting meeting = createDefaultMeeting();

            assertThatThrownBy(() -> meeting.update(
                            null,
                            MeetingSchedule.of(DATETIME),
                            LOCATION,
                            COST,
                            LIMIT,
                            TYPE
                    )
            )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("title must not be null");
        }

        @Test
        void update_fail_when_schedule_null() {
            Meeting meeting = createDefaultMeeting();

            assertThatThrownBy(() -> meeting.update(
                            TITLE,
                            null,
                            LOCATION,
                            COST,
                            LIMIT,
                            TYPE
                    )
            )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("schedule must not be null");
        }

        @Test
        void update_fail_when_location_null() {
            Meeting meeting = createDefaultMeeting();

            assertThatThrownBy(() -> meeting.update(
                            TITLE,
                            MeetingSchedule.of(DATETIME),
                            null,
                            COST,
                            LIMIT,
                            TYPE
                    )
            )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("location must not be null");
        }

        @Test
        void update_fail_when_cost_null() {
            Meeting meeting = createDefaultMeeting();

            assertThatThrownBy(() -> meeting.update(
                            TITLE,
                            MeetingSchedule.of(DATETIME),
                            LOCATION,
                            null,
                            LIMIT,
                            TYPE
                    )
            )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("cost must not be null");
        }

        @Test
        void update_fail_when_limit_is_zero() {
            Meeting meeting = createDefaultMeeting();

            assertThatThrownBy(() -> meeting.update(
                            TITLE,
                            MeetingSchedule.of(DATETIME),
                            LOCATION,
                            COST,
                            0,
                            TYPE
                    )
            )
                    .isInstanceOf(DomainException.class)
                    .hasMessage(MeetingError.INVALID_MEMBER_LIMIT.message());
        }

        @Test
        void update_fail_when_type_null() {
            Meeting meeting = createDefaultMeeting();

            assertThatThrownBy(() -> meeting.update(
                            TITLE,
                            MeetingSchedule.of(DATETIME),
                            LOCATION,
                            COST,
                            LIMIT,
                            null
                    )
            )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("type must not be null");
        }
    }

    @Nested
    class Attend {

        @Test
        void success_attend() {
            Meeting meeting = createDefaultMeeting();
            Member member = MemberTestFixture.createDefaultMember();
            ReflectionTestUtils.setField(member, "id", 1L);

            meeting.attend(member);

            assertThat(meeting.getAttendeeCount()).isEqualTo(1);
            assertThat(meeting.hasAttendee(1L)).isTrue();
        }

        @Test
        void success_when_attend_same_member_twice() {
            Meeting meeting = createDefaultMeeting();
            Member member = MemberTestFixture.createDefaultMember();
            ReflectionTestUtils.setField(member, "id", 1L);

            meeting.attend(member);
            meeting.attend(member);

            assertThat(meeting.getAttendeeCount()).isEqualTo(1);
        }

        @Test
        void success_fail_when_full_capacity() {
            Meeting meeting = Meeting.create(
                    mockClubMember,
                    TITLE,
                    MeetingSchedule.of(DATETIME),
                    LOCATION,
                    COST,
                    1,
                    TYPE
            );

            Member member1 = MemberTestFixture.createDefaultMember();
            Member member2 = MemberTestFixture.createDefaultMember();
            ReflectionTestUtils.setField(member1, "id", 1L);
            ReflectionTestUtils.setField(member2, "id", 2L);

            meeting.attend(member1);

            assertThatThrownBy(() -> meeting.attend(member2))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(MeetingError.FULL_CAPACITY.message());
        }

        @Test
        void success_attend_cancel() {
            Meeting meeting = createDefaultMeeting();
            Member member = MemberTestFixture.createDefaultMember();
            ReflectionTestUtils.setField(member, "id", 1L);

            meeting.attend(member);
            meeting.cancelAttend(1L);

            assertThat(meeting.getAttendeeCount()).isZero();
        }

        @Test
        void success_when_cancel_non_attended_member() {
            Meeting meeting = createDefaultMeeting();
            Member member = MemberTestFixture.createDefaultMember();
            ReflectionTestUtils.setField(member, "id", 1L);

            meeting.attend(member);
            assertThat(meeting.getAttendeeCount()).isEqualTo(1);

            meeting.cancelAttend(999L);
            assertThat(meeting.getAttendeeCount()).isEqualTo(1);
        }
    }
}
