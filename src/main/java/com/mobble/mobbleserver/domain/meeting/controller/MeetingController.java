package com.mobble.mobbleserver.domain.meeting.controller;

import com.mobble.mobbleserver.domain.meeting.dto.request.MeetingRequestDto;
import com.mobble.mobbleserver.domain.meeting.dto.response.MeetingResponseDto;
import com.mobble.mobbleserver.domain.meeting.service.MeetingService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
