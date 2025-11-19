package com.mobble.mobbleserver.infrastructure.web.club;

import com.mobble.mobbleserver.application.club.command.CreateClubCommand;
import com.mobble.mobbleserver.application.club.port.provided.ClubCreatePort;
import com.mobble.mobbleserver.application.club.port.provided.ClubDeletePort;
import com.mobble.mobbleserver.application.club.port.provided.ClubQueryPort;
import com.mobble.mobbleserver.application.club.port.provided.ClubUpdatePort;
import com.mobble.mobbleserver.application.club.result.ClubResult;
import com.mobble.mobbleserver.infrastructure.web.club.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.request.ClubSearchRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.response.ClubResponseDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.response.ClubSummaryDto;
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
    public ResponseEntity<ClubResponseDto> create(
            @RequestBody @Valid ClubRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        CreateClubCommand command = CreateClubCommand.create(
                memberId,
                dto.name(),
                dto.description(),
                dto.isAutoJoin(),
                dto.category(),
                dto.ageGroup(),
                dto.address1(),
                dto.address2(),
                dto.city(),
                dto.district(),
                dto.latitude(),
                dto.longitude(),
                dto.mainImageId()
        );
        ClubResult result = clubCreatePort.create(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ClubResponseDto.toDto(result));
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
                .body(clubUpdatePort.update(clubId, memberId, dto));
    }

    @DeleteMapping("/{club-id}")
    public ResponseEntity<Void> deleteClub(
            @PathVariable("club-id") @Positive Long clubId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ){
        clubDeletePort.delete(clubId, memberId);

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
