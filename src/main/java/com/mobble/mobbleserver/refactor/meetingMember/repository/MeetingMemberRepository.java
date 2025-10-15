package com.mobble.mobbleserver.refactor.meetingMember.repository;

import com.mobble.mobbleserver.domain.meetingMember.MeetingMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {

    Optional<MeetingMember> findMeetingMemberByMeetingIdAndMemberId(Long meetingId, Long memberId);

    List<MeetingMember> findByMeetingId(Long meetingId);

    int countByMeetingId(Long meetingId);
}
