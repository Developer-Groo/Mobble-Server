package com.mobble.mobbleserver.application.liked.likeCounter.service;

import com.mobble.mobbleserver.application.liked.likeCounter.port.provided.LikeCounterQueryPort;
import com.mobble.mobbleserver.application.liked.likeCounter.port.required.LikeCounterReadPort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeCounterQueryService implements LikeCounterQueryPort {

    private final LikeCounterReadPort likeCounterReadPort;

    @Override
    public long getCount(LikeType likeType, Long targetId) {
        return likeCounterReadPort.findByLikeTypeAndTargetId(likeType, targetId)
                .map(LikeCounter::getCount)
                .orElse(0L);
    }
}
