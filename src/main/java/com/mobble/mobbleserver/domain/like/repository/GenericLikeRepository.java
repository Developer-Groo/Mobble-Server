package com.mobble.mobbleserver.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericLikeRepository<E> extends JpaRepository<E, Long> {

}
