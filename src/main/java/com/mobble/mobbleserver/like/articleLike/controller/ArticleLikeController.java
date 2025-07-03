package com.mobble.mobbleserver.like.articleLike.controller;

import com.mobble.mobbleserver.like.articleLike.dto.response.ArticleLikeCountResponseDto;
import com.mobble.mobbleserver.like.articleLike.dto.response.ArticleLikeMemberResponseDto;
import com.mobble.mobbleserver.like.articleLike.dto.response.ArticleLikeToggleResponseDto;
import com.mobble.mobbleserver.like.articleLike.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles/{article-id}/likes")
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping
    public ResponseEntity<ArticleLikeToggleResponseDto> like(
            @PathVariable("article-id") Long articleId
//            @AuthenticationPrincipal @Positive Long member
    ) {
        Long memberId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleLikeService.toggleLike(articleId, memberId));
    }

    @GetMapping("/count")
    public ResponseEntity<ArticleLikeCountResponseDto> getLikeCount(
            @PathVariable("article-id") Long articleId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleLikeService.getArticleLikeCount(articleId));
    }

    @GetMapping("/members")
    public ResponseEntity<List<ArticleLikeMemberResponseDto>> getArticleLikedMembers(
            @PathVariable("article-id") Long articleId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleLikeService.getArticleLikedMembers(articleId));
    }
}
