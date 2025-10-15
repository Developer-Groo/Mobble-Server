package com.mobble.mobbleserver.refactor.meetingMember.service;

import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.meetingMember.dto.response.MeetingAttendanceResponseDto;
import com.mobble.mobbleserver.refactor.meetingMember.dto.response.MeetingMemberListResponseDto;
import com.mobble.mobbleserver.refactor.meetingMember.entity.MeetingMember;
import com.mobble.mobbleserver.refactor.meetingMember.repository.MeetingMemberRepository;
import com.mobble.mobbleserver.refactor.meetingMember.validator.MeetingMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingMemberService {

    private final MeetingMemberRepository meetingMemberRepository;
    private final MeetingMemberValidator meetingMemberValidator;

    private final MemberReadPort memberReadPort;
    private final MeetingReadPort meetingReadPort;

    @Transactional
    public MeetingAttendanceResponseDto attendMeeting(Long meetingId, Long memberId) {
        Meeting meeting = findMeetingByMeetingIdOrThrow(meetingId);
        Member member = findMemberByMemberIdOrThrow(memberId);

        Boolean isAttended = meetingMemberValidator.findMeetingByMeetingIdAndMemberId(meeting.getId(), member.getId())
                .map(attending -> {
                    meetingMemberRepository.delete(attending);
                    return false;
                })
                .orElseGet(() -> {
                    int currentCount = meetingMemberRepository.countByMeetingId(meeting.getId());

                    if (currentCount >= meeting.getMemberLimit()) throw new DomainException(MeetingMemberErrorCode.FULL_CAPACITY);

                    MeetingMember attendedMember = MeetingMember.createMeetingMember(meeting, member);
                    meetingMemberRepository.save(attendedMember);
                    return true;
                });

        return MeetingAttendanceResponseDto.toDto(meeting.getId(), isAttended);
    }

    public MeetingMemberListResponseDto getMeetingMembers(Long meetingId) {
        Meeting meeting = findMeetingByMeetingIdOrThrow(meetingId);

        List<MeetingMember> meetingMembers = meetingMemberValidator.findByMeetingId(meeting.getId());

        return MeetingMemberListResponseDto.toDto(meeting.getId(), meetingMembers);
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
