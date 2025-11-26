package com.mobble.mobbleserver.domain.like.counter;

import com.mobble.mobbleserver.domain.like.LikeType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "like_counter",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_like_counter_type_target",
                        columnNames = {"like_type", "target_id"}
                )
        }
)
public class LikeCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_counter_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "like_type", nullable = false, length = 20)
    private LikeType likeType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "cnt", nullable = false)
    private long count;

    @Builder(access = AccessLevel.PRIVATE)
    private LikeCounter(LikeType likeType, Long targetId, Long count) {
        validateTypeAndTarget(likeType, targetId);
        this.likeType = likeType;
        this.targetId = targetId;
        this.count = Math.max(count, 0);
    }

    public static LikeCounter create(LikeType likeType, Long targetId) {
        return LikeCounter.builder()
                .likeType(likeType)
                .targetId(targetId)
                .count(0L)
                .build();
    }

    private void validateTypeAndTarget(LikeType likeType, Long targetId) {
        requireNonNull(likeType, "likeType must not be null");
        requireNonNull(targetId, "targetId must not be null");
    }
}
