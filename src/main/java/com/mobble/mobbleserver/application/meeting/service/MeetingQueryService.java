package com.mobble.mobbleserver.application.meeting.service;

import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingQueryPort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingQueryService implements MeetingQueryPort {

    private final MeetingReadPort meetingReadPort;
    private final ClubMemberReadPort clubMemberReadPort;

    @Override
    public List<Meeting> findMeetingsByClubId(Long memberId, Long clubId) {
        clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId);

        return meetingReadPort.findByClubMember_Club_Id(clubId);
    }
}
