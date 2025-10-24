package com.mobble.mobbleserver.infrastructure.web.article.dto.response;

import com.mobble.mobbleserver.infrastructure.web.comment.dto.response.RootCommentResponseDto;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.infrastructure.persistence.article.projection.ArticleLikeInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleResponseDto(
        Long articleId,
        String title,
        String content,
        ArticleType articleType,
        Long clubId,
        Long memberId,
        String memberName,
        // todo: 글작성 회원 프로필 사진 추가
        int likeCount,
        boolean isLiked,
        boolean isMine,
        int commentCount,
        List<RootCommentResponseDto> comments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ArticleResponseDto toDto(Article article) {

        return new ArticleResponseDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getArticleType(),
                article.getClub().getId(),
                article.getMember().getId(),
                article.getMember().getName(),
                0,
                false,
                false,
                0,
                List.of(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }

    public static ArticleResponseDto toDto(
            Article article,
            boolean isMine,
            ArticleLikeInfoDto likeInfo,
            int commentCount,
            List<RootCommentResponseDto> comments
    ) {

        return new ArticleResponseDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getArticleType(),
                article.getClub().getId(),
                article.getMember().getId(),
                article.getMember().getName(),
                likeInfo.likeCount(),
                likeInfo.isLiked(),
                isMine,
                commentCount,
                comments,
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }
}
