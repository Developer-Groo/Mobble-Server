package com.mobble.mobbleserver.infrastructure.web.meeting;

import com.mobble.mobbleserver.application.meeting.port.provided.MeetingCreatePort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingDeletePort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingQueryPort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingUpdatePort;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.infrastructure.web.meeting.dto.request.MeetingRequestDto;
import com.mobble.mobbleserver.infrastructure.web.meeting.dto.request.MeetingUpdateRequestDto;
import com.mobble.mobbleserver.infrastructure.web.meeting.dto.response.MeetingResponseDto;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{club-id}/meetings")
public class MeetingAPI {

    private final MeetingCreatePort meetingCreatePort;
    private final MeetingQueryPort meetingQueryPort;
    private final MeetingUpdatePort meetingUpdatePort;
    private final MeetingDeletePort meetingDeletePort;

    @PreAuthorize("hasAnyAuthority('LEADER', 'MANAGER')")
    @PostMapping
    public ResponseEntity<MeetingResponseDto> createMeeting(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody MeetingRequestDto dto
    ) {
        Meeting meeting = meetingCreatePort.createMeeting(memberId, clubId, dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MeetingResponseDto.toDto(meeting));
    }

    @GetMapping
    public ResponseEntity<List<MeetingResponseDto>> findMeetingsByClubId(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @PathVariable("club-id") @Positive Long clubId
    ) {
        List<Meeting> meetings = meetingQueryPort.findMeetingsByClubId(clubId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(MeetingResponseDto.toDto(meetings));
    }

    @PreAuthorize("hasAnyAuthority('LEADER', 'MANAGER')")
    @PatchMapping("/{meeting-id}")
    public ResponseEntity<MeetingResponseDto> updateMeeting(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @PathVariable("club-id") @Positive Long clubId,
            @PathVariable("meeting-id") @Positive Long meetingId,
            @RequestBody MeetingUpdateRequestDto dto
    ) {
        Meeting meeting = meetingUpdatePort.updateMeeting(memberId, clubId, meetingId, dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(MeetingResponseDto.toDto(meeting));
    }

    @PreAuthorize("hasAnyAuthority('LEADER', 'MANAGER')")
    @DeleteMapping("/{meeting-id}")
    public ResponseEntity<Void> deleteMeeting(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @PathVariable("club-id") @Positive Long clubId,
            @PathVariable("meeting-id") @Positive Long meetingId
    ) {
        meetingDeletePort.deleteMeeting(memberId, clubId, meetingId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
