package com.mobble.mobbleserver.infrastructure.persistence.category;

import com.mobble.mobbleserver.domain.category.Category;
import com.mobble.mobbleserver.domain.category.CategoryCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCode(CategoryCode code);
}
