package com.mobble.mobbleserver.application.meetingMember.port.provided;

import com.mobble.mobbleserver.application.meetingMember.result.MeetingMemberResult;

public interface MeetingMemberQueryPort {

    MeetingMemberResult getMeetingMembers(Long meetingId);
}
