package com.mobble.mobbleserver.domain.meetingMember.repository;

import com.mobble.mobbleserver.domain.meetingMember.entity.MeetingMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {

    Optional<MeetingMember> findMeetingMemberByMeetingIdAndMemberId(Long meetingId, Long memberId);
}
