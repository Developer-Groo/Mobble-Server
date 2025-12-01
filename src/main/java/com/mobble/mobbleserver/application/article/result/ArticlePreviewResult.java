package com.mobble.mobbleserver.application.article.result;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.domain.member.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ArticlePreviewResult(
        Long clubId,
        Long articleId,
        ArticleType articleType,
        String title,
        String body,
        String articleImageUrl,
        Long ownerId,
        String ownerName,
        String profileImageUrl,
        int likeCount,
        boolean isLiked,
        int commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static List<ArticlePreviewResult> create(
            List<Article> articles,
            Map<Long, Integer> likeCounts,
            List<Long> likedIds,
            Map<Long, Integer> commentCounts
    ) {
        return articles.stream()
                .map(article -> {
                    Integer likeCount = likeCounts.getOrDefault(article.getId(), 0);
                    boolean isLiked = likedIds.contains(article.getId());
                    Integer commentCount = commentCounts.getOrDefault(article.getId(), 0);

                    return from(article, likeCount, isLiked, commentCount);
                })
                .toList();
    }

    private static ArticlePreviewResult from(Article article, Integer likeCount, boolean isLiked, Integer commentCount) {
        String previewContent = summarize(article.getContent().getBody());
        Member owner = article.getMember();
        String profileImageUrl = getProfileImageUrl(owner);
        String articleImageUrl = getArticleContentImageUrl(article);

        return new ArticlePreviewResult(
                article.getClub().getId(),
                article.getId(),
                article.getArticleType(),
                article.getContent().getTitle(),
                previewContent,
                articleImageUrl,
                owner.getId(),
                owner.getName(),
                profileImageUrl,
                likeCount,
                isLiked,
                commentCount,
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }

    private static String summarize(String content) {
        if (content == null) return "";

        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
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
