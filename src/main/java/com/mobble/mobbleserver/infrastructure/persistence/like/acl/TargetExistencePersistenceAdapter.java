package com.mobble.mobbleserver.infrastructure.persistence.like.acl;
//Todo 패키지명 bridge vs gateway
import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.like.core.port.required.TargetExistencePort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TargetExistencePersistenceAdapter implements TargetExistencePort {
    private final ArticleReadPort articleReadPort;
    private final ClubReadPort clubReadPort;
    private final CommentReadPort commentReadPort;

    @Override
    public boolean existsTarget(LikeType likeType, Long targetId) {
        return switch (likeType) {
            case ARTICLE -> articleReadPort.existsById(targetId);
            case CLUB -> clubReadPort.existsById(targetId);
            case COMMENT -> commentReadPort.existsById(targetId);
        };
    }
}
