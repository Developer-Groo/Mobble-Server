package com.mobble.mobbleserver.application.article.command.response;

import com.mobble.mobbleserver.application.comment.command.response.RootCommentResult;
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
        Long likeCount,
        List<ArticleLikedMembers> likedMembers,
        List<RootCommentResult> commentList,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        // todo: Owner 의 프로필 이미지 데이터 추가
        // todo: isOwner, isLiked, Comment Count 고려
) {

    public static ArticleDetailResult create(
            Article article,
            Long likeCount,
            List<Member> likedMembers,
            List<RootCommentResult> commentList
    ) {
        return new ArticleDetailResult(
                article.getClub().getId(),
                article.getId(),
                article.getArticleType(),
                article.getContent().getTitle(),
                article.getContent().getBody(),
                article.getMember().getName(),
                likeCount,
                likedMembers.stream()
                        .map(ArticleLikedMembers::create)
                        .toList(),
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
