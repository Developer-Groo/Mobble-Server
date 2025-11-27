package com.mobble.mobbleserver.infrastructure.web.article.dto.response;

import com.mobble.mobbleserver.application.article.result.ArticleDetailResult;
import com.mobble.mobbleserver.application.comment.result.RootCommentResult;
import com.mobble.mobbleserver.domain.article.ArticleType;

import java.time.LocalDateTime;
import java.util.List;

import static com.mobble.mobbleserver.application.article.result.ArticleDetailResult.ArticleLikedMembers;

public record ArticleDetailResponseDto(
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

    public static ArticleDetailResponseDto create(ArticleDetailResult result) {
        return new ArticleDetailResponseDto(
                result.clubId(),
                result.articleId(),
                result.articleType(),
                result.title(),
                result.body(),
                result.ownerName(),
                result.isOwner(),
                result.isLiked(),
                result.likeCount(),
                result.likedMembers(),
                result.commentCount(),
                result.commentList(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
