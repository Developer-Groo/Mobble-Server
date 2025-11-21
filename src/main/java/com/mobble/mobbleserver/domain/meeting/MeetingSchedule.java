package com.mobble.mobbleserver.domain.meeting;

import com.mobble.mobbleserver.domain.common.exception.DomainException;
import com.mobble.mobbleserver.domain.meeting.error.MeetingError;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

@Value
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class MeetingSchedule {

    @Column(name = "datetime", nullable = false)
    LocalDateTime datetime;

    private MeetingSchedule(LocalDateTime datetime) {
        requireNonNull(datetime, "schedule must not be null");

        LocalDateTime now = LocalDateTime.now();
        if (datetime.isBefore(now)) throw new DomainException(MeetingError.INVALID_DATETIME);

        this.datetime = datetime;
    }

    public static MeetingSchedule of(LocalDateTime datetime) {
        return new MeetingSchedule(datetime);
    }
}
