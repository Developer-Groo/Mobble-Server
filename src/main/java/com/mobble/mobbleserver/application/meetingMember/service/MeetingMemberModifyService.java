package com.mobble.mobbleserver.application.meetingMember.service;

import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.application.meetingMember.port.provided.AttendMeetingPort;
import com.mobble.mobbleserver.application.meetingMember.port.required.MeetingMemberReadPort;
import com.mobble.mobbleserver.application.meetingMember.port.required.MeetingMemberWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meeting.MeetingMember;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingMemberModifyService implements AttendMeetingPort {

    private final MeetingMemberWritePort meetingMemberWritePort;
    private final MeetingMemberReadPort meetingMemberReadPort;
    private final MeetingReadPort meetingReadPort;
    private final MemberReadPort memberReadPort;

    @Override
    public void attendMeeting(Long meetingId, Long memberId) {
        Meeting meeting = findMeetingByMeetingIdOrThrow(meetingId);
        Member member = findMemberByMemberIdOrThrow(memberId);

        Optional<MeetingMember> result = meetingMemberReadPort.findMeetingMemberByMeetingIdAndMemberId(meetingId, memberId);

        if (result.isPresent()) {
            meetingMemberWritePort.delete(result.get());
        } else {
            int currentCount = meetingMemberReadPort.countByMeetingId(meeting.getId());
            if (currentCount >= meeting.getMemberLimit()) throw new DomainException(MeetingMemberErrorCode.FULL_CAPACITY);

            meetingMemberWritePort.save(MeetingMember.createMeetingMember(meeting, member));
        }
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    public Meeting findMeetingByMeetingIdOrThrow(Long meetingId) {
        return meetingReadPort.findById(meetingId)
                .orElseThrow(() -> new DomainException(MeetingErrorCode.NOT_FOUND_MEETING));
    }
}
