package com.mobble.mobbleserver.domain.meetingMember.validator;

import com.mobble.mobbleserver.domain.meetingMember.repository.MeetingMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingMemberValidator {

    private final MeetingMemberRepository meetingMemberRepository;
}
