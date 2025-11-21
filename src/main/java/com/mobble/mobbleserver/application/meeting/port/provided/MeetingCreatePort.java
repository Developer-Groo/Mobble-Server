package com.mobble.mobbleserver.application.meeting.port.provided;

import com.mobble.mobbleserver.application.meeting.command.request.CreateMeetingCommand;
import com.mobble.mobbleserver.domain.meeting.Meeting;

public interface MeetingCreatePort {

    Meeting createMeeting(CreateMeetingCommand command);
}
