package com.mobble.mobbleserver.application.meetingMember.service;

import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.meeting.error.MeetingBusinessError;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.application.meetingMember.port.provided.AttendMeetingPort;
import com.mobble.mobbleserver.application.member.error.MemberBusinessError;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingMemberModifyService implements AttendMeetingPort {

    private final MeetingReadPort meetingReadPort;
    private final MemberReadPort memberReadPort;

    @Override
    public void toggleAttend(Long meetingId, Long memberId) {
        // Todo: 검증 조건 추가 필요
        Meeting meeting = assertMeetingByMeetingId(meetingId);
        Member member = assertMemberByMemberId(memberId);

        if (meeting.hasAttendee(memberId)) {
            meeting.cancelAttend(member.getId());
        } else {
            meeting.attend(member);
        }
    }

    /* ==== Private Helper ==== */
    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new BusinessException(MemberBusinessError.NOT_FOUND));
    }

    public Meeting assertMeetingByMeetingId(Long meetingId) {
        return meetingReadPort.findById(meetingId)
                .orElseThrow(() -> new BusinessException(MeetingBusinessError.NOT_FOUND));
    }
}
