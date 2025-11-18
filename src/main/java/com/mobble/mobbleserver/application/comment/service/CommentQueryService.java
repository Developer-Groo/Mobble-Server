package com.mobble.mobbleserver.application.comment.service;

import com.mobble.mobbleserver.application.article.error.ArticleBusinessError;
import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.comment.command.response.RootCommentResult;
import com.mobble.mobbleserver.application.comment.port.provided.CommentQueryPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.like.port.provided.LikeQueryPort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.domain.like.LikeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService implements CommentQueryPort {

    private final LikeQueryPort likeQueryPort;

    private final CommentReadPort commentReadPort;
    private final ArticleReadPort articleReadPort;

    @Override
    public List<RootCommentResult> getCommentList(Long articleId, Long memberId) {
        Article article = assertArticleByArticleId(articleId);

        List<Comment> comments = commentReadPort.findCommentsByArticleId(article.getId());
        List<Long> commentIds = comments.stream()
                .map(Comment::getId)
                .toList();

        Map<Long, Long> likeCounts = likeQueryPort.getLikeCounts(LikeType.COMMENT, commentIds);
        List<Long> likedList = likeQueryPort.getLikedIds(LikeType.COMMENT, memberId, commentIds);

        return RootCommentResult.create(comments, likeCounts, likedList);
    }

    @Override
    public Map<Long, Integer> getCountComments(List<Long> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) return Map.of();

        return commentReadPort.countCommentsByArticleIds(articleIds);
    }

    /* ==== Private Helper ==== */
    private Article assertArticleByArticleId(Long articleId) {
        return articleReadPort.findById(articleId)
                .orElseThrow(() -> new BusinessException(ArticleBusinessError.NOT_FOUND));
    }
}
