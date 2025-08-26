package com.mobble.mobbleserver.domain.meetingMember.service;

import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.repository.MeetingRepository;
import com.mobble.mobbleserver.domain.meetingMember.dto.response.MeetingAttendanceResponseDto;
import com.mobble.mobbleserver.domain.meetingMember.entity.MeetingMember;
import com.mobble.mobbleserver.domain.meetingMember.repository.MeetingMemberRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingMemberService {

    private final MeetingMemberRepository meetingMemberRepository;
    private final MeetingRepository meetingRepository;
    private final MemberValidator memberValidator;

    @Transactional
    public MeetingAttendanceResponseDto attendMeeting(Long meetingId, Long memberId) {
        Meeting meeting = findMeetingById(meetingId);
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        Boolean isAttended = findMeetingByMeetingIdAndMemberId(meeting.getId(), member.getId())
                .map(attending -> {
                    meetingMemberRepository.delete(attending);
                    return false;
                })
                .orElseGet(() -> {
                    MeetingMember attendedMember = MeetingMember.createMeetingMember(meeting, member);
                    meetingMemberRepository.save(attendedMember);
                    return true;
                });

        return MeetingAttendanceResponseDto.toDto(meeting.getId(), isAttended);
    }


    private Meeting findMeetingById(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException(""));
    }

    private Optional<MeetingMember> findMeetingByMeetingIdAndMemberId(Long meetingId, Long memberId) {
        return meetingMemberRepository.findMeetingMemberByMeetingIdAndMemberId(meetingId, memberId);
    }
}
