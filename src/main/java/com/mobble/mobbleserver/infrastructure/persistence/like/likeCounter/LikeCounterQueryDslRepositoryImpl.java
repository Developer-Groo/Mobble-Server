package com.mobble.mobbleserver.infrastructure.persistence.like.likeCounter;

import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.domain.like.counter.LikeCounter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;

import static com.mobble.mobbleserver.domain.like.counter.QLikeCounter.likeCounter;

@RequiredArgsConstructor
public class LikeCounterQueryDslRepositoryImpl implements LikeCounterQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public long increment(LikeType likeType, Long targetId) {
        return queryFactory
                .update(likeCounter)
                .set(likeCounter.count, likeCounter.count.add(1))
                .where(likeCounter.likeType.eq(likeType),
                        likeCounter.targetId.eq(targetId))
                .execute();

    }

    @Override
    public long safeDecrement(LikeType likeType, Long targetId) {
        return queryFactory
                .update(likeCounter)
                .set(likeCounter.count, likeCounter.count.subtract(1))
                .where(likeCounter.likeType.eq(likeType),
                        likeCounter.targetId.eq(targetId),
                        likeCounter.count.gt(0L))
                .execute();
    }


    @Override
    public void createIfAbsent(LikeType likeType, Long targetId) {
        try {
            entityManager.persist(LikeCounter.create(likeType, targetId));
            entityManager.flush();
        } catch (PersistenceException e) {
            entityManager.clear();
        }
    }
}
