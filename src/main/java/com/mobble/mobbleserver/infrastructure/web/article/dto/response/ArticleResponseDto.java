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
        Long ownerId,
        String ownerName,
        String profileImageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ArticleResponseDto toDto(Article article) {
        Member owner = article.getMember();
        String profileImageUrl = getProfileImageUrl(owner);

        return new ArticleResponseDto(
                article.getClub().getId(),
                article.getId(),
                article.getArticleType(),
                article.getContent().getTitle(),
                article.getContent().getBody(),
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
}
