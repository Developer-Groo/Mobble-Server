package com.mobble.mobbleserver.application.meeting.service;

import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.meeting.command.request.CreateMeetingCommand;
import com.mobble.mobbleserver.application.meeting.command.request.UpdateMeetingCommand;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingCreatePort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingDeletePort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingUpdatePort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingWritePort;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meeting.MeetingSchedule;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingErrorCode;
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
    public Meeting createMeeting(CreateMeetingCommand command) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(command.clubId(), command.memberId());
        assertCanManageMeeting(clubMember);

        MeetingSchedule meetingSchedule = MeetingSchedule.of(command.schedule());

        Meeting meeting = Meeting.createMeeting(
                clubMember,
                command.title(),
                meetingSchedule,
                command.location(),
                command.cost(),
                command.memberLimit(),
                command.type()
        );

        return meetingWritePort.save(meeting);
    }

    @Override
    public Meeting updateMeeting(UpdateMeetingCommand command) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(command.clubId(), command.memberId());
        assertCanManageMeeting(clubMember);

        MeetingSchedule meetingSchedule = MeetingSchedule.of(command.schedule());

        Meeting meeting = assertMeetingByMeetingId(command.meetingId());

        return meeting.updateMeeting(
                command.title(),
                meetingSchedule,
                command.location(),
                command.cost(),
                command.memberLimit(),
                command.type()
        );
    }

    @Override
    public void deleteMeeting(Long memberId, Long clubId, Long meetingId) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(clubId, memberId);
        assertCanManageMeeting(clubMember);

        Meeting meeting = assertMeetingByMeetingId(meetingId);

        meetingWritePort.delete(meeting);
    }

    /* ==== Private Helper ==== */
    private Meeting assertMeetingByMeetingId(Long meetingId) {
        return meetingReadPort.findById(meetingId)
                .orElseThrow(() -> new DomainException(MeetingErrorCode.NOT_FOUND_MEETING));
    }

    private ClubMember assertClubMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB));
    }

    private void assertCanManageMeeting(ClubMember clubMember) {
        if (!clubMember.canManage()) throw new DomainException(ClubMemberErrorCode.NO_PERMISSION);
    }
}
