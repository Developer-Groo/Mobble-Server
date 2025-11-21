package com.mobble.mobbleserver.application.meeting.service;

import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingQueryPort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingQueryService implements MeetingQueryPort {

    private final MeetingReadPort meetingReadPort;
    private final ClubReadPort clubReadPort;

    // 전체 미팅 조회
    @Override
    public List<Meeting> findMeetingsByClubId(Long clubId) {
        Club club = findClubByClubIdOrThrow(clubId);

        // Todo MeetingMember도 반환 해줘야 되지 않을까?
        return meetingReadPort.findByClubMember_Club_Id(club.getId());
    }

    // 다가오는 미팅 조회
    @Override
    public List<Meeting> findUpcomingMeetingsByClubId(Long memberId, Long clubId) {
        Club club = findClubByClubIdOrThrow(clubId);

        LocalDateTime today = LocalDate.now().atStartOfDay();

        return meetingReadPort.findUpcomingMeetingsByClubId(club.getId(), today);
    }

    private Club findClubByClubIdOrThrow(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new DomainException((ClubErrorCode.NOT_FOUND)));
    }
}
