package com.mobble.mobbleserver.application.meetingMember.port.provided;

import com.mobble.mobbleserver.domain.member.Member;

import java.util.List;

public interface MeetingMemberQueryPort {

    List<Long> getIsAttended(Long memberId, Long clubId);

    List<Member> getMeetingMembers(Long meetingId);
}
