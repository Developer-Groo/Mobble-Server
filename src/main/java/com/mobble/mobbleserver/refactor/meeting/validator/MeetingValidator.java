package com.mobble.mobbleserver.refactor.meeting.validator;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingErrorCode;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.infrastructure.persistence.meeting.JpaMeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MeetingValidator {

    private final JpaMeetingRepository meetingRepository;

    public Meeting findMeetingByMeetingIdOrThrow(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new DomainException(MeetingErrorCode.NOT_FOUND_MEETING));
    }

    public List<Meeting> findMeetingsByClubId(Long clubId) {
        return meetingRepository.findByClubMember_Club_Id(clubId);
    }
}
