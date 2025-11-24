package com.mobble.mobbleserver.infrastructure.web.clubMember;

import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberJoinPort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberDeletePort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberQueryPort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberUpdatePort;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.request.UpdateClubMemberRoleDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.request.UpdateClubMemberStatusDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMemberResponseDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMemberRoleUpdateResultDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMemberUpsertResponseDto;
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
@RequestMapping("/api/clubs")
public class ClubMemberAPI {

    private final ClubMemberJoinPort clubMemberJoinPort;
    private final ClubMemberQueryPort clubMemberQueryPort;
    private final ClubMemberUpdatePort clubMemberUpdatePort;
    private final ClubMemberDeletePort clubMemberDeletePort;

    @PostMapping("/join/{club-id}")
    public ResponseEntity<ClubMemberResponseDto> join(
            @PathVariable("club-id") @Positive Long clubId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId

    ) {
        ClubMember join = clubMemberJoinPort.join(memberId, clubId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ClubMemberResponseDto.toDto(join));
    }

    @PatchMapping("/{club-id}/members/status")
    public ResponseEntity<ClubMemberUpsertResponseDto> updateClubMemberStatus(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody UpdateClubMemberStatusDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clubMemberUpdatePort.updateClubMemberJoinStatus(clubId, memberId, dto));
    }

    // Todo 클라이언트에서 기존 accessToken 제거 필요
    @PatchMapping("/{club-id}/members/role")
    public ResponseEntity<ClubMemberUpsertResponseDto> updateClubMemberRole(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody UpdateClubMemberRoleDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        ClubMemberRoleUpdateResultDto result = clubMemberUpdatePort.updateClubMemberRole(clubId, memberId, dto);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", "Bearer " + result.jwtToken())
                .body(result.clubMember());
    }

    @DeleteMapping("/{club-id}/members/withdraw")
    public ResponseEntity<ClubMemberUpsertResponseDto> leave(
            @PathVariable("club-id") @Positive Long clubId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        clubMemberDeletePort.leaveClub(memberId, clubId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{club-id}/members")
    public ResponseEntity<List<ClubMemberResponseDto>> findClubMembers(
            @PathVariable("club-id") @Positive Long clubId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clubMemberQueryPort.findClubMembers(clubId));
    }
}

