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
        String ownerName,
        boolean isOwner,
        boolean isLiked,
        int likeCount,
        List<ArticleLikedMembers> likedMembers,
        int commentCount,
        List<RootCommentResult> commentList,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        // todo: Owner 의 프로필 이미지 데이터 추가
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
        return new ArticleDetailResult(
                article.getClub().getId(),
                article.getId(),
                article.getArticleType(),
                article.getContent().getTitle(),
                article.getContent().getBody(),
                article.getMember().getName(),
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
            String name
            // todo: member 프로필 이미지 url
    ) {

        public static ArticleLikedMembers create(Member member) {
            return new ArticleLikedMembers(member.getId(), member.getName());
        }
    }
}
