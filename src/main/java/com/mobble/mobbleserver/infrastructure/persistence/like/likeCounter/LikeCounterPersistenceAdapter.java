package com.mobble.mobbleserver.infrastructure.persistence.like.likeCounter;

import com.mobble.mobbleserver.application.liked.likeCounter.port.required.LikeCounterReadPort;
import com.mobble.mobbleserver.application.liked.likeCounter.port.required.LikeCounterWritePort;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LikeCounterPersistenceAdapter implements LikeCounterWritePort, LikeCounterReadPort {

    private final JpaLikeCounterRepository jpaLikeCounterRepository;

    /**
     * LikeCounterWritePort
     */
    @Override
    public void increment(LikeType likeType, Long targetId) {
        retryOnDeadlock(() -> jpaLikeCounterRepository.upsertIncrement(likeType.name(), targetId));
    }

    @Override
    public void safeDecrement(LikeType likeType, Long targetId) {
        retryOnDeadlock(() -> jpaLikeCounterRepository.safeDecrement(likeType.name(), targetId));
    }

    /**
     * LikeCounterReadPort
     */
    @Override
    public List<LikeCounter> findAllByLikeTypeAndTargetIds(LikeType likeType, List<Long> targetIds) {
        return jpaLikeCounterRepository.findAllByLikeTypeAndTargetIdIn(likeType, targetIds);
    }

    /**
     * Deadlock 발생 시 재시도 로직
     */
    private void retryOnDeadlock(Runnable dbOperation) {
        for (int attempt = 0; attempt < 2; attempt++) { // 기본 1회 + 재시도 1회
            try {
                dbOperation.run();
                return; // 성공 시 종료
            } catch (Exception e) {
                if (isDeadlock(e) && attempt == 0) { // 첫 시도에서만 재시도
                    log.warn("[LikeCounter] Deadlock detected. Retrying once...");

                    try {
                        Thread.sleep(50); // 50ms 대기 후 재시도
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt(); // 상태 복원
                    }
                    continue;
                }
                throw e; // Deadlock이 아니거나 재시도 후 실패면 예외 전파
            }
        }
    }

    /**
     * Deadlock 예외 감지
     */
    private boolean isDeadlock(Throwable e) {
        while (e != null) {
            String msg = e.getMessage();

            if (msg != null && (msg.contains("Deadlock found") || msg.contains("1213"))) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }
}
