package com.mobble.mobbleserver.application.meetingMember.service;

import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.meeting.error.MeetingBusinessError;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.application.meetingMember.port.provided.MeetingMemberQueryPort;
import com.mobble.mobbleserver.application.meetingMember.result.MeetingMemberResult;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingMemberQueryService implements MeetingMemberQueryPort {

    private final MeetingReadPort meetingReadPort;

    @Override
    public MeetingMemberResult getMeetingMembers(Long meetingId) {
        Meeting meeting = findMeetingByMeetingIdOrThrow(meetingId);
        List<Member> attendedMembers = meeting.getAttendedMembers();

        return MeetingMemberResult.create(meeting.getId(), attendedMembers);
    }

    /* ==== Private Helper ==== */
    public Meeting findMeetingByMeetingIdOrThrow(Long meetingId) {
        return meetingReadPort.findById(meetingId)
                .orElseThrow(() -> new BusinessException(MeetingBusinessError.NOT_FOUND));
    }
}
