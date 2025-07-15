package com.mobble.mobbleserver.domain.article.dto.response;

import com.mobble.mobbleserver.domain.article.entity.ArticleType;

import java.time.LocalDateTime;

public record ArticleSummaryResponseDto(
        Long articleId,
        String title,
        String content,
        ArticleType articleType,
        Long clubId,
        String memberName,

        // todo: 글작성 회원 프로필 사진 추가
        Long likeCount,
        Long commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
