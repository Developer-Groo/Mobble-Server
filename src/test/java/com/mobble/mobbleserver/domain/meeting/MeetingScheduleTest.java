package com.mobble.mobbleserver.domain.meeting;

import com.mobble.mobbleserver.domain.exception.DomainException;
import com.mobble.mobbleserver.domain.meeting.error.MeetingError;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MeetingScheduleTest {

    @Test
    void success_create() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        MeetingSchedule schedule = MeetingSchedule.of(dateTime);

        assertThat(schedule.getDatetime()).isEqualTo(dateTime);
    }

    @Test
    void success_fail_when_datetime_null() {
        assertThatThrownBy(() -> MeetingSchedule.of(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("schedule must not be null");
    }

    @Test
    void success_when_fail_datetime_past() {
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);

        assertThatThrownBy(() -> MeetingSchedule.of(pastDateTime))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingError.INVALID_DATETIME.message());
    }
}
