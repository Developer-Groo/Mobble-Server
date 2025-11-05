package com.mobble.mobbleserver.application.like.likeCounter.service;

import com.mobble.mobbleserver.application.like.likeCounter.port.provided.LikeCounterModifyPort;
import com.mobble.mobbleserver.application.like.likeCounter.port.required.LikeCounterWritePort;
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
        likeCounterWritePort.increment(likeType, targetId);
    }

    @Override
    public void decrement(LikeType likeType, Long targetId) {
        likeCounterWritePort.decrement(likeType, targetId);
    }
}
