package com.mobble.mobbleserver.infrastructure.persistence.category;

import com.mobble.mobbleserver.application.category.port.required.CategoryReadPort;
import com.mobble.mobbleserver.application.category.port.required.CategoryWritePort;
import com.mobble.mobbleserver.domain.category.Category;
import com.mobble.mobbleserver.domain.category.CategoryCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryReadPort, CategoryWritePort {

    private final JpaCategoryRepository repository;

    @Override
    public Optional<Category> findByCode(CategoryCode code) {
        return repository.findByCode(code);
    }
}
