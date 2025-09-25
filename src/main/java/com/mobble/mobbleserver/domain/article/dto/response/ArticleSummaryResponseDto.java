package com.mobble.mobbleserver.domain.article.dto.response;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.article.repository.dto.ArticleLikeInfoDto;

import java.time.LocalDateTime;

public record ArticleSummaryResponseDto(
        Long articleId,
        String title,
        String content,
        ArticleType articleType,
        Long clubId,
        String memberName,
        int likeCount,
        boolean isLiked,
        // todo: 글작성 회원 프로필 사진 추가
        int commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ArticleSummaryResponseDto toDto(Article article,ArticleLikeInfoDto likeInfo, int commentCount ){
        String summarizedContent = summarize(article.getContent());

        return new ArticleSummaryResponseDto(
                article.getId(),
                article.getTitle(),
                summarizedContent,
                article.getArticleType(),
                article.getClub().getId(),
                article.getMember().getName(),
                likeInfo.likeCount(),
                likeInfo.isLiked(),
                commentCount,
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }

    private static String summarize(String content) {
        if (content == null) return "";

        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }
}
