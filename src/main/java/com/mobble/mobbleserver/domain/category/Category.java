package com.mobble.mobbleserver.domain.category;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private CategoryCode code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoryTargetType targetType;

    @Builder(access = AccessLevel.PRIVATE)
    private Category(CategoryCode code, CategoryTargetType targetType) {
        this.code = code;
        this.targetType = targetType;
    }

    public static Category create(CategoryCode code, CategoryTargetType targetType) {
        if (!code.supports(targetType))
            throw new IllegalArgumentException("CategoryCode " + code + " does not support targetType " + targetType);

        return Category.builder()
                .code(code)
                .targetType(targetType)
                .build();
    }
}
