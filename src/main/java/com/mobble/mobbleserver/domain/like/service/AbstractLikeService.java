package com.mobble.mobbleserver.domain.like.service;

import com.mobble.mobbleserver.domain.like.dto.response.LikeToggleResponseDto;
import com.mobble.mobbleserver.domain.like.entity.LikeType;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
public abstract class AbstractLikeService<T, E> implements LikeStrategy {

    protected final MemberValidator memberValidator;

    @Override
    public LikeToggleResponseDto toggleLike(Long targetId, Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        T target = getTarget(targetId);

        Optional<E> existing = findExistingLike(target, member);
        boolean isLiked = existing.isEmpty();

        if (isLiked) saveLike(createLike(target, member));
        else deleteLike(existing.get());

        return LikeToggleResponseDto.toDto(targetId, isLiked);
    }

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

    /** 이 전략이 처리할 타입 */
    @Override
    public abstract LikeType getType();
}
