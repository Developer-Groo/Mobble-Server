package com.mobble.mobbleserver.infrastructure.web.clubMember;

import com.mobble.mobbleserver.application.clubMember.command.UpdateRoleCommand;
import com.mobble.mobbleserver.application.clubMember.command.UpdateStatusCommand;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberJoinPort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberLeavePort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberQueryPort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberUpdatePort;
import com.mobble.mobbleserver.application.clubMember.result.ClubMembersResult;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.request.UpdateClubMemberRoleDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.request.UpdateClubMemberStatusDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMemberResponseDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMembersResponseDto;
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
@RequestMapping("/api/clubs")
public class ClubMemberAPI {

    private final ClubMemberJoinPort clubMemberJoinPort;
    private final ClubMemberQueryPort clubMemberQueryPort;
    private final ClubMemberUpdatePort clubMemberUpdatePort;
    private final ClubMemberLeavePort clubMemberLeavePort;

    @PostMapping("/join/{club-id}")
    public ResponseEntity<ClubMemberResponseDto> join(
            @PathVariable("club-id") @Positive Long clubId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId

    ) {
        ClubMember clubMember = clubMemberJoinPort.join(memberId, clubId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ClubMemberResponseDto.toDto(clubMember));
    }

    @PatchMapping("/{club-id}/members/status")
    public ResponseEntity<ClubMemberResponseDto> updateStatus(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody UpdateClubMemberStatusDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        UpdateStatusCommand command = UpdateStatusCommand.create(clubId, memberId, dto.targetMemberId(), dto.targetStatus());
        ClubMember clubMember = clubMemberUpdatePort.updateJoinStatus(command);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ClubMemberResponseDto.toDto(clubMember));
    }

    @PatchMapping("/{club-id}/members/role")
    public ResponseEntity<ClubMemberResponseDto> updateRole(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody UpdateClubMemberRoleDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        UpdateRoleCommand command = UpdateRoleCommand.create(clubId, memberId, dto.targetMemberId(), dto.newRole());
        ClubMember clubMember = clubMemberUpdatePort.updateRole(command);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ClubMemberResponseDto.toDto(clubMember));
    }

    @DeleteMapping("/{club-id}/members/leave")
    public ResponseEntity<Void> leave(
            @PathVariable("club-id") @Positive Long clubId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        clubMemberLeavePort.leave(memberId, clubId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{club-id}/members")
    public ResponseEntity<ClubMembersResponseDto> getClubMembers(
            @PathVariable("club-id") @Positive Long clubId
    ) {
        ClubMembersResult result = clubMemberQueryPort.getClubMembers(clubId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ClubMembersResponseDto.toDto(result));
    }
}

