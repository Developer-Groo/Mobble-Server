package com.mobble.mobbleserver.domain.like.articleLike.controller;

import com.mobble.mobbleserver.domain.like.articleLike.dto.response.ArticleLikeInfoResponseDto;
import com.mobble.mobbleserver.domain.like.articleLike.service.ArticleLikeService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/articles/{article-id}/likes")
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping
    public ResponseEntity<ArticleLikeInfoResponseDto> likeToggle(
            @PathVariable("article-id") @Positive Long articleId
//            @AuthenticationPrincipal @Positive Long member
    ) {
        Long memberId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleLikeService.toggleLike(articleId, memberId));
    }
}
