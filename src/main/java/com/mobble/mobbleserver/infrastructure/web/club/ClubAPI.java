package com.mobble.mobbleserver.infrastructure.web.club;

import com.mobble.mobbleserver.application.club.command.CreateClubCommand;
import com.mobble.mobbleserver.application.club.command.UpdateClubCommand;
import com.mobble.mobbleserver.application.club.port.provided.ClubCreatePort;
import com.mobble.mobbleserver.application.club.port.provided.ClubDeletePort;
import com.mobble.mobbleserver.application.club.port.provided.ClubQueryPort;
import com.mobble.mobbleserver.application.club.port.provided.ClubUpdatePort;
import com.mobble.mobbleserver.application.club.result.ClubResult;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.infrastructure.web.club.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.response.ClubDetailResponseDto;
import com.mobble.mobbleserver.infrastructure.web.club.dto.response.ClubResponseDto;
import jakarta.validation.Valid;
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
public class ClubAPI {

    private final ClubCreatePort clubCreatePort;
    private final ClubUpdatePort clubUpdatePort;
    private final ClubDeletePort clubDeletePort;
    private final ClubQueryPort clubQueryPort;

    @PostMapping
    public ResponseEntity<ClubResponseDto> create(
            @RequestBody @Valid ClubRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        CreateClubCommand command = dto.toCreateCommand(memberId);
        Club result = clubCreatePort.create(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ClubResponseDto.toDto(result));
    }

    @PatchMapping("/{club-id}")
    public ResponseEntity<ClubResponseDto> update(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody @Valid ClubRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        UpdateClubCommand command = dto.toUpdateCommand(clubId, memberId);
        Club result = clubUpdatePort.update(command);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ClubResponseDto.toDto(result));
    }

    @DeleteMapping("/{club-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("club-id") @Positive Long clubId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ){
        clubDeletePort.delete(clubId, memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{club-id}")
    public ResponseEntity<ClubDetailResponseDto> findClubById(
            @PathVariable("club-id") @Positive Long clubId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        ClubResult result = clubQueryPort.getClub(clubId, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ClubDetailResponseDto.create(result));
    }
}
