package com.mobble.mobbleserver.application.meeting.service;

import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.meeting.command.response.MeetingResult;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingQueryPort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.application.meetingMember.port.provided.MeetingMemberQueryPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
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

    private final MeetingMemberQueryPort meetingMemberQueryPort;

    private final MeetingReadPort meetingReadPort;
    private final MemberReadPort memberReadPort;
    private final ClubReadPort clubReadPort;

    // 전체 미팅 조회
    @Override
    public List<MeetingResult> findMeetings(Long memberId, Long clubId) {
        Member member = assertMemberByMemberId(memberId);
        Club club = assertClubByClubId(clubId);

        List<Meeting> meetings = meetingReadPort.findMeetingsByClubId(club.getId());
        List<Long> atendedList = meetingMemberQueryPort.getIsAttended(member.getId(), club.getId());

        return MeetingResult.create(meetings, atendedList);
    }

    // 다가오는 미팅 조회
    @Override
    public List<MeetingResult> findUpcomingMeetings(Long memberId, Long clubId) {
        Member member = assertMemberByMemberId(memberId);
        Club club = assertClubByClubId(clubId);

        LocalDateTime today = LocalDate.now().atStartOfDay();

        List<Meeting> meetings = meetingReadPort.findUpcomingMeetingsByClubId(club.getId(), today);
        List<Long> attendedList = meetingMemberQueryPort.getIsAttended(member.getId(), club.getId());

        return MeetingResult.create(meetings, attendedList);
    }

    /* ==== Private Helper ==== */
    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
    private Club assertClubByClubId(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new DomainException(ClubErrorCode.NOT_FOUND));
    }
}
