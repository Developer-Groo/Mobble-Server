package com.mobble.mobbleserver.domain.meeting.controller;

import com.mobble.mobbleserver.account.auth.principal.AuthMember;
import com.mobble.mobbleserver.domain.meeting.dto.request.MeetingRequestDto;
import com.mobble.mobbleserver.domain.meeting.dto.request.MeetingUpdateRequestDto;
import com.mobble.mobbleserver.domain.meeting.dto.response.MeetingResponseDto;
import com.mobble.mobbleserver.domain.meeting.service.MeetingService;
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
@RequestMapping("/api/clubs/{club-id}/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
    public ResponseEntity<MeetingResponseDto> createMeeting(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody MeetingRequestDto dto
    ) {
        Long memberId = authMember.memberId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(meetingService.createMeeting(memberId, clubId, dto));
    }

    @GetMapping
    public ResponseEntity<List<MeetingResponseDto>> findMeetingsByClubId(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable("club-id") @Positive Long clubId
    ) {
        Long memberId = authMember.memberId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(meetingService.findMeetingsByClubId(memberId, clubId));
    }

    @PatchMapping("/{meeting-id}")
    public ResponseEntity<MeetingResponseDto> updateMeeting(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @PathVariable("meeting-id") @Positive Long meetingId,
            @RequestBody MeetingUpdateRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(meetingService.updateMeeting(memberId, meetingId, dto));
    }

    @DeleteMapping("/{meeting-id}")
    public ResponseEntity<Void> deleteMeeting(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @PathVariable("meeting-id") @Positive Long meetingId
    ) {
        meetingService.deleteMeeting(memberId, meetingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
