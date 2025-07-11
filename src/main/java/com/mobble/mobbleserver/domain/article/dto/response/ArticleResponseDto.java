package com.mobble.mobbleserver.domain.article.dto.response;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentListResponseDto;

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
        boolean likedByMe,
        boolean isMine,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CommentListResponseDto> comments
) {
    public static ArticleResponseDto toDto(
            Article article,
            boolean likedByMe,
            boolean isMine,
            int likeCount,
            List<CommentListResponseDto> comments
    ) {
        return new ArticleResponseDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getArticleType(),
                article.getClub().getId(),
                article.getMember().getId(),
                article.getMember().getName(), // memberName
                likeCount,
                likedByMe,
                isMine,
                article.getCreatedAt(),
                article.getUpdatedAt(),
                comments
        );
    }
}
