package com.mobble.mobbleserver.domain.meeting.repository;

import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

}
