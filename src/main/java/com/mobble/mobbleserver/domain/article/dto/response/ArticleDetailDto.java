package com.mobble.mobbleserver.domain.article.dto.response;

import com.mobble.mobbleserver.domain.article.entity.ArticleType;

import java.time.LocalDateTime;

public record ArticleDetailDto(
        Long articleId,
        String title,
        String content,
        ArticleType articleType,
        Long clubId,
        Long memberId,
        String memberName,
        Long likeCount,
        Long commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
