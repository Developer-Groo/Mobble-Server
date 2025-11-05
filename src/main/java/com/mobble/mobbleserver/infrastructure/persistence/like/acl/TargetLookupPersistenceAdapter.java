package com.mobble.mobbleserver.infrastructure.persistence.like.acl;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.liked.core.port.required.TargetLookupPort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.infrastructure.web.like.core.dto.command.TargetInfoResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TargetLookupPersistenceAdapter implements TargetLookupPort {

    private final ArticleReadPort articleReadPort;
    private final ClubReadPort clubReadPort;
    private final CommentReadPort commentReadPort;

    @Override
    public TargetInfoResult targetLoad(LikeType likeType, Long targetId) {
        return switch (likeType) {
            case ARTICLE -> TargetInfoResult.toDto(likeType, targetId, articleReadPort.existsById(targetId));
            case CLUB -> TargetInfoResult.toDto(likeType, targetId, clubReadPort.existsById(targetId));
            case COMMENT -> TargetInfoResult.toDto(likeType, targetId, commentReadPort.existsById(targetId));
        };
    }
}
