package com.mobble.mobbleserver.domain.meetingMember.validator;

import com.mobble.mobbleserver.domain.meetingMember.entity.MeetingMember;
import com.mobble.mobbleserver.domain.meetingMember.repository.MeetingMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MeetingMemberValidator {

    private final MeetingMemberRepository meetingMemberRepository;

    public Optional<MeetingMember> findMeetingByMeetingIdAndMemberId(Long meetingId, Long memberId) {
        return meetingMemberRepository.findMeetingMemberByMeetingIdAndMemberId(meetingId, memberId);
    }
}
