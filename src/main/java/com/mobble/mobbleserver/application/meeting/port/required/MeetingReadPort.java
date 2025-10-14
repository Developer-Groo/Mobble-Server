package com.mobble.mobbleserver.application.meeting.port.required;

import com.mobble.mobbleserver.domain.meeting.Meeting;

import java.util.List;

public interface MeetingReadPort {

    Meeting findById(Long meetingId);
    
    List<Meeting> findByClubMember_Club_Id(Long clubId);
}
