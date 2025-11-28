package com.mobble.mobbleserver.application.like.service;

import com.mobble.mobbleserver.application.article.error.ArticleBusinessError;
import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.clubMember.error.ClubMemberBusinessError;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.comment.error.CommentBusinessError;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.like.error.LikeBusinessError;
import com.mobble.mobbleserver.application.like.port.provided.LikeModifyPort;
import com.mobble.mobbleserver.application.like.port.required.LikeCounterWritePort;
import com.mobble.mobbleserver.application.like.port.required.LikeReadPort;
import com.mobble.mobbleserver.application.like.port.required.LikeWritePort;
import com.mobble.mobbleserver.application.like.port.required.TargetExistencePort;
import com.mobble.mobbleserver.application.member.error.MemberBusinessError;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.domain.like.LikeType;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeModifyService implements LikeModifyPort {

    private final LikeWritePort likeWritePort;
    private final LikeCounterWritePort likeCounterWritePort;

    private final LikeReadPort likeReadPort;
    private final MemberReadPort memberReadPort;
    private final ArticleReadPort articleReadPort;
    private final CommentReadPort commentReadPort;
    private final ClubMemberReadPort clubMemberReadPort;

    private final TargetExistencePort targetExistencePort;

    @Override
    public void toggleLike(LikeType likeType, Long targetId, Long memberId) {
        Member member = assertMemberByMemberId(memberId);

        validateTarget(likeType, targetId);
        validateLikePermission(likeType, targetId, member.getId());

        boolean existsTargetLike = likeReadPort.existsTargetLike(likeType, targetId, member.getId());

        if (existsTargetLike) {
            likeWritePort.delete(likeType, targetId, member.getId());
            likeCounterWritePort.decrement(likeType, targetId);
        } else {
            likeWritePort.save(likeType, targetId, member.getId());
            likeCounterWritePort.increment(likeType, targetId);
        }
    }

    @Override
    public void delete(LikeType likeType, Long targetId) {
        if (targetId == null) return;

        likeWritePort.deleteByLikeTypeAndTargetId(likeType, targetId);
        likeCounterWritePort.deleteByLikeTypeAndTargetId(likeType, targetId);
    }

    @Override
    public void deleteAll(LikeType likeType, List<Long> targetIds) {
        if (targetIds == null || targetIds.isEmpty()) return;

        if (likeType == LikeType.CLUB) throw new BusinessException(LikeBusinessError.INVALID_LIKE_TYPE);

        List<Long> distinctIds = targetIds.stream()
                .distinct()
                .toList();

        likeWritePort.deleteAllByLikeTypeAndTargetIds(likeType, distinctIds);
        likeCounterWritePort.deleteAllByLikeTypeAndTargetIds(likeType, distinctIds);
    }

    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new BusinessException(MemberBusinessError.NOT_FOUND));
    }

    private Article assertArticleByArticleId(Long targetId) {
        return articleReadPort.findById(targetId)
                .orElseThrow(() -> new BusinessException(ArticleBusinessError.NOT_FOUND));
    }

    private Comment assertCommentByCommentId(Long targetId) {
        return commentReadPort.findById(targetId)
                .orElseThrow(() -> new BusinessException(CommentBusinessError.NOT_FOUND));
    }

    private void validateTarget(LikeType likeType, Long targetId) {
        if (!targetExistencePort.existsTarget(likeType, targetId)) {
            throw new BusinessException(LikeBusinessError.TARGET_NOT_FOUND);
        }
    }

    private void validateClubMember(Long clubId, Long memberId) {
        boolean isClubMember = clubMemberReadPort.existsByClubIdAndMemberId(clubId, memberId);
        if (!isClubMember) throw new BusinessException(ClubMemberBusinessError.NOT_JOINED_CLUB);
    }

    private void validateLikePermission(LikeType likeType, Long targetId, Long memberId) {
        switch (likeType) {
            case CLUB -> {
                // 로그인한 회원이면 통과
            }
            case ARTICLE -> {
                Long clubId = assertArticleByArticleId(targetId).getClub().getId();
                validateClubMember(clubId, memberId);
            }
            case COMMENT -> {
                Long clubId = assertCommentByCommentId(targetId).getArticle().getClub().getId();
                validateClubMember(clubId, memberId);
            }
        }
    }
}
