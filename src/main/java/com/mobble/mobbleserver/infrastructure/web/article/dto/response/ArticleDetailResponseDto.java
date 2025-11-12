package com.mobble.mobbleserver.infrastructure.web.article.dto.response;

import com.mobble.mobbleserver.application.article.command.response.ArticleDetailResult;
import com.mobble.mobbleserver.application.comment.command.response.RootCommentResult;
import com.mobble.mobbleserver.domain.article.ArticleType;

import java.time.LocalDateTime;
import java.util.List;

import static com.mobble.mobbleserver.application.article.command.response.ArticleDetailResult.ArticleLikedMembers;

public record ArticleDetailResponseDto(
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

    public static ArticleDetailResponseDto create(ArticleDetailResult result) {
        return new ArticleDetailResponseDto(
                result.clubId(),
                result.articleId(),
                result.articleType(),
                result.title(),
                result.body(),
                result.ownerName(),
                result.likeCount(),
                result.likedMembers(),
                result.commentList(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
