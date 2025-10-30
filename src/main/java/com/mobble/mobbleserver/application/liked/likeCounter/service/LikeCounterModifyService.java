package com.mobble.mobbleserver.application.liked.likeCounter.service;

import com.mobble.mobbleserver.application.liked.likeCounter.port.provided.LikeCounterModifyPort;
import com.mobble.mobbleserver.application.liked.likeCounter.port.required.LikeCounterWritePort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeCounterModifyService implements LikeCounterModifyPort {

    private final LikeCounterWritePort likeCounterWritePort;

    @Override
    public void increment(LikeType likeType, Long targetId) {
        long updateCounter = likeCounterWritePort.increment(likeType, targetId);

        if (updateCounter == 0) {
            likeCounterWritePort.createIfAbsent(likeType, targetId);
            likeCounterWritePort.increment(likeType, targetId);
        }
    }

    @Override
    public void decrement(LikeType likeType, Long targetId) {
        likeCounterWritePort.safeDecrement(likeType, targetId);
    }
}
