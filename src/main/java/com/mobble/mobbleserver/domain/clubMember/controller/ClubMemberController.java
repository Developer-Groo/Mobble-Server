package com.mobble.mobbleserver.domain.clubMember.controller;

import com.mobble.mobbleserver.domain.clubMember.dto.request.UpdateClubMemberRoleDto;
import com.mobble.mobbleserver.domain.clubMember.dto.request.UpdateClubMemberStatusDto;
import com.mobble.mobbleserver.domain.clubMember.dto.response.ClubMemberResponseDto;
import com.mobble.mobbleserver.domain.clubMember.dto.response.ClubMemberUpsertResponseDto;
import com.mobble.mobbleserver.domain.clubMember.service.ClubMemberService;
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
@RequestMapping("/api/clubs")
public class ClubMemberController {

    private final ClubMemberService clubMemberService;

    @PostMapping("/join/{club-id}")
    public ResponseEntity<ClubMemberUpsertResponseDto> joinClub(
            @PathVariable("club-id") @Positive Long clubId
    ) {
        Long memberId = 2L; // Todo: 임시 member id

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clubMemberService.joinClub(memberId, clubId));
    }

    @DeleteMapping("/{club-id}/members/withdraw")
    public ResponseEntity<ClubMemberUpsertResponseDto> withdrawClub(
            @PathVariable("club-id") @Positive Long clubId
    ) {
        Long memberId = 2L; // Todo: 임시 member id

        clubMemberService.withdrawClub(memberId, clubId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping("/{club-id}/members/status")
    public ResponseEntity<ClubMemberUpsertResponseDto> updateClubMemberStatus(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody UpdateClubMemberStatusDto dto
    ) {
        Long loginedMemberId = 2L; // Todo: 임시 member id

        return ResponseEntity.status(HttpStatus.OK)
                .body(clubMemberService.updateClubMemberStatus(clubId, loginedMemberId, dto));
    }

    @PatchMapping("/{club-id}/members/role")
    public ResponseEntity<ClubMemberUpsertResponseDto> updateClubMemberRole(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody UpdateClubMemberRoleDto dto
    ) {
        Long loginedMemberId = 2L; // Todo: 임시 member id

        return ResponseEntity.ok(
                clubMemberService.updateClubMemberRole(clubId, loginedMemberId, dto)
        );
    }
