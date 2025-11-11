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
public class CommentPersistenceAdapter implements CommentWritePort, CommentReadPort {

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
    public void deleteAllByArticleId(Long articleId) {
        repository.deleteAllByArticleId(articleId);
    }

    @Override
    public void deleteAllByArticleIdIn(List<Long> articleIds) {
        repository.deleteAllByArticleIdIn(articleIds);
    }

    /* CommentReadPort */
    @Override
    public Optional<Comment> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Comment> findByIdAndArticleId(Long commentId, Long articleId) {
        return repository.findByIdAndArticleId(commentId, articleId);
    }

    @Override
    public List<Comment> findCommentsWithRepliesByArticleId(Long articleId) {
        return repository.findCommentsWithRepliesByArticleId(articleId);
    }

    @Override
    public List<Long> findIdsByArticleId(Long articleId) {
        return repository.findIdsByArticleId(articleId);
    }

    @Override
    public List<Long> findIdsByArticleIdIn(List<Long> articleIds) {
        return repository.findIdsByArticleIdIn(articleIds);
    }

    @Override
    public Map<Long, Integer> countCommentsByArticleIds(List<Long> articleIds) {
        return repository.countCommentsByArticleIds(articleIds);
    }
}
