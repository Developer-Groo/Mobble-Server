package com.mobble.mobbleserver.domain.club.core.validator;

import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.club.core.repository.ClubRepository;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubValidatorTest {
    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private ClubValidator clubValidator;

    private static final Long CLUB_ID = 1L;

    private Club mockClub;

    @BeforeEach
    void setUp() {
        mockClub = mock(Club.class);
    }

    @Test
    @DisplayName("클럽 ID로 조회 성공")
    void success_when_find_club_or_throw() {
        // given
        given(clubRepository.findById(CLUB_ID)).willReturn(Optional.of(mockClub));

        // when
        Club club = clubValidator.findClubByClubIdOrThrow(CLUB_ID);

        // then
        assertThat(club).isEqualTo(mockClub);
        verify(clubRepository, times(1)).findById(CLUB_ID);
    }
