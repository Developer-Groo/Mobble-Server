package com.mobble.mobbleserver.infrastructure.persistence.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberPersistenceAdapter {

    private final JpaMemberRepository repository;
}
