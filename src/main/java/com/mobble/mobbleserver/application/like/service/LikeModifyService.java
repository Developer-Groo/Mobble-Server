package com.mobble.mobbleserver.application.like.service;

import com.mobble.mobbleserver.application.like.port.provided.LikeModifyPort;
import com.mobble.mobbleserver.application.like.port.required.LikeReadPort;
import com.mobble.mobbleserver.application.like.port.required.LikeWritePort;
import com.mobble.mobbleserver.application.like.port.required.TargetExistencePort;
import com.mobble.mobbleserver.application.like.port.required.LikeCounterWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.like.LikeType;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeModifyService implements LikeModifyPort {

    private final LikeWritePort likeWritePort;
    private final LikeReadPort likeReadPort;
    private final LikeCounterWritePort likeCounterWritePort;
    private final MemberReadPort memberReadPort;
    private final TargetExistencePort targetExistencePort;

    @Override
    public void toggleLike(LikeType likeType, Long targetId, Long memberId) {
        validateTarget(likeType, targetId);

        Member member = findMemberByMemberIdOrThrow(memberId);

        boolean existsTargetLike = likeReadPort.existsTargetLike(likeType, targetId, member.getId());

        if (existsTargetLike) {
            likeWritePort.delete(likeType, targetId, member.getId());
            likeCounterWritePort.decrement(likeType, targetId);
        } else {
            likeWritePort.save(likeType, targetId, member.getId());
            likeCounterWritePort.increment(likeType, targetId);
        }
    }

    private void validateTarget(LikeType likeType, Long targetId) {
        if (!targetExistencePort.existsTarget(likeType, targetId)) {
            throw new DomainException(LikeErrorCode.TARGET_NOT_FOUND);
        }
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
