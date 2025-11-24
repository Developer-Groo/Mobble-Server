package com.mobble.mobbleserver.infrastructure.web.meetingMember;

import com.mobble.mobbleserver.application.meetingMember.port.provided.AttendMeetingPort;
import com.mobble.mobbleserver.application.meetingMember.port.provided.MeetingMemberQueryPort;
import com.mobble.mobbleserver.domain.meeting.MeetingMember;
import com.mobble.mobbleserver.infrastructure.web.meetingMember.dto.response.MeetingAttendanceResponseDto;
import com.mobble.mobbleserver.infrastructure.web.meetingMember.dto.response.MeetingMemberListResponseDto;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings/{meeting-id}")
public class MeetingMemberAPI {

    private final AttendMeetingPort attendMeetingPort;
    private final MeetingMemberQueryPort meetingMemberQueryPort;

    @PostMapping
    public ResponseEntity<Void> toggleAttendanceMeeting(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @PathVariable("meeting-id") @Positive Long meetingId
    ) {
        attendMeetingPort.attendMeeting(meetingId, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    public ResponseEntity<MeetingAttendanceResponseDto> getIsAttended(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @PathVariable("meeting-id") @Positive Long meetingId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(meetingMemberQueryPort.getIsAttended(memberId, meetingId));
    }

    @GetMapping("/members")
    public ResponseEntity<MeetingMemberListResponseDto> getMeetingMembers(
            @PathVariable("meeting-id") @Positive Long meetingId
    ) {
        List<MeetingMember> meetingMembers = meetingMemberQueryPort.getMeetingMembers(meetingId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(MeetingMemberListResponseDto.toDto(meetingMembers));
    }
}
