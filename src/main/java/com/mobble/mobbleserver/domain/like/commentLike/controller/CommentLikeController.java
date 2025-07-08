package com.mobble.mobbleserver.domain.like.commentLike.controller;

import com.mobble.mobbleserver.domain.like.commentLike.dto.response.CommentLikeResponseDto;
import com.mobble.mobbleserver.domain.like.commentLike.service.CommentLikeService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("comments/{comment-id}/likes")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<CommentLikeResponseDto> likeToggle(
            @PathVariable("comment-id") @Positive Long commentId
    ) {
        Long memberId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentLikeService.toggleLike(commentId, memberId));

    }
}
