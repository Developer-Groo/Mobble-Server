package com.mobble.mobbleserver.domain.article.dto.response;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.comment.dto.response.RootCommentResponseDto;

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
        Long likeCount,
        Long commentCount,
        boolean likedByMe,
        boolean isMine,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<RootCommentResponseDto> comments
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
                0L,
                0L,
                false,
                true,
                article.getCreatedAt(),
                article.getUpdatedAt(),
                List.of()
        );
    }

    public static ArticleResponseDto toDto(
            ArticleDetailDto dto,
            boolean likedByMe,
            boolean isMine,
            List<RootCommentResponseDto> comments
    ) {
        return new ArticleResponseDto(
                dto.articleId(),
                dto.title(),
                dto.content(),
                dto.articleType(),
                dto.clubId(),
                dto.memberId(),
                dto.memberName(),
                dto.likeCount(),
                dto.commentCount(),
                likedByMe,
                isMine,
                dto.createdAt(),
                dto.updatedAt(),
                comments
        );
    }
}
