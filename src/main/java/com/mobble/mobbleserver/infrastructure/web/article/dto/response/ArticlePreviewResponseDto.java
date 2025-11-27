package com.mobble.mobbleserver.infrastructure.web.article.dto.response;

import com.mobble.mobbleserver.application.article.result.ArticlePreviewResult;
import com.mobble.mobbleserver.domain.article.ArticleType;

import java.time.LocalDateTime;
import java.util.List;

public record ArticlePreviewResponseDto(
        Long clubId,
        Long articleId,
        ArticleType articleType,
        String title,
        String body,
        Long ownerId,
        String ownerName,
        String profileImageUrl,
        int likeCount,
        boolean isLiked,
        int commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static List<ArticlePreviewResponseDto> toDto(List<ArticlePreviewResult> results) {
        return results.stream()
                .map(ArticlePreviewResponseDto::from)
                .toList();
    }

    private static ArticlePreviewResponseDto from(ArticlePreviewResult result) {
        return new ArticlePreviewResponseDto(
                result.clubId(),
                result.articleId(),
                result.articleType(),
                result.title(),
                result.body(),
                result.ownerId(),
                result.ownerName(),
                result.profileImageUrl(),
                result.likeCount(),
                result.isLiked(),
                result.commentCount(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
