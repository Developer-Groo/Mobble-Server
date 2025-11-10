package com.mobble.mobbleserver.infrastructure.web.article.dto.response;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;

import java.time.LocalDateTime;

public record ArticleUpdatedResponseDto(
        Long articleId,
        String title,
        String content,
        ArticleType articleType,
        Long clubId,
        Long memberId,
        LocalDateTime updatedAt
) {
    public static ArticleUpdatedResponseDto toDto(Article article ){
        return new ArticleUpdatedResponseDto(
                article.getId(),
                article.getContent().getTitle(),
                summarize(article.getContent().getBody()),
                article.getArticleType(),
                article.getClub().getId(),
                article.getMember().getId(),
                article.getUpdatedAt()
        );
    }

    private static String summarize(String content) {
        if (content == null) return "";

        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }
}
