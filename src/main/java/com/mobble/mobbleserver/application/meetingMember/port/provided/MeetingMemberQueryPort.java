package com.mobble.mobbleserver.application.meetingMember.port.provided;

import com.mobble.mobbleserver.application.meetingMember.response.MeetingMemberResult;

import java.util.List;

public interface MeetingMemberQueryPort {

    List<Long> getIsAttended(Long memberId, Long clubId);

    MeetingMemberResult getMeetingMembers(Long meetingId);
}
