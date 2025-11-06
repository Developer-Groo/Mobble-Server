package com.mobble.mobbleserver.infrastructure.web.article;

import com.mobble.mobbleserver.application.article.command.request.CreateArticleCommand;
import com.mobble.mobbleserver.application.article.command.request.UpdateArticleCommand;
import com.mobble.mobbleserver.application.article.port.provided.ArticleCreatePort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleDeletePort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleQueryPort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleUpdatePort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.infrastructure.web.article.dto.request.ArticleCreateRequestDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.request.ArticleUpdateRequestDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleResponseDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticlePreviewResponseDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleUpdatedResponseDto;
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
@RequestMapping("api/clubs/{club-id}/articles")
public class ArticleAPI {

    private final ArticleCreatePort articleCreatePort;
    private final ArticleQueryPort articleQueryPort;
    private final ArticleUpdatePort articleUpdatePort;
    private final ArticleDeletePort articleDeletePort;

    @PostMapping
    public ResponseEntity<ArticleResponseDto> createArticle(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody @Valid ArticleCreateRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        CreateArticleCommand command = CreateArticleCommand.create(memberId, clubId, dto.articleType(), dto.title(), dto.content());
        Article article = articleCreatePort.createArticle(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ArticleResponseDto.toDto(article));
    }

    @GetMapping
    public ResponseEntity<List<ArticlePreviewResponseDto>> findArticlesPreview(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestParam(value = "articleType", required = false) ArticleType articleType,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        List<ArticlePreviewResponseDto> articlesByClubId = articleQueryPort.findArticlesByClubId(clubId, articleType, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(articleQueryPort.findArticlesByClubId(clubId, articleType, memberId));
    }

    @GetMapping("/{article-id}")
    public ResponseEntity<ArticleResponseDto> findArticleDetail(
            @PathVariable("club-id") @Positive Long clubId,
            @PathVariable("article-id") @Positive Long articleId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleQueryPort.findArticleById(articleId, memberId));
    }

    @PatchMapping("/{article-id}")
    public ResponseEntity<ArticleUpdatedResponseDto> updateArticle(
            @PathVariable("club-id") @Positive Long clubId,
            @PathVariable("article-id") @Positive Long articleId,
            @RequestBody @Valid ArticleUpdateRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        UpdateArticleCommand command = UpdateArticleCommand.create(memberId, clubId, articleId, dto.title(), dto.content());
        Article article = articleUpdatePort.updateArticle(command);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ArticleUpdatedResponseDto.toDto(article));
    }

    @DeleteMapping("/{article-id}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable("club-id") @Positive Long clubId,
            @PathVariable("article-id") @Positive Long articleId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        articleDeletePort.deleteArticle(clubId, articleId, memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
