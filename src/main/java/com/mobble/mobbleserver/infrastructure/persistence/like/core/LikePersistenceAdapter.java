package com.mobble.mobbleserver.infrastructure.persistence.like.core;

import com.mobble.mobbleserver.application.like.core.port.required.LikeReadPort;
import com.mobble.mobbleserver.application.like.core.port.required.LikeWritePort;
import com.mobble.mobbleserver.domain.like.core.ArticleLike;
import com.mobble.mobbleserver.domain.like.core.ClubLike;
import com.mobble.mobbleserver.domain.like.core.CommentLike;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.infrastructure.persistence.like.core.articleLike.JpaArticleLikeRepository;
import com.mobble.mobbleserver.infrastructure.persistence.like.core.clubLike.JpaClubLikeRepository;
import com.mobble.mobbleserver.infrastructure.persistence.like.core.commentLike.JpaCommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikePersistenceAdapter implements LikeReadPort, LikeWritePort {

    private final JpaArticleLikeRepository articleLikeRepository;
    private final JpaClubLikeRepository clubLikeRepository;
    private final JpaCommentLikeRepository commentLikeRepository;

    /**
     * LikeReadPort
     */
    @Override
    public boolean existsTargetLike(LikeType likeType, Long targetId, Long memberId) {
        return switch (likeType) {
            case ARTICLE -> articleLikeRepository.existsByMemberIdAndArticleId(memberId, targetId);
            case CLUB -> clubLikeRepository.existsByMemberIdAndClubId(memberId, targetId);
            case COMMENT -> commentLikeRepository.existsByMemberIdAndCommentId(memberId, targetId);
        };
    }

    @Override
    public List<Long> findLikedMemberListByTargetId(LikeType likeType, Long targetId) {
        return switch (likeType) {
            case ARTICLE -> articleLikeRepository.findLikedMemberListByArticleId(targetId);
            default -> throw new DomainException(LikeErrorCode.NOT_SUPPORTED_TYPE);
        };
    }

    @Override
    public List<Long> findLikedTargetIdListByMemberId(LikeType likeType, Long memberId, List<Long> targetIds) {
        return switch (likeType) {
            case ARTICLE -> articleLikeRepository.findLikedArticleIdListByMemberId(targetIds, memberId);
            case CLUB -> clubLikeRepository.findLikedClubIdListByMemberId(targetIds, memberId);
            case COMMENT -> commentLikeRepository.findLikedCommentIdListByMemberId(targetIds, memberId);
        };
    }

    /**
     * LikeWritePort
     */
    @Override
    @Transactional
    public void save(LikeType likeType, Long targetId, Long memberId) {
        switch (likeType) {
            case ARTICLE -> articleLikeRepository.save(ArticleLike.createArticleLike(memberId, targetId));
            case CLUB -> clubLikeRepository.save(ClubLike.createClubLike(memberId, targetId));
            case COMMENT -> commentLikeRepository.save(CommentLike.createCommentLike(memberId, targetId));
        }
    }

    @Override
    @Transactional
    public void delete(LikeType likeType, Long targetId, Long memberId) {
        switch (likeType) {
            case ARTICLE -> articleLikeRepository.deleteByMemberIdAndArticleId(memberId, targetId);
            case CLUB -> clubLikeRepository.deleteByMemberIdAndClubId(memberId, targetId);
            case COMMENT -> commentLikeRepository.deleteByMemberIdAndCommentId(memberId, targetId);
        }
    }
}
