package com.mobble.mobbleserver.domain.comment.controller;

import com.mobble.mobbleserver.domain.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentListResponseDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentResponseDto;
import com.mobble.mobbleserver.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/articles/")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{article-id}/comments")
    public ResponseEntity<CommentResponseDto> createRootComment(
            @PathVariable("article-id") Long articleId,
            @RequestBody CommentRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createRootComment(articleId, dto));
    }

    @PostMapping("/{article-id}/comments/{parent-comment-id}/replies")
    public ResponseEntity<CommentResponseDto> createReplyComment(
            @PathVariable("article-id") Long articleId,
            @PathVariable("parent-comment-id") Long parentCommentId,
            @RequestBody CommentRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createReplyComment(articleId, parentCommentId, dto));
    }

    @GetMapping("/{article-id}/comments")
    public ResponseEntity<List<CommentListResponseDto>> getCommentList(@PathVariable("article-id") Long articleId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getCommentListByArticle(articleId));
    }

    @PatchMapping("/comments/{comment-id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable("comment-id") Long commentId,
            @RequestBody CommentRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.updateComment(commentId, dto));
    }

    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("comment-id") Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
