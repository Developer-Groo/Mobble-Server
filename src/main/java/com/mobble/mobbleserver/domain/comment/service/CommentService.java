package com.mobble.mobbleserver.domain.comment.service;

import com.mobble.mobbleserver.domain.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentListResponseDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentResponseDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto createRootComment(Long articleId, CommentRequestDto dto) {
        // Todo: 해당 member 의 댓글인지 검증 필요
        // Todo: 해당 article 이 존재하는지 검증 필요
        Comment comment = dto.toEntity();

        return CommentResponseDto.toDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponseDto createReplyComment(Long articleId, Long parentCommentId, CommentRequestDto dto) {
        // Todo: 해당 member 의 댓글인지 검증 필요
        // Todo: 해당 article 이 존재하는지 검증 필요
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new IllegalArgumentException(""));
        Comment comment = dto.toEntity(parentComment);

        return CommentResponseDto.toDto(commentRepository.save(comment));
    }

    public List<CommentListResponseDto> getCommentListByArticle(Long articleId) {
        return commentRepository.findCommentsWithRepliesByArticleId(articleId).stream()
                .map(CommentListResponseDto::toDto)
                .toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto dto) {
        // Todo: 해당 member 의 댓글인지 검증 필요
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException(""));

        return CommentResponseDto.toDto(comment.changeContent(dto.content()));
    }

    @Transactional
    public void deleteComment(Long commentId) {
        // Todo: 해당 member 의 댓글인지 검증 필요
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException(""));
        commentRepository.delete(comment);
    }
}
