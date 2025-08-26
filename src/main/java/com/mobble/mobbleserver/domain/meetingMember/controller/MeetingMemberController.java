package com.mobble.mobbleserver.domain.meetingMember.controller;

import com.mobble.mobbleserver.domain.meetingMember.dto.response.MeetingAttendanceResponseDto;
import com.mobble.mobbleserver.domain.meetingMember.service.MeetingMemberService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings/{meeting-id}")
public class MeetingMemberController {

    private final MeetingMemberService meetingMemberService;

    @PostMapping("/attend")
    public ResponseEntity<MeetingAttendanceResponseDto> toggleAttendanceMeeting(
            @PathVariable("meeting-id") @Positive Long meetingId
    ) {
        Long memberId = 1L;
        return ResponseEntity.status(HttpStatus.OK)
                .body(meetingMemberService.attendMeeting(meetingId, memberId));
    }
}
