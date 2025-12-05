package com.mobble.mobbleserver.domain.image;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;

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

    @Column(name = "size")
    private long size;

    @Column(name = "content_type", length = 50)
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private ImageType type;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Builder(access = AccessLevel.PRIVATE)
    private Image(
            String url,
            String originalName,
            long size,
            String contentType,
            ImageType type,
            boolean isDefault
    ) {
        this.url = url;
        this.originalName = originalName;
        this.size = size;
        this.contentType = contentType;
        this.type = type;
        this.isDefault = isDefault;
    }

    public static Image create(
            String url,
            String originalName,
            long size,
            String contentType,
            ImageType type
    ) {
        assertCreate(url, originalName, contentType, type);

        return Image.builder()
                .url(url)
                .originalName(originalName)
                .size(size)
                .contentType(contentType)
                .type(type)
                .isDefault(false)
                .build();
    }

    /* Assert 검증 */
    private static void assertCreate(String url, String originalName, String contentType, ImageType type) {
        requireNonNull(url, "url must not be null");
        requireNonNull(originalName, "original name must not be null");
        requireNonNull(contentType, "content type must not be null");
        requireNonNull(type, "image type must not be null");
    }
}
