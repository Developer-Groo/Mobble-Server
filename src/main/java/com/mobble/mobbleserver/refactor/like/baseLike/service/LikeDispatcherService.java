package com.mobble.mobbleserver.refactor.like.baseLike.service;

import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.infrastructure.web.like.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.infrastructure.web.like.dto.response.LikeToggleResponseDto;
import com.mobble.mobbleserver.domain.like.baseLike.LikeType;
import com.mobble.mobbleserver.domain.member.Member;
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

    private final Map<LikeType, AbstractLikeService<?, ?>> serviceMap;
    private final Map<LikeType, LikeQueryService> queryServiceMap;

    private final MemberReadPort memberReadPort;

    public LikeDispatcherService(MemberReadPort memberReadPort, List<AbstractLikeService<?, ?>> services, List<LikeQueryService> queryServices) {
        this.memberReadPort = memberReadPort;
        this.serviceMap = toEnumMap(services, AbstractLikeService::getType);
        this.queryServiceMap = toEnumMap(queryServices, LikeQueryService::getType);
    }

    @Transactional
    public LikeToggleResponseDto toggleLike(LikeType likeType, Long targetId, Long memberId) {
        Member member = findMemberByMemberIdOrThrow(memberId);
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

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
