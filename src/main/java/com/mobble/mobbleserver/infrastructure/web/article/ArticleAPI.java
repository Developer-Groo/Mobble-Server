package com.mobble.mobbleserver.infrastructure.web.article;

import com.mobble.mobbleserver.application.article.command.request.CreateArticleCommand;
import com.mobble.mobbleserver.application.article.command.request.UpdateArticleCommand;
import com.mobble.mobbleserver.application.article.command.response.ArticleDetailResult;
import com.mobble.mobbleserver.application.article.command.response.ArticlePreviewResult;
import com.mobble.mobbleserver.application.article.port.provided.ArticleCreatePort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleDeletePort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleQueryPort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleUpdatePort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.infrastructure.web.article.dto.request.ArticleCreateRequestDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.request.ArticleUpdateRequestDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleDetailResponseDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticlePreviewResponseDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleResponseDto;
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
        Article article = articleCreatePort.create(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ArticleResponseDto.create(article));
    }

    @GetMapping
    public ResponseEntity<List<ArticlePreviewResponseDto>> getArticlesPreview(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestParam(value = "articleType", required = false) ArticleType articleType,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        List<ArticlePreviewResult> previews = articleQueryPort.getArticlesPreview(clubId, memberId, articleType);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ArticlePreviewResponseDto.create(previews));
    }

    @GetMapping("/{article-id}")
    public ResponseEntity<ArticleDetailResponseDto> getArticleDetail(
            @PathVariable("club-id") @Positive Long clubId,
            @PathVariable("article-id") @Positive Long articleId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        ArticleDetailResult detail = articleQueryPort.getArticleDetail(clubId, articleId, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ArticleDetailResponseDto.create(detail));
    }

    @PatchMapping("/{article-id}")
    public ResponseEntity<ArticleResponseDto> updateArticle(
            @PathVariable("club-id") @Positive Long clubId,
            @PathVariable("article-id") @Positive Long articleId,
            @RequestBody @Valid ArticleUpdateRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        UpdateArticleCommand command = UpdateArticleCommand.create(memberId, clubId, articleId, dto.title(), dto.content());
        Article article = articleUpdatePort.update(command);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ArticleResponseDto.create(article));
    }

    @DeleteMapping("/{article-id}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable("club-id") @Positive Long clubId,
            @PathVariable("article-id") @Positive Long articleId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        articleDeletePort.delete(clubId, articleId, memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
