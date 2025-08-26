package com.mobble.mobbleserver.domain.club.core.validator;

import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.club.core.repository.ClubRepository;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubValidator {

    private final ClubRepository clubRepository;

    public Club findClubByClubIdOrThrow(Long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new DomainException(ClubErrorCode.NOT_FOUND));
    }
}
