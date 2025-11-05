package com.mobble.mobbleserver.infrastructure.persistence.like.acl;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.liked.core.port.required.TargetLookupPort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.infrastructure.web.like.core.dto.command.TargetInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TargetLookupPersistenceAdapter implements TargetLookupPort {

    private final ArticleReadPort articleReadPort;
    private final ClubReadPort clubReadPort;
    private final CommentReadPort commentReadPort;

    @Override
    public TargetInfo targetLoad(LikeType likeType, Long targetId) {
        return switch (likeType) {
            case ARTICLE -> TargetInfo.toDto(likeType, targetId, articleReadPort.existsById(targetId));
            case CLUB -> TargetInfo.toDto(likeType, targetId, clubReadPort.existsById(targetId));
            case COMMENT -> TargetInfo.toDto(likeType, targetId, commentReadPort.existsById(targetId));
        };
    }
}
