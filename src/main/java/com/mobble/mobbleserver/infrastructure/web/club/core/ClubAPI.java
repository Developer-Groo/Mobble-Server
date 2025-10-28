package com.mobble.mobbleserver.infrastructure.web.club.core;

import com.mobble.mobbleserver.application.club.core.port.provided.ClubCreatePort;
import com.mobble.mobbleserver.application.club.core.port.provided.ClubDeletePort;
import com.mobble.mobbleserver.application.club.core.port.provided.ClubQueryPort;
import com.mobble.mobbleserver.application.club.core.port.provided.ClubUpdatePort;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.request.ClubSearchRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.response.ClubResponseDto;
import com.mobble.mobbleserver.infrastructure.web.club.core.dto.response.ClubSummaryDto;
import jakarta.validation.Valid;
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
public class ClubAPI {

    private final ClubCreatePort clubCreatePort;
    private final ClubQueryPort clubQueryPort;
    private final ClubUpdatePort clubUpdatePort;
    private final ClubDeletePort clubDeletePort;

    @PostMapping
    public ResponseEntity<ClubResponseDto> createClub(
            @RequestBody @Valid ClubRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clubCreatePort.createClub(memberId, dto));
    }

    @GetMapping("/{club-id}")
    public ResponseEntity<ClubResponseDto> findClubById(
            @PathVariable("club-id") @Positive Long clubId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clubQueryPort.findClubById(clubId, memberId));
    }

    @PatchMapping("/{club-id}")
    public ResponseEntity<ClubResponseDto> updateClub(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody @Valid ClubRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clubUpdatePort.updateClub(clubId, memberId, dto));
    }

    @DeleteMapping("/{club-id}")
    public ResponseEntity<Void> deleteClub(
            @PathVariable("club-id") @Positive Long clubId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ){
        clubDeletePort.deleteClub(clubId, memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClubSummaryDto>> searchClubs(
            @Validated ClubSearchRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clubQueryPort.searchClubs(dto, memberId));
    }
}
