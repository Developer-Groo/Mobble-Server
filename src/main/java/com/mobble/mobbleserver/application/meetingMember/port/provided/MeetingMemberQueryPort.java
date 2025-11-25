package com.mobble.mobbleserver.application.meetingMember.port.provided;

import com.mobble.mobbleserver.domain.meeting.MeetingMember;

import java.util.List;

public interface MeetingMemberQueryPort {

    List<Long> getIsAttended(Long memberId, Long clubId);

    List<MeetingMember> getMeetingMembers(Long meetingId);
}
