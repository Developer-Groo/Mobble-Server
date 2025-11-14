package com.mobble.mobbleserver.application.article.command.response;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ArticlePreviewResult(
        Long clubId,
        Long articleId,
        ArticleType articleType,
        String title,
        String body,
        Long ownerId,
        String ownerName,
        Long likeCount,
        boolean isLiked,
        int commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        // todo: Owner 의 프로필 이미지 데이터 추가
) {

    public static List<ArticlePreviewResult> create(
            List<Article> articles,
            Map<Long, Long> likeCounts,
            List<Long> likedIds,
            Map<Long, Integer> commentCounts
    ) {
        return articles.stream()
                .map(article -> {
                    Long likeCount = likeCounts.getOrDefault(article.getId(), 0L);
                    boolean isLiked = likedIds.contains(article.getId());
                    Integer commentCount = commentCounts.getOrDefault(article.getId(), 0);

                    return from(article, likeCount, isLiked, commentCount);
                })
                .toList();
    }

    private static ArticlePreviewResult from(Article article, Long likeCount, boolean isLiked, Integer commentCount) {
        String previewContent = summarize(article.getContent().getBody());

        return new ArticlePreviewResult(
                article.getClub().getId(),
                article.getId(),
                article.getArticleType(),
                article.getContent().getTitle(),
                previewContent,
                article.getMember().getId(),
                article.getMember().getName(),
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
}
