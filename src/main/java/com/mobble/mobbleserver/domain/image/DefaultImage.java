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
public class DefaultImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "default_image_id")
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "content_type", nullable = false, length = 50)
    private String contentType;

    @Enumerated
    @Column(name = "type", nullable = false, length = 30)
    private ImageType type;

    @Builder(access = AccessLevel.PRIVATE)
    private DefaultImage(String url, String contentType, ImageType type) {
        this.url = url;
        this.contentType = contentType;
        this.type = type;
    }

    public static DefaultImage create(String url, String contentType, ImageType type) {
        assertCreate(url, contentType, type);

        return DefaultImage.builder()
                .url(url)
                .contentType(contentType)
                .type(type)
                .build();
    }

    /* Assert 검증 */
    private static void assertCreate(String url, String contentType, ImageType type) {
        requireNonNull(url, "default image url must not be null");
        requireNonNull(contentType, "default image content type must not be null");
        requireNonNull(type, "default image type must not be null");
    }
}
