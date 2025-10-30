package com.mobble.mobbleserver.application.liked.core.service.dispatcher;

import com.mobble.mobbleserver.application.liked.core.port.provided.LikeMemberListPort;
import com.mobble.mobbleserver.application.liked.core.port.provided.LikeTogglePort;
import com.mobble.mobbleserver.application.liked.core.service.core.ArticleLikeModifyService;
import com.mobble.mobbleserver.application.liked.core.service.core.ArticleLikeQueryService;
import com.mobble.mobbleserver.application.liked.core.service.core.ClubLikeModifyService;
import com.mobble.mobbleserver.application.liked.core.service.core.CommentLikeModifyService;
import com.mobble.mobbleserver.domain.like.core.AbstractLike;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeDispatcher implements LikeTogglePort, LikeMemberListPort {

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
    public List<? extends AbstractLike> getLikeEntities(LikeType likeType, Long targetId) {
        if (likeType == LikeType.ARTICLE) {
            return articleLikeQueryService.getLikeEntities(targetId);
        }
        throw new DomainException(LikeErrorCode.NOT_SUPPORTED_TYPE);
    }
}

