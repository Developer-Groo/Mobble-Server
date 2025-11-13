package com.mobble.mobbleserver.infrastructure.web.article.dto.response;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;

import java.time.LocalDateTime;

public record ArticleResponseDto(
        Long clubId,
        Long articleId,
        ArticleType articleType,
        String title,
        String body,
        Long ownerId,
        String ownerName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        // todo: Owner 의 프로필 이미지 데이터 추가
) {

    public static ArticleResponseDto create(Article article) {

        return new ArticleResponseDto(
                article.getClub().getId(),
                article.getId(),
                article.getArticleType(),
                article.getContent().getTitle(),
                article.getContent().getBody(),
                article.getMember().getId(),
                article.getMember().getName(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }
}
