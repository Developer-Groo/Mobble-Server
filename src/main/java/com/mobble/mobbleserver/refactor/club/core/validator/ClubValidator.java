package com.mobble.mobbleserver.refactor.club.core.validator;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.club.core.repository.ClubRepository;
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
