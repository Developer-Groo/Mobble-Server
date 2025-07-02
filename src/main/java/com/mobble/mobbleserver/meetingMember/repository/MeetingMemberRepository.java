package com.mobble.mobbleserver.meetingMember.repository;

import com.mobble.mobbleserver.meetingMember.entity.MeetingMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {

}
