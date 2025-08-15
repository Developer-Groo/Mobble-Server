package com.mobble.mobbleserver.domain.club.club.controller;

import com.mobble.mobbleserver.domain.club.club.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.domain.club.club.dto.response.ClubResponseDto;
import com.mobble.mobbleserver.domain.club.club.service.ClubService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public ResponseEntity<ClubResponseDto> createClub(
            @RequestBody @Valid ClubRequestDto dto
    ) {
        Long memberId = 1L; // Todo: 임시 member id

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clubService.createClub(memberId, dto));
    }

    @GetMapping("/{club-id}")
    public ResponseEntity<ClubResponseDto> findClubById(
            @PathVariable("club-id") @Positive Long clubId
    ) {
        Long memberId = 1L; // Todo: 임시 member id

        return ResponseEntity.status(HttpStatus.OK)
                .body(clubService.findClubById(clubId, memberId));
    }

    @PatchMapping("/{club-id}")
    public ResponseEntity<ClubResponseDto> updateClub(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody @Valid ClubRequestDto dto
    ) {
        Long memberId = 1L; // Todo: 임시 member id

        return ResponseEntity.status(HttpStatus.OK)
                .body(clubService.updateClub(clubId, memberId, dto));
    }

    @DeleteMapping("/{club-id}")
    public ResponseEntity<Void> deleteClub(
            @PathVariable("club-id") @Positive Long clubId
    ){
        Long memberId = 1L; // Todo: 임시 member id
        clubService.deleteClub(clubId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
