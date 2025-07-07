package com.mobble.mobbleserver.domain.like.commentLike.controller;

import com.mobble.mobbleserver.domain.like.commentLike.dto.response.CommentLikeResponseDto;
import com.mobble.mobbleserver.domain.like.commentLike.service.CommentLikeService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("comments/{comment-id}/likes/{member-id}")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<CommentLikeResponseDto> likeToggle(
            @PathVariable("comment-id") @Positive Long commentId
    ) {
        Long memberId = 1L;
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentLikeService.toggleLike(commentId, memberId));

    }
}
