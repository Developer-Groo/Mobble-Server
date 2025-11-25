package com.mobble.mobbleserver.application.meeting.port.required;

import com.mobble.mobbleserver.domain.meeting.Meeting;

import java.util.List;

public interface MeetingWritePort {

    Meeting save(Meeting meeting);

    void delete(Meeting meeting);

    void deleteAll(List<Meeting> meetings);
}
