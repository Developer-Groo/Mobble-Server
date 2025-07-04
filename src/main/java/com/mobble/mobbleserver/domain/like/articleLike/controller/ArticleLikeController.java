package com.mobble.mobbleserver.domain.like.articleLike.controller;

import com.mobble.mobbleserver.domain.like.articleLike.dto.response.ArticleLikeToggleResponseDto;
import com.mobble.mobbleserver.domain.like.articleLike.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles/{article-id}/likes")
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping
    public ResponseEntity<ArticleLikeToggleResponseDto> likeToggle(
            @PathVariable("article-id") Long articleId
//            @AuthenticationPrincipal @Positive Long member
    ) {
        Long memberId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleLikeService.toggleLike(articleId, memberId));
    }
}
