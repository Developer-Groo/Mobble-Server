package com.mobble.mobbleserver.domain.comment.controller;

import com.mobble.mobbleserver.domain.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentResponseDto;
import com.mobble.mobbleserver.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/articles/{article-id}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createRootComment(
            @PathVariable("article-id") @Positive Long articleId,
            @RequestBody @Valid CommentRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createRootComment(memberId, articleId, dto));
    }

    @PostMapping("/{parent-comment-id}/replies")
    public ResponseEntity<CommentResponseDto> createReplyComment(
            @PathVariable("article-id") @Positive Long articleId,
            @PathVariable("parent-comment-id") @Positive Long parentCommentId,
            @RequestBody @Valid CommentRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createReplyComment(memberId, articleId, parentCommentId, dto));
    }

    @PatchMapping("/{comment-id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable("article-id") @Positive Long articleId,
            @PathVariable("comment-id") @Positive Long commentId,
            @RequestBody @Valid CommentRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.updateComment(articleId, commentId, memberId, dto));
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("article-id") @Positive Long articleId,
            @PathVariable("comment-id") @Positive Long commentId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        commentService.deleteComment(articleId, commentId, memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
