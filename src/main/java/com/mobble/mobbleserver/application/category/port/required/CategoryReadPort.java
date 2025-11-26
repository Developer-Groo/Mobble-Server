package com.mobble.mobbleserver.application.category.port.required;

import com.mobble.mobbleserver.domain.category.Category;
import com.mobble.mobbleserver.domain.category.CategoryCode;

import java.util.Optional;

public interface CategoryReadPort {

    Optional<Category> findByCode(CategoryCode code);
}
