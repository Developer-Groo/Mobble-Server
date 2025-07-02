package com.mobble.mobbleserver.domain.comment.service;

import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentListResponseDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentResponseDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public CommentResponseDto createRootComment(Long articleId, CommentRequestDto dto) {
        // Todo: 해당 member 가 존재하는지 검증 필요
        validateArticleExistence(articleId);
        Comment comment = dto.toEntity();

        return CommentResponseDto.toDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponseDto createReplyComment(Long articleId, Long parentCommentId, CommentRequestDto dto) {
        // Todo: 해당 member 가 존재하는지 검증 필요
        validateArticleExistence(articleId);
        Comment parentComment = findCommentOrThrow(parentCommentId);
        Comment comment = dto.toEntity(parentComment);

        return CommentResponseDto.toDto(commentRepository.save(comment));
    }

    public List<CommentListResponseDto> getCommentListByArticle(Long articleId) {
        validateArticleExistence(articleId);

        return commentRepository.findCommentsWithRepliesByArticleId(articleId).stream()
                .map(CommentListResponseDto::toDto)
                .toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto dto) {
        // Todo: 해당 member 의 댓글인지 검증 필요
        Comment comment = findCommentOrThrow(commentId);
        Comment updatedComment = comment.changeContent(dto.content());

        return CommentResponseDto.toDto(updatedComment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        // Todo: 해당 member 의 댓글인지 검증 필요
        Comment comment = findCommentOrThrow(commentId);
        commentRepository.delete(comment);
    }

    private void validateArticleExistence(Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            // Todo: Custom 예외 적용
            throw new IllegalArgumentException("");
        }
    }

    private Comment findCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException(""));
                // Todo: Custom 예외 적용
    }
}
