package com.mobble.mobbleserver.domain.article.controller;

import com.mobble.mobbleserver.domain.article.dto.request.ArticleRequestDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleResponseDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleSummaryResponseDto;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.article.service.ArticleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/clubs/{club-id}/articles")
    public ResponseEntity<ArticleResponseDto> createArticle(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody @Valid ArticleRequestDto dto
    ) {
        Long memberId = 1L; // Todo: 임시 member id

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.createArticle(memberId, clubId, dto));
    }

    @GetMapping("/clubs/{club-id}/articles")
    public ResponseEntity<List<ArticleSummaryResponseDto>> findArticlesByClubId(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestParam(value = "articleType", required = false) ArticleType articleType
    ) {
        Long memberId = 1L; // Todo: 임시 member id

        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.findArticlesByClubId(clubId, articleType, memberId));
    }

    @GetMapping("/articles/{article-id}")
    public ResponseEntity<ArticleResponseDto> findArticle(
            @PathVariable("article-id") @Positive Long articleId
    ) {
        Long memberId = 1L; // Todo: 임시 member id

        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.findArticleById(articleId, memberId));
    }

    @PatchMapping("/articles/{article-id}")
    public ResponseEntity<ArticleResponseDto> updateArticle(
            @PathVariable("article-id") @Positive Long articleId,
            @RequestBody @Valid ArticleRequestDto dto
    ) {
        Long memberId = 3L; // Todo: 임시 member id

        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.updateArticle(articleId, memberId, dto));
    }

    @DeleteMapping("/articles/{article-id}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable("article-id") @Positive Long articleId
    ) {
        Long memberId = 1L; // Todo: 임시 member id
        articleService.deleteArticle(articleId, memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
