package com.mobble.mobbleserver.domain.club;

import com.mobble.mobbleserver.domain.category.Category;
import com.mobble.mobbleserver.domain.club.error.ClubError;
import com.mobble.mobbleserver.domain.common.BaseEntity;
import com.mobble.mobbleserver.domain.common.Location;
import com.mobble.mobbleserver.domain.exception.DomainException;
import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private Member leader;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_image_id")
    private Image mainImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Embedded
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", nullable = false)
    private AgeGroup ageGroup;

    @Column(name = "description", length = 300)
    private String description;

    @Column(name = "isAutoJoin")
    private boolean isAutoJoin;

    @Column(name = "member_count", nullable = false)
    private int memberCount;

    @Builder(access = AccessLevel.PRIVATE)
    private Club(
            String name,
            Member leader,
            Image mainImage,
            Category category,
            Location location,
            AgeGroup ageGroup,
            String description,
            boolean isAutoJoin,
            int memberCount
    ) {
        this.name = name;
        this.leader = leader;
        this.mainImage = mainImage;
        this.category = category;
        this.location = location;
        this.ageGroup = ageGroup;
        this.description = description;
        this.isAutoJoin = isAutoJoin;
        this.memberCount = memberCount;
    }

    public static Club create(
            String name,
            Member leader,
            Image mainImage,
            Category category,
            Location location,
            AgeGroup ageGroup,
            String description,
            boolean isAutoJoin
    ) {
        assertCreate(name, leader, mainImage, category, location, ageGroup);
        assertName(name);

        return Club.builder()
                .name(name)
                .leader(leader)
                .mainImage(mainImage)
                .category(category)
                .location(location)
                .ageGroup(ageGroup)
                .description(description)
                .isAutoJoin(isAutoJoin)
                .memberCount(1)
                .build();
    }

    public void update(
            String name,
            Image mainImage,
            Category category,
            Location location,
            AgeGroup ageGroup,
            String description,
            boolean isAutoJoin
    ) {
        assertUpdate(name, mainImage, category, location, ageGroup);
        assertName(name);

        this.name = name;
        this.mainImage = mainImage;
        this.category = category;
        this.location = location;
        this.ageGroup = ageGroup;
        this.description = description;
        this.isAutoJoin = isAutoJoin;
    }

    public void increaseMemberCount() {
        this.memberCount++;
    }

    public void decreaseMemberCount() {
        this.memberCount--;
        if (this.memberCount < 0) this.memberCount = 0;
    }

    /* Assert 검증 */
    private static void assertName(String name) {
        if (name.isBlank() || name.length() > 20) throw new DomainException(ClubError.CONTENT_TOO_LONG);
    }

    private static void assertCreate(
            String name,
            Member leader,
            Image mainImage,
            Category category,
            Location location,
            AgeGroup ageGroup
    ) {
        requireNonNull(name, "name must not be null");
        requireNonNull(leader, "leader must not be null");
        requireNonNull(mainImage, "main image must not be null");
        requireNonNull(category, "category must not be null");
        requireNonNull(location, "location must not be null");
        requireNonNull(ageGroup, "age group must not be null");
    }

    private static void assertUpdate(
            String name,
            Image mainImage,
            Category category,
            Location location,
            AgeGroup ageGroup
    ) {
        requireNonNull(name, "name must not be null");
        requireNonNull(mainImage, "main image must not be null");
        requireNonNull(category, "category must not be null");
        requireNonNull(location, "location must not be null");
        requireNonNull(ageGroup, "age group must not be null");
    }
}
