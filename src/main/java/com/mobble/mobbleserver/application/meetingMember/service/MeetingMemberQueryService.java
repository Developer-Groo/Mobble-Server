package com.mobble.mobbleserver.application.meetingMember.service;

import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.application.meetingMember.port.provided.MeetingMemberQueryPort;
import com.mobble.mobbleserver.application.meetingMember.response.MeetingMemberResult;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingMemberQueryService implements MeetingMemberQueryPort {

    private final MemberReadPort memberReadPort;
    private final MeetingReadPort meetingReadPort;
    private final ClubReadPort clubReadPort;

    @Override
    public List<Long> getIsAttended(Long memberId, Long clubId) {
        Member member = assertMemberByMemberId(memberId);
        Club club = assertClubByClubId(clubId);

        List<Meeting> meetings = meetingReadPort.findMeetingsByClubId(club.getId());

        return meetings.stream()
                .filter(meeting -> meeting.hasAttendee(member.getId()))
                .map(Meeting::getId)
                .toList();
    }

    @Override
    public MeetingMemberResult getMeetingMembers(Long meetingId) {
        Meeting meeting = findMeetingByMeetingIdOrThrow(meetingId);
        List<Member> attendedMembers = meeting.getAttendedMembers();

        return MeetingMemberResult.create(meeting.getId(), attendedMembers);
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

    public Meeting findMeetingByMeetingIdOrThrow(Long meetingId) {
        return meetingReadPort.findById(meetingId)
                .orElseThrow(() -> new DomainException(MeetingErrorCode.NOT_FOUND_MEETING));
    }
}
