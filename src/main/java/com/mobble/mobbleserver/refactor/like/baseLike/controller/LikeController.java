package com.mobble.mobbleserver.refactor.like.baseLike.controller;

import com.mobble.mobbleserver.refactor.like.baseLike.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.refactor.like.baseLike.dto.response.LikeToggleResponseDto;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.refactor.like.baseLike.service.LikeDispatcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeDispatcherService likeDispatcherService;

    @PostMapping
    public ResponseEntity<LikeToggleResponseDto> toggleLike(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @RequestParam LikeType likeType,
            @RequestParam Long targetId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(likeDispatcherService.toggleLike(likeType, targetId, memberId));
    }

    @GetMapping("/members")
    public ResponseEntity<LikeMemberListResponseDto> getMemberList(
            @RequestParam LikeType likeType,
            @RequestParam Long targetId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(likeDispatcherService.getMemberList(likeType, targetId));
    }
}
