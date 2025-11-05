package com.mobble.mobbleserver.infrastructure.web.like;

import com.mobble.mobbleserver.application.like.core.port.provided.LikeModifyPort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeAPI {

    private final LikeModifyPort likeModifyPort;

    @PostMapping
    public ResponseEntity<Void> toggleLike(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @RequestParam LikeType likeType,
            @RequestParam Long targetId
    ) {
        likeModifyPort.toggleLike(likeType, targetId, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
