package com.mobble.mobbleserver.application.comment.service;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentQueryPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.infrastructure.persistence.comment.projection.CommentLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.web.comment.dto.response.RootCommentResponseDto;
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

    private final CommentReadPort commentReadPort;
    private final ArticleReadPort articleReadPort;

    @Override
    public List<RootCommentResponseDto> getCommentListByArticle(Long articleId, Long memberId) {
        Article article = articleReadPort.findById(articleId)
                .orElseThrow(() -> new DomainException(ArticleErrorCode.NOT_FOUND));

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
