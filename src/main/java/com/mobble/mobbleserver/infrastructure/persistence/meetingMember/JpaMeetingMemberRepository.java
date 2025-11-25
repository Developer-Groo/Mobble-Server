package com.mobble.mobbleserver.infrastructure.persistence.meetingMember;

import com.mobble.mobbleserver.domain.meeting.MeetingMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaMeetingMemberRepository extends JpaRepository<MeetingMember, Long> {

    boolean existsByMeeting_IdAndMember_Id(Long meetingId, Long memberId);

    List<MeetingMember> findMeetingIdsByMemberIdAndMeeting_Club_Id(Long meetingId, Long clubId);

    List<MeetingMember> findByMeetingId(Long meetingId);
}
