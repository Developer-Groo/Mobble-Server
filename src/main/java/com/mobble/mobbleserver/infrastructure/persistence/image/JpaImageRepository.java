package com.mobble.mobbleserver.infrastructure.persistence.image;

import com.mobble.mobbleserver.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaImageRepository extends JpaRepository<Image, Long> {
}
