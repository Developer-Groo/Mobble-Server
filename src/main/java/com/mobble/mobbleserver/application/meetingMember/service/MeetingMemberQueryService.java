package com.mobble.mobbleserver.application.meetingMember.service;

import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.application.meetingMember.port.provided.MeetingMemberQueryPort;
import com.mobble.mobbleserver.application.meetingMember.port.required.MeetingMemberReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meeting.MeetingMember;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.infrastructure.web.meetingMember.dto.response.MeetingAttendanceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingMemberQueryService implements MeetingMemberQueryPort {

    private final MeetingMemberReadPort meetingMemberReadPort;
    private final MemberReadPort memberReadPort;
    private final MeetingReadPort meetingReadPort;

    @Override
    public MeetingAttendanceResponseDto getIsAttended(Long meetingId, Long memberId) {
        Meeting meeting = findMeetingByMeetingIdOrThrow(meetingId);
        Member member = findMemberByMemberIdOrThrow(memberId);

        boolean isAttended = meetingMemberReadPort.existsByMeeting_IdAndMember_Id(meeting.getId(), member.getId());

        return MeetingAttendanceResponseDto.toDto(meeting.getId(), isAttended);
    }

    @Override
    public List<MeetingMember> getMeetingMembers(Long meetingId) {
        Meeting meeting = findMeetingByMeetingIdOrThrow(meetingId);

        return meetingMemberReadPort.findByMeetingId(meeting.getId());
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
