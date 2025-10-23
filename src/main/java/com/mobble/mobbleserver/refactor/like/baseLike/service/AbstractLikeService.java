package com.mobble.mobbleserver.refactor.like.baseLike.service;

import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.like.baseLike.dto.response.LikeToggleResponseDto;
import com.mobble.mobbleserver.refactor.like.baseLike.entity.LikeType;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public abstract class AbstractLikeService<T, E> {

    @Transactional
    public LikeToggleResponseDto toggleLike(Long targetId, Member member) {
        T target = getTarget(targetId);

        Boolean isLiked = findExistingLike(target, member)
                .map(existing -> {
                    deleteLike(existing);
                    return false;
                })
                .orElseGet(() -> {
                    E like = createLike(target, member);
                    saveLike(like);
                    return true;
                });

        return LikeToggleResponseDto.toDto(targetId, isLiked);
    }

    /** Dispatcher 매핑용 식별자*/
    public abstract LikeType getType();

    /** 도메인별 Like 존재 조회 */
    protected abstract Optional<E> findExistingLike(T target, Member member);

    /** 새 Like 엔티티 생성 */
    protected abstract E createLike(T target, Member member);

    /** 저장 */
    protected abstract void saveLike(E entity);

    /** 삭제 */
    protected abstract void deleteLike(E entity);

    /** 대상 엔티티 로드 (Validator 호출 등) */
    protected abstract T getTarget(Long targetId);
}
