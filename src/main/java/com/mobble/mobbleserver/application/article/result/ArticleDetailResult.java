package com.mobble.mobbleserver.application.article.result;

import com.mobble.mobbleserver.application.comment.result.RootCommentResult;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.domain.member.Member;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleDetailResult(
        Long clubId,
        Long articleId,
        ArticleType articleType,
        String title,
        String body,
        String articleImageUrl,
        Long ownerId,
        String ownerName,
        String profileImageUrl,
        boolean isOwner,
        boolean isLiked,
        int likeCount,
        List<ArticleLikedMembers> likedMembers,
        int commentCount,
        List<RootCommentResult> commentList,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ArticleDetailResult create(
            Article article,
            int likeCount,
            List<Member> likedMembers,
            boolean isLiked,
            boolean isOwner,
            int commentCount,
            List<RootCommentResult> commentList
    ) {
        Member owner = article.getMember();
        String profileImageUrl = getProfileImageUrl(owner);
        String articleImageUrl = getArticleContentImageUrl(article);

        return new ArticleDetailResult(
                article.getClub().getId(),
                article.getId(),
                article.getArticleType(),
                article.getContent().getTitle(),
                article.getContent().getBody(),
                articleImageUrl,
                owner.getId(),
                owner.getName(),
                profileImageUrl,
                isOwner,
                isLiked,
                likeCount,
                likedMembers.stream()
                        .map(ArticleLikedMembers::create)
                        .toList(),
                commentCount,
                commentList,
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }

    public record ArticleLikedMembers(
            Long memberId,
            String name,
            String profileImageUrl
    ) {

        private static ArticleLikedMembers create(Member member) {
            String profileImageUrl = getProfileImageUrl(member);

            return new ArticleLikedMembers(member.getId(), member.getName(), profileImageUrl);
        }
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
