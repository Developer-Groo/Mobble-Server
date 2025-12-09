package com.mobble.mobbleserver.infrastructure.persistence.meeting;

import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingWritePort;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MeetingPersistenceAdapter implements MeetingWritePort, MeetingReadPort {

    private final JpaMeetingRepository repository;

    /* MeetingWritePort */
    @Override
    public Meeting save(Meeting meeting) {
        return repository.save(meeting);
    }

    @Override
    public void delete(Meeting meeting) {
        repository.delete(meeting);
    }

    @Override
    public void deleteAll(List<Meeting> meetings) {
        repository.deleteAll(meetings);
    }

    /* MeetingReadPort */
    @Override
    public Optional<Meeting> findById(Long meetingId) {
        return repository.findById(meetingId);
    }

    @Override
    public List<Meeting> findMeetingsByClubId(Long clubId) {
        return repository.findByClubIdOrderBySchedule_DatetimeAsc(clubId);
    }

    @Override
    public List<Meeting> findUpcomingMeetingsByClubId(Long clubId, LocalDateTime today) {
        return repository.findUpcomingMeetings(clubId, today);
    }

    @Override
    public List<Long> findMainImageIdsByClubId(Long clubId) {
        return repository.findMainImageIdsByClubId(clubId);
    }
}
