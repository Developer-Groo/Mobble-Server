package com.mobble.mobbleserver.domain.meeting.controller;

import com.mobble.mobbleserver.domain.meeting.dto.request.MeetingRequestDto;
import com.mobble.mobbleserver.domain.meeting.dto.response.MeetingResponseDto;
import com.mobble.mobbleserver.domain.meeting.service.MeetingService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/api/clubs/{club-id}/meetings")
    public ResponseEntity<MeetingResponseDto> createMeeting(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody MeetingRequestDto dto
    ) {
        Long memberId = 1L;

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(meetingService.createMeeting(memberId, clubId, dto));
    }

    @GetMapping("/api/clubs/{club-id}/meetings")
    public ResponseEntity<List<MeetingResponseDto>> findMeetingsByClubId(
            @PathVariable("club-id") @Positive Long clubId
    ) {
        Long memberId = 1L;

        return ResponseEntity.status(HttpStatus.OK)
                .body(meetingService.findMeetingsByClubId(memberId, clubId));
    }

    @GetMapping("/api/meetings/{meeting-id}")
    public ResponseEntity<MeetingResponseDto> findMeeting(
            @PathVariable("meeting-id") @Positive Long meetingId
    ) {
        Long memberId = 1L;

        return ResponseEntity.status(HttpStatus.OK)
                .body(meetingService.findMeetingById(meetingId, memberId));
    }
}
