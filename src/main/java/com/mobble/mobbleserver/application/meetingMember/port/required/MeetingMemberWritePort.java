package com.mobble.mobbleserver.application.meetingMember.port.required;

import com.mobble.mobbleserver.domain.meeting.MeetingMember;

public interface MeetingMemberWritePort {

    MeetingMember save(MeetingMember meetingMember);

    void delete(MeetingMember meetingMember);
}
