package com.mobble.mobbleserver.application.meeting.port.provided;

import com.mobble.mobbleserver.application.meeting.command.UpdateMeetingCommand;
import com.mobble.mobbleserver.domain.meeting.Meeting;

public interface MeetingUpdatePort {

    Meeting updateMeeting(UpdateMeetingCommand command);
}
