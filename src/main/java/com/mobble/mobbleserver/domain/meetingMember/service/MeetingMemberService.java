package com.mobble.mobbleserver.domain.meetingMember.service;

import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.validator.MeetingValidator;
import com.mobble.mobbleserver.domain.meetingMember.dto.response.MeetingAttendanceResponseDto;
import com.mobble.mobbleserver.domain.meetingMember.dto.response.MeetingMemberListResponseDto;
import com.mobble.mobbleserver.domain.meetingMember.entity.MeetingMember;
import com.mobble.mobbleserver.domain.meetingMember.repository.MeetingMemberRepository;
import com.mobble.mobbleserver.domain.meetingMember.validator.MeetingMemberValidator;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
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

                    if (currentCount >= meeting.getMemberLimit()) throw new IllegalArgumentException("모임 정원 초과");

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
