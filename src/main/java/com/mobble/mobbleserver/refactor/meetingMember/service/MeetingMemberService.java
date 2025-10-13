package com.mobble.mobbleserver.refactor.meetingMember.service;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingMemberErrorCode;
import com.mobble.mobbleserver.refactor.meeting.entity.Meeting;
import com.mobble.mobbleserver.refactor.meeting.validator.MeetingValidator;
import com.mobble.mobbleserver.refactor.meetingMember.dto.response.MeetingAttendanceResponseDto;
import com.mobble.mobbleserver.refactor.meetingMember.dto.response.MeetingMemberListResponseDto;
import com.mobble.mobbleserver.refactor.meetingMember.entity.MeetingMember;
import com.mobble.mobbleserver.refactor.meetingMember.repository.MeetingMemberRepository;
import com.mobble.mobbleserver.refactor.meetingMember.validator.MeetingMemberValidator;
import com.mobble.mobbleserver.refactor.member.entity.Member;
import com.mobble.mobbleserver.refactor.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingMemberService {

    private final MeetingMemberRepository meetingMemberRepository;
    private final MeetingValidator meetingValidator;
    private final MeetingMemberValidator meetingMemberValidator;
    private final MemberValidator memberValidator;

    @Transactional
    public MeetingAttendanceResponseDto attendMeeting(Long meetingId, Long memberId) {
        Meeting meeting = meetingValidator.findMeetingByMeetingIdOrThrow(meetingId);
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

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
        Meeting meeting = meetingValidator.findMeetingByMeetingIdOrThrow(meetingId);

        List<MeetingMember> meetingMembers = meetingMemberValidator.findByMeetingId(meeting.getId());

        return MeetingMemberListResponseDto.toDto(meeting.getId(), meetingMembers);
    }
}
