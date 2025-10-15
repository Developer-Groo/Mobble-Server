package com.mobble.mobbleserver.application.meetingMember.port.required;

import com.mobble.mobbleserver.domain.meetingMember.MeetingMember;

import java.util.List;
import java.util.Optional;

public interface MeetingMemberReadPort {

    Optional<MeetingMember> findMeetingMemberByMeetingIdAndMemberId(Long meetingId, Long memberId);

    boolean existsByMeeting_IdAndMember_Id(Long meetingId, Long memberId);

    List<MeetingMember> findByMeetingId(Long meetingId);

    int countByMeetingId(Long meetingId);
}
