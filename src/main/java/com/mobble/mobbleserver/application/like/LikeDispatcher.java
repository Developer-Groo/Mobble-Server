package com.mobble.mobbleserver.application.like;

import com.mobble.mobbleserver.application.like.provided.LikeMemberListPort;
import com.mobble.mobbleserver.application.like.provided.ToggleLikePort;
import com.mobble.mobbleserver.application.like.service.ArticleLikeModifyService;
import com.mobble.mobbleserver.application.like.service.ArticleLikeQueryService;
import com.mobble.mobbleserver.application.like.service.ClubLikeModifyService;
import com.mobble.mobbleserver.application.like.service.CommentLikeModifyService;
import com.mobble.mobbleserver.domain.like.baseLike.LikeType;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.infrastructure.web.like.dto.response.LikeMemberListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeDispatcher implements ToggleLikePort, LikeMemberListPort {

    private final ArticleLikeModifyService articleLikeModifyService;
    private final ArticleLikeQueryService articleLikeQueryService;
    private final ClubLikeModifyService clubLikeModifyService;
    private final CommentLikeModifyService commentLikeModifyService;

    @Override
    public void toggleLike(LikeType likeType, Long targetId, Long memberId) {
        switch (likeType) {
            case ARTICLE -> articleLikeModifyService.toggleLike(targetId, memberId);
            case CLUB -> clubLikeModifyService.toggleLike(targetId, memberId);
            case COMMENT -> commentLikeModifyService.toggleLike(targetId, memberId);
        }
    }

    @Override
    @Transactional
    public LikeMemberListResponseDto getMemberList(LikeType likeType, Long targetId) {
        if (likeType == LikeType.ARTICLE) {
            return articleLikeQueryService.getMemberList(targetId);
        }
        throw new DomainException(LikeErrorCode.NOT_SUPPORTED_TYPE);
    }
}
