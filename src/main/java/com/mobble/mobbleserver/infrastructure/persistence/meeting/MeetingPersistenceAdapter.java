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

    private final JpaMeetingRepository jpaMeetingRepository;

    @Override
    public Meeting save(Meeting meeting) {
        return jpaMeetingRepository.save(meeting);
    }

    @Override
    public void delete(Meeting meeting) {
        jpaMeetingRepository.delete(meeting);
    }

    @Override
    public void deleteAll(List<Meeting> meetings) {
        jpaMeetingRepository.deleteAll(meetings);
    }

    @Override
    public Optional<Meeting> findById(Long meetingId) {
        return jpaMeetingRepository.findById(meetingId);
    }

    @Override
    public List<Meeting> findMeetingsByClubId(Long clubId) {
        return jpaMeetingRepository.findByClubMember_Club_IdOrderBySchedule_DatetimeAsc(clubId);
    }

    @Override
    public List<Meeting> findUpcomingMeetingsByClubId(Long clubId, LocalDateTime today) {
        return jpaMeetingRepository.findByClubMember_Club_IdAndSchedule_DatetimeGreaterThanEqualOrderBySchedule_DatetimeAsc(clubId, today);
    }
}
