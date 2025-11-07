package com.mobble.mobbleserver.application.meeting.service;

import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingCreatePort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingDeletePort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingUpdatePort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingWritePort;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingErrorCode;
import com.mobble.mobbleserver.infrastructure.web.meeting.dto.request.MeetingRequestDto;
import com.mobble.mobbleserver.infrastructure.web.meeting.dto.request.MeetingUpdateRequestDto;
import com.mobble.mobbleserver.refactor.club.policy.ClubPermissionPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingModifyService implements MeetingCreatePort, MeetingUpdatePort, MeetingDeletePort {

    private final MeetingWritePort meetingWritePort;
    private final MeetingReadPort meetingReadPort;
    private final ClubMemberReadPort clubMemberReadPort;

    @Override
    public Meeting createMeeting(Long memberId, Long clubId, MeetingRequestDto dto) {
        ClubMember hostMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        ClubPermissionPolicy.validateLeaderOrManagerOrThrow(hostMember);

        Meeting meeting = dto.toEntity(hostMember);

        return meetingWritePort.save(meeting);
    }

    @Override
    public Meeting updateMeeting(Long memberId, Long clubId, Long meetingId, MeetingUpdateRequestDto dto) {
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        ClubPermissionPolicy.validateLeaderOrManagerOrThrow(clubMember);
        Meeting meeting = findMeetingByMeetingIdOrThrow(meetingId);

        return meeting.updateMeeting(
                dto.title(),
                dto.dateTime(),
                dto.location(),
                dto.cost(),
                dto.memberLimit(),
                dto.type()
        );
    }

    @Override
    public void deleteMeeting(Long memberId, Long clubId, Long meetingId) {
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        ClubPermissionPolicy.validateLeaderOrManagerOrThrow(clubMember);
        Meeting meeting = findMeetingByMeetingIdOrThrow(meetingId);

        meetingWritePort.delete(meeting);
    }

    private Meeting findMeetingByMeetingIdOrThrow(Long meetingId) {
        return meetingReadPort.findById(meetingId)
                .orElseThrow(() -> new DomainException(MeetingErrorCode.NOT_FOUND_MEETING));
    }

    private ClubMember findClubMemberByClubIdAndMemberIdOrThrow(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB));
    }
}
