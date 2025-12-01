package com.mobble.mobbleserver.infrastructure.persistence.like.existence;

import com.mobble.mobbleserver.application.like.port.required.TargetExistencePort;
import com.mobble.mobbleserver.domain.like.LikeType;
import com.mobble.mobbleserver.infrastructure.persistence.article.JpaArticleRepository;
import com.mobble.mobbleserver.infrastructure.persistence.club.JpaClubRepository;
import com.mobble.mobbleserver.infrastructure.persistence.comment.JpaCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TargetExistencePersistenceAdapter implements TargetExistencePort {
    private final JpaArticleRepository articleRepository;
    private final JpaClubRepository clubRepository;
    private final JpaCommentRepository commentRepository;

    @Override
    public boolean existsTarget(LikeType likeType, Long targetId) {
        return switch (likeType) {
            case ARTICLE -> articleRepository.existsById(targetId);
            case CLUB -> clubRepository.existsById(targetId);
            case COMMENT -> commentRepository.existsById(targetId);
        };
    }
}
