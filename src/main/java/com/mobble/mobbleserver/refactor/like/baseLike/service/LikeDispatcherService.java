package com.mobble.mobbleserver.refactor.like.baseLike.service;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.refactor.like.baseLike.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.refactor.like.baseLike.dto.response.LikeToggleResponseDto;
import com.mobble.mobbleserver.refactor.like.baseLike.entity.LikeType;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.member.validator.MemberValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LikeDispatcherService {

    private final MemberValidator memberValidator;
    private final Map<LikeType, AbstractLikeService<?, ?>> serviceMap;
    private final Map<LikeType, LikeQueryService> queryServiceMap;

    public LikeDispatcherService(MemberValidator memberValidator, List<AbstractLikeService<?, ?>> services, List<LikeQueryService> queryServices) {
        this.memberValidator = memberValidator;
        this.serviceMap = toEnumMap(services, AbstractLikeService::getType);
        this.queryServiceMap = toEnumMap(queryServices, LikeQueryService::getType);
    }

    @Transactional
    public LikeToggleResponseDto toggleLike(LikeType likeType, Long targetId, Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);
        AbstractLikeService<?, ?> service = serviceMap.get(likeType);

        if (service == null) throw new DomainException(LikeErrorCode.NOT_SUPPORTED_TYPE);

        return service.toggleLike(targetId, member);
    }

    public LikeMemberListResponseDto getMemberList(LikeType likeType, Long targetId) {
        LikeQueryService queryService = queryServiceMap.get(likeType);

        if (queryService == null) throw new DomainException(LikeErrorCode.NOT_SUPPORTED_TYPE);

        return queryService.getLikedMemberList(targetId);
    }

    // 생성자 중복 로직 메서드 분리
    private static <T> Map<LikeType, T> toEnumMap(List<T> beans, Function<T, LikeType> keyFn) {
        return beans.stream()
                .collect(Collectors.toMap(
                        keyFn,
                        Function.identity(),
                        (a, b) -> a,
                        () -> new EnumMap<>(LikeType.class)
                ));
    }
}
