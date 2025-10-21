package com.mobble.mobbleserver.infrastructure.persistence.like.articleLike;

import com.mobble.mobbleserver.application.like.required.LikeMemberListReadPort;
import com.mobble.mobbleserver.application.like.required.LikeReadPort;
import com.mobble.mobbleserver.application.like.required.LikeWritePort;
import com.mobble.mobbleserver.domain.like.articleLike.ArticleLike;
import com.mobble.mobbleserver.domain.like.baseLike.LikeType;
import com.mobble.mobbleserver.infrastructure.web.like.dto.response.LikeMemberListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("articleLikePersistenceAdapter")
@RequiredArgsConstructor
public class ArticleLikePersistenceAdapter implements LikeWritePort<ArticleLike>, LikeReadPort<ArticleLike>, LikeMemberListReadPort {

    private final JpaArticleLikeRepository jpaArticleLikeRepository;

    /**
     * LikeWritePort
     */
    @Override
    public ArticleLike save(ArticleLike articleLike) {
        return jpaArticleLikeRepository.save(articleLike);
    }

    @Override
    public void delete(ArticleLike articleLike) {
        jpaArticleLikeRepository.delete(articleLike);
    }

    /**
     * LikeReadPort
     */
    @Override
    public Optional<ArticleLike> findLike(LikeType likeType, Long targetId, Long memberId) {
        return jpaArticleLikeRepository.findLikedByArticleIdAndMemberId(targetId, memberId);
    }

    /**
     * LikeMemberListReadPort
     */
    @Override
    public LikeMemberListResponseDto getLikedMembers(Long articleId) {
        List<ArticleLike> articleLikeList = jpaArticleLikeRepository.findAllByArticleId(articleId);

        return LikeMemberListResponseDto.toDto(articleId, articleLikeList, ArticleLike::getMember);
    }
}
