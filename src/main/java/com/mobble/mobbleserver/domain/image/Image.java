package com.mobble.mobbleserver.domain.image;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "original_name")
    private String originalName;

    @Column(nullable = false)
    private long size;

    @Column(name = "content_type", length = 50)
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ImageType type;

    @Builder(access = AccessLevel.PRIVATE)
    private Image(
            String url,
            String originalName,
            long size,
            String contentType,
            ImageType type
    ) {
        this.url = url;
        this.originalName = originalName;
        this.size = size;
        this.contentType = contentType;
        this.type = type;
    }

    public static Image create(
            String url,
            String originalName,
            long size,
            String contentType,
            ImageType type
    ) {
        // Todo: assert 검증

        return Image.builder()
                .url(url)
                .originalName(originalName)
                .size(size)
                .contentType(contentType)
                .type(type)
                .build();
    }
}
