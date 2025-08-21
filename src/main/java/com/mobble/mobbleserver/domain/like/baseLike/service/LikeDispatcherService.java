package com.mobble.mobbleserver.domain.like.baseLike.service;

import com.mobble.mobbleserver.domain.like.baseLike.dto.response.LikeToggleResponseDto;
import com.mobble.mobbleserver.domain.like.baseLike.entity.LikeType;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
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

    public LikeDispatcherService(MemberValidator memberValidator, List<AbstractLikeService<?, ?>> services) {
        this.memberValidator = memberValidator;
        this.serviceMap = services.stream()
                .collect(Collectors.toMap(
                        AbstractLikeService::getType,
                        Function.identity(),
                        (a, b) -> a,
                        () -> new EnumMap<>(LikeType.class)
                ));
    }

    @Transactional
    public LikeToggleResponseDto toggleLike(LikeType likeType, Long targetId, Long memberId) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        AbstractLikeService<?, ?> service = serviceMap.get(likeType);

        if (service == null) throw new DomainException(LikeErrorCode.NOT_SUPPORTED_TYPE);

        return service.toggleLike(targetId, member);
    }
}
