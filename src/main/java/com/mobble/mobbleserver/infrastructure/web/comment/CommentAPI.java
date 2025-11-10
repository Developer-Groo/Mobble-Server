package com.mobble.mobbleserver.infrastructure.web.comment;

import com.mobble.mobbleserver.application.comment.command.request.CreateReplyCommentCommand;
import com.mobble.mobbleserver.application.comment.command.request.CreateRootCommentCommand;
import com.mobble.mobbleserver.application.comment.command.request.UpdateCommentCommand;
import com.mobble.mobbleserver.application.comment.port.provided.CommentCreatePort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentDeletePort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentUpdatePort;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.infrastructure.web.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.infrastructure.web.comment.dto.response.CommentResponseDto;
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
@RequestMapping("/api/clubs/{club-id}/articles/{article-id}/comments")
public class CommentAPI {

    private final CommentCreatePort commentCreatePort;
    private final CommentUpdatePort commentUpdatePort;
    private final CommentDeletePort commentDeletePort;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createRootComment(
            @PathVariable("club-id") @Positive Long clubId,
            @PathVariable("article-id") @Positive Long articleId,
            @RequestBody @Valid CommentRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        CreateRootCommentCommand command = CreateRootCommentCommand.create(memberId, clubId, articleId, dto.content());
        Comment comment = commentCreatePort.createRootComment(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommentResponseDto.toDto(comment));
    }

    @PostMapping("/{parent-comment-id}/replies")
    public ResponseEntity<CommentResponseDto> createReplyComment(
            @PathVariable("club-id") @Positive Long clubId,
            @PathVariable("article-id") @Positive Long articleId,
            @PathVariable("parent-comment-id") @Positive Long parentCommentId,
            @RequestBody @Valid CommentRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        CreateReplyCommentCommand command = CreateReplyCommentCommand.create(memberId, clubId, articleId, parentCommentId, dto.content());
        Comment comment = commentCreatePort.createReplyComment(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommentResponseDto.toDto(comment));
    }

    @PatchMapping("/{comment-id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable("club-id") @Positive Long clubId,
            @PathVariable("article-id") @Positive Long articleId,
            @PathVariable("comment-id") @Positive Long commentId,
            @RequestBody @Valid CommentRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        UpdateCommentCommand command = UpdateCommentCommand.create(memberId, clubId, articleId, commentId, dto.content());
        Comment comment = commentUpdatePort.update(command);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommentResponseDto.toDto(comment));
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("club-id") @Positive Long clubId,
            @PathVariable("article-id") @Positive Long articleId,
            @PathVariable("comment-id") @Positive Long commentId,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        commentDeletePort.delete(memberId, clubId,  articleId, commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
