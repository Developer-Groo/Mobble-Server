package com.mobble.mobbleserver.application.comment.service;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.comment.command.response.RootCommentResult;
import com.mobble.mobbleserver.application.comment.port.provided.CommentQueryPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.infrastructure.persistence.comment.projection.CommentLikeInfoDto;
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
    public List<RootCommentResult> getCommentListByArticle(Long articleId, Long memberId) {
        Article article = assertArticleByArticleId(articleId);

        // Todo: Like 조회 로직 변경 필요
        List<Comment> comments = commentReadPort.findCommentsWithRepliesByArticleId(article.getId());
        Map<Long, CommentLikeInfoDto> likeInfoMap = getCommentLikeInfo(comments, memberId);

        return comments.stream()
                .map(comment -> RootCommentResult.toDto(comment, likeInfoMap))
                .toList();
    }


        return commentReadPort.findLikeInfoByCommentIdsAndMemberId(commentIds, memberId);
    }

    /* ==== Private Helper ==== */
    private Article assertArticleByArticleId(Long articleId) {
        return articleReadPort.findById(articleId)
                .orElseThrow(() -> new DomainException(ArticleErrorCode.NOT_FOUND));
    }
}
