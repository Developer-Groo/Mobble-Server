package com.mobble.mobbleserver.infrastructure.persistence.comment;

import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentWritePort;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.infrastructure.persistence.comment.projection.CommentLikeInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements CommentWritePort, CommentReadPort  {

    private final JpaCommentRepository repository;

    /* CommentWritePort */
    @Override
    public Comment save(Comment comment) {
        return repository.save(comment);
    }

    @Override
    public void delete(Comment comment) {
         repository.delete(comment);
    }

    @Override
    public void deleteAll(List<Comment> comments) {
        repository.deleteAll(comments);
    }

    /* CommentReadPort */
    @Override
    public Optional<Comment> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Comment> findByIdAndMemberId(Long commentId, Long memberId) {
        return repository.findByIdAndMemberId(commentId, memberId);
    }

    @Override
    public List<Comment> findCommentsWithRepliesByArticleId(Long articleId) {
        return repository.findCommentsWithRepliesByArticleId(articleId);
    }

    @Override
    public Map<Long, CommentLikeInfoDto> findLikeInfoByCommentIdsAndMemberId(List<Long> ids, Long memberId) {
        return repository.findLikeInfoByCommentIdsAndMemberId(ids, memberId);
    }

    public Map<Long, Integer> countCommentsByArticleIds(List<Long> articleIds) {
        return repository.countCommentsByArticleIds(articleIds);
    }

    @Override
    public void deleteAllCommentByArticle_IdIn(List<Long> articleIds) {
        repository.deleteAllCommentByArticle_IdIn(articleIds);
    }
}
