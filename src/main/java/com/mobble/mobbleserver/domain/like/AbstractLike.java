package com.mobble.mobbleserver.domain.like;

import com.mobble.mobbleserver.domain.common.exception.DomainException;
import com.mobble.mobbleserver.domain.like.error.LikeError;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    protected AbstractLike(Long memberId) {
        if (memberId == null) throw new DomainException(LikeError.REQUIRED_MEMBER);
        this.memberId = memberId;
    }
}
