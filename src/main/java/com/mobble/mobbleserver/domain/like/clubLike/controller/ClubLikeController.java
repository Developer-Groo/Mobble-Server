package com.mobble.mobbleserver.domain.like.clubLike.controller;

import com.mobble.mobbleserver.domain.like.clubLike.dto.response.ClubLikeResponseDto;
import com.mobble.mobbleserver.domain.like.clubLike.service.ClubLikeService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("clubs/{club-id}/likes")
public class ClubLikeController {

    private final ClubLikeService clubLikeService;

    @PostMapping
    public ResponseEntity<ClubLikeResponseDto> likeToggle(
            @PathVariable("club-id") @Positive Long clubId
    ) {
        Long memberId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clubLikeService.toggleLike(clubId, memberId));
    }
}
