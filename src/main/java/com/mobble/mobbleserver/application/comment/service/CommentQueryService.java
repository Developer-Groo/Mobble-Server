package com.mobble.mobbleserver.application.comment.service;

import com.mobble.mobbleserver.application.comment.port.provided.CommentQueryPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.infrastructure.persistence.comment.projection.CommentLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.web.comment.dto.response.RootCommentResponseDto;
import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.article.validator.ArticleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService implements CommentQueryPort {

    private final ArticleValidator articleValidator;
    private final CommentReadPort commentReadPort;

    @Override
    public List<RootCommentResponseDto> getCommentListByArticle(Long articleId, Long memberId) {
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);
        List<Comment> comments = commentReadPort.findCommentsWithRepliesByArticleId(article.getId());
        Map<Long, CommentLikeInfoDto> likeInfoMap = getCommentLikeInfo(comments, memberId);

        return comments.stream()
                .map(comment -> RootCommentResponseDto.toDto(comment, likeInfoMap))
                .toList();
    }

    private Map<Long, CommentLikeInfoDto> getCommentLikeInfo(List<Comment> comments, Long memberId) {
        List<Long> commentIds = comments.stream()
                .flatMap(comment -> Stream.concat(
                        Stream.of(comment.getId()),
                        comment.getChildren().stream().map(Comment::getId)
                ))
                .distinct()
                .toList();

        return commentReadPort.findLikeInfoByCommentIdsAndMemberId(commentIds, memberId);
    }
}
