package com.mobble.mobbleserver.infrastructure.web.like;

import com.mobble.mobbleserver.application.liked.core.port.provided.LikeMemberListPort;
import com.mobble.mobbleserver.application.liked.core.port.provided.LikeTogglePort;
import com.mobble.mobbleserver.domain.like.core.AbstractLike;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.infrastructure.web.like.dto.response.LikeMemberListResponseDto;
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
                .body(LikeMemberListResponseDto.toDto(likes));
    }
}
