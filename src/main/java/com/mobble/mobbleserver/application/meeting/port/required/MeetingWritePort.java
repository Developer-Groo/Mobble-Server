package com.mobble.mobbleserver.application.meeting.port.required;

import com.mobble.mobbleserver.domain.meeting.Meeting;

public interface MeetingWritePort {

    Meeting save(Meeting meeting);

    void delete(Meeting meeting);
}
