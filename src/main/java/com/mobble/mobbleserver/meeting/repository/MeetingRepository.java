package com.mobble.mobbleserver.meeting.repository;

import com.mobble.mobbleserver.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

}
