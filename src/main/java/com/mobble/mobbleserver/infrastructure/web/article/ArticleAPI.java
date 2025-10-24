package com.mobble.mobbleserver.infrastructure.web.article;

import com.mobble.mobbleserver.application.article.port.provided.ArticleCreatePort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleDeletePort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleQueryPort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleUpdatePort;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.infrastructure.web.article.dto.request.ArticleRequestDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleResponseDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleSummaryResponseDto;
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
public class ArticleAPI {

    private final ArticleCreatePort articleCreatePort;
    private final ArticleQueryPort articleQueryPort;
    private final ArticleUpdatePort articleUpdatePort;
    private final ArticleDeletePort articleDeletePort;

    @PostMapping("/clubs/{club-id}/articles")
    public ResponseEntity<ArticleResponseDto> createArticle(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody @Valid ArticleRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleCreatePort.createArticle(memberId, clubId, dto));
    }

    @GetMapping("/clubs/{club-id}/articles")
    public ResponseEntity<List<ArticleSummaryResponseDto>> findArticlesByClubId(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestParam(value = "articleType", required = false) ArticleType articleType,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleQueryPort.findArticlesByClubId(clubId, articleType, memberId));
    }

    @GetMapping("/articles/{article-id}")
    public ResponseEntity<ArticleResponseDto> findArticle(
            @PathVariable("article-id") @Positive Long articleId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleQueryPort.findArticleById(articleId, memberId));
    }

    @PatchMapping("/articles/{article-id}")
    public ResponseEntity<ArticleResponseDto> updateArticle(
            @PathVariable("article-id") @Positive Long articleId,
            @RequestBody @Valid ArticleRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleUpdatePort.updateArticle(articleId, memberId, dto));
    }

    @DeleteMapping("/articles/{article-id}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable("article-id") @Positive Long articleId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        articleDeletePort.deleteArticle(articleId, memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
