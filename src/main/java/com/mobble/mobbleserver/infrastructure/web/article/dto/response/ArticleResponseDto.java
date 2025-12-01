package com.mobble.mobbleserver.infrastructure.web.article.dto.response;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.domain.member.Member;

import java.time.LocalDateTime;

public record ArticleResponseDto(
        Long clubId,
        Long articleId,
        ArticleType articleType,
        String title,
        String body,
        String articleImageUrl,
        Long ownerId,
        String ownerName,
        String profileImageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ArticleResponseDto toDto(Article article) {
        Member owner = article.getMember();
        String profileImageUrl = getProfileImageUrl(owner);
        String articleImageUrl = getArticleContentImageUrl(article);

        return new ArticleResponseDto(
                article.getClub().getId(),
                article.getId(),
                article.getArticleType(),
                article.getContent().getTitle(),
                article.getContent().getBody(),
                articleImageUrl,
                owner.getId(),
                owner.getName(),
                profileImageUrl,
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }

    private static String getProfileImageUrl(Member member) {
        return member.getProfileImage() != null
                ? member.getProfileImage().getUrl()
                : null;
    }

    private static String getArticleContentImageUrl(Article article) {
        return article.getImage() != null
                ? article.getImage().getUrl()
                : null;
    }
}
