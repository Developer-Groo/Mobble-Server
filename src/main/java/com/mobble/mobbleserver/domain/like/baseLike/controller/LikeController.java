package com.mobble.mobbleserver.domain.like.baseLike.controller;

import com.mobble.mobbleserver.domain.like.baseLike.dto.response.LikeToggleResponseDto;
import com.mobble.mobbleserver.domain.like.baseLike.entity.LikeType;
import com.mobble.mobbleserver.domain.like.baseLike.service.LikeDispatcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeDispatcherService likeDispatcherService;

    @PostMapping
    public ResponseEntity<LikeToggleResponseDto> toggleLike(
            @RequestParam LikeType likeType,
            @RequestParam Long targetId
    ) {
        Long memberId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(likeDispatcherService.toggleLike(likeType, targetId, memberId));
    }

    @GetMapping("/members")
    public ResponseEntity<?> getMemberList(
            @RequestParam LikeType likeType,
            @RequestParam Long targetId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(likeDispatcherService.getMemberList(likeType, targetId));
    }
}
