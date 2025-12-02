package com.mobble.mobbleserver.application.meeting.service;

import com.mobble.mobbleserver.application.club.error.ClubBusinessError;
import com.mobble.mobbleserver.application.club.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingQueryPort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.application.meeting.result.MeetingResult;
import com.mobble.mobbleserver.application.member.error.MemberBusinessError;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.member.Member;
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
    private final MemberReadPort memberReadPort;
    private final ClubReadPort clubReadPort;

    @Override
    public List<MeetingResult> findMeetings(Long memberId, Long clubId) {
        Member member = assertMemberByMemberId(memberId);
        Club club = assertClubByClubId(clubId);

        List<Meeting> meetings = meetingReadPort.findMeetingsByClubId(club.getId());

        return MeetingResult.create(meetings, member.getId());
    }

    @Override
    public List<MeetingResult> findUpcomingMeetings(Long memberId, Long clubId) {
        Member member = assertMemberByMemberId(memberId);
        Club club = assertClubByClubId(clubId);

        LocalDateTime today = LocalDate.now().atStartOfDay();

        List<Meeting> meetings = meetingReadPort.findUpcomingMeetingsByClubId(club.getId(), today);

        return MeetingResult.create(meetings, member.getId());
    }

    /* ==== Private Helper ==== */
    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new BusinessException(MemberBusinessError.NOT_FOUND));
    }

    private Club assertClubByClubId(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new BusinessException(ClubBusinessError.NOT_FOUND));
    }
}
