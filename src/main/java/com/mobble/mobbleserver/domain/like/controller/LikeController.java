package com.mobble.mobbleserver.domain.like.controller;

import com.mobble.mobbleserver.domain.like.articleLike.service.ArticleLikeService;
import com.mobble.mobbleserver.domain.like.dto.response.LikeToggleResponseDto;
import com.mobble.mobbleserver.domain.like.entity.LikeType;
import com.mobble.mobbleserver.domain.like.service.LikeDispatcherService;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeDispatcherService likeDispatcherService;
    private final ArticleLikeService articleLikeService;

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
        return switch (likeType) {
            case ARTICLE -> ResponseEntity.status(HttpStatus.OK)
                    .body(articleLikeService.getArticleLikedMembers(targetId));
            case CLUB, COMMENT -> throw new DomainException(LikeErrorCode.NOT_SUPPORTED_TYPE);
        };
    }
}
