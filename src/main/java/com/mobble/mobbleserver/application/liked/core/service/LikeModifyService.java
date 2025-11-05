package com.mobble.mobbleserver.application.liked.core.service;

import com.mobble.mobbleserver.application.liked.core.port.provided.LikeModifyPort;
import com.mobble.mobbleserver.application.liked.core.port.required.LikeReadPort;
import com.mobble.mobbleserver.application.liked.core.port.required.LikeWritePort;
import com.mobble.mobbleserver.application.liked.core.port.required.TargetLookupPort;
import com.mobble.mobbleserver.application.liked.likeCounter.port.required.LikeCounterWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.application.liked.core.command.TargetInfoResult;
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
    private final TargetLookupPort targetLookupPort;

    @Override
    public void toggleLike(LikeType likeType, Long targetId, Long memberId) {
        validateTarget(likeType, targetId);

        Member member = findMemberOrThrow(memberId);

        boolean already = likeReadPort.existsTargetLike(likeType, targetId, member.getId());

        if (already) {
            likeWritePort.delete(likeType, targetId, member.getId());
            likeCounterWritePort.safeDecrement(likeType, targetId);
        } else {
            likeWritePort.save(likeType, targetId, member.getId());
            likeCounterWritePort.increment(likeType, targetId);
        }
    }

    private void validateTarget(LikeType likeType, Long targetId) {
        TargetInfoResult targetInfoResult = targetLookupPort.targetLoad(likeType, targetId);

        if (!targetInfoResult.exists()) {
            throw new DomainException(LikeErrorCode.TARGET_NOT_FOUND);
        }
    }

    private Member findMemberOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
