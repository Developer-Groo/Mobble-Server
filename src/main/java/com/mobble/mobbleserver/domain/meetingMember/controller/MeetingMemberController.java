package com.mobble.mobbleserver.domain.meetingMember.controller;

import com.mobble.mobbleserver.account.auth.principal.AuthMember;
import com.mobble.mobbleserver.domain.meetingMember.dto.response.MeetingAttendanceResponseDto;
import com.mobble.mobbleserver.domain.meetingMember.dto.response.MeetingMemberListResponseDto;
import com.mobble.mobbleserver.domain.meetingMember.service.MeetingMemberService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings/{meeting-id}/members")
public class MeetingMemberController {

    private final MeetingMemberService meetingMemberService;

    @PostMapping
    public ResponseEntity<MeetingAttendanceResponseDto> toggleAttendanceMeeting(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable("meeting-id") @Positive Long meetingId
    ) {
        Long memberId = authMember.memberId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(meetingMemberService.attendMeeting(meetingId, memberId));
    }

    @GetMapping
    public ResponseEntity<MeetingMemberListResponseDto> getMeetingMembers(
            @PathVariable("meeting-id") @Positive Long meetingId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(meetingMemberService.getMeetingMembers(meetingId));
    }
}
