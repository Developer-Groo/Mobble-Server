package com.mobble.mobbleserver.application.meeting.service;

import com.mobble.mobbleserver.application.meeting.port.provided.MeetingCreatePort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingDeletePort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingUpdatePort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingWritePort;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingErrorCode;
import com.mobble.mobbleserver.infrastructure.web.meeting.dto.request.MeetingRequestDto;
import com.mobble.mobbleserver.infrastructure.web.meeting.dto.request.MeetingUpdateRequestDto;
import com.mobble.mobbleserver.refactor.club.policy.ClubPermissionPolicy;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.validator.ClubMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingModifyService implements MeetingCreatePort, MeetingUpdatePort, MeetingDeletePort {

    private final ClubMemberValidator clubMemberValidator;

    private final MeetingWritePort meetingWritePort;
    private final MeetingReadPort meetingReadPort;

    @Override
    public Meeting createMeeting(Long memberId, Long clubId, MeetingRequestDto dto) {
        ClubMember hostMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        ClubPermissionPolicy.validateLeaderOrManagerOrThrow(hostMember);

        Meeting meeting = dto.toEntity(hostMember);

        return meetingWritePort.save(meeting);

//        int attendeeCount = 0;
//        int dDay = calculateDDay(saveMeeting.getDatetime());
//
//        return MeetingResponseDto.toDto(saveMeeting, attendeeCount, dDay)
    }

    @Override
    public Meeting updateMeeting(Long memberId, Long clubId, Long meetingId, MeetingUpdateRequestDto dto) {
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
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

//        Meeting updateMeeting = meetingWritePort.save(meeting);
//        int attendeeCount = meeting.getMeetingMembers().size();
//        int dDay = calculateDDay(updateMeeting.getDatetime());
//
//        return MeetingResponseDto.toDto(updateMeeting, attendeeCount, dDay);
    }

    @Override
    public void deleteMeeting(Long memberId, Long clubId, Long meetingId) {
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        ClubPermissionPolicy.validateLeaderOrManagerOrThrow(clubMember);
        Meeting meeting = findMeetingByMeetingIdOrThrow(meetingId);

        meetingWritePort.delete(meeting);
    }

    private Meeting findMeetingByMeetingIdOrThrow(Long meetingId) {
        return meetingReadPort.findById(meetingId)
                .orElseThrow(() -> new DomainException(MeetingErrorCode.NOT_FOUND_MEETING));
    }
}
