package com.mobble.mobbleserver.infrastructure.web.like.core;

import com.mobble.mobbleserver.application.liked.core.port.provided.LikeMemberListPort;
import com.mobble.mobbleserver.application.liked.core.port.provided.LikeTogglePort;
import com.mobble.mobbleserver.domain.like.core.AbstractLike;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.infrastructure.web.like.core.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.infrastructure.web.like.core.dto.response.LikeMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeAPI {

    private final LikeTogglePort likeTogglePort;
    private final LikeMemberListPort likeMemberListPort;
    private final LikeMemberMapper likeMemberMapper;

    @PostMapping
    public ResponseEntity<Void> toggleLike(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @RequestParam LikeType likeType,
            @RequestParam Long targetId
    ) {
        likeTogglePort.toggleLike(likeType, targetId, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/members")
    public ResponseEntity<LikeMemberListResponseDto> getMemberList(
            @RequestParam LikeType likeType,
            @RequestParam Long targetId
    ) {
        List<? extends AbstractLike> likes = likeMemberListPort.getLikeEntities(likeType, targetId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(LikeMemberListResponseDto.toDto(likes, likeMemberMapper));
    }
}
