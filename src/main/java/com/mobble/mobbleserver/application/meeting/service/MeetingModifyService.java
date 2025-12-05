package com.mobble.mobbleserver.application.meeting.service;

import com.mobble.mobbleserver.application.clubMember.error.ClubMemberBusinessError;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.image.error.ImageBusinessError;
import com.mobble.mobbleserver.application.image.port.provided.ImageDeletePort;
import com.mobble.mobbleserver.application.image.port.required.ImageReadPort;
import com.mobble.mobbleserver.application.image.port.required.ImageWritePort;
import com.mobble.mobbleserver.application.meeting.command.CreateMeetingCommand;
import com.mobble.mobbleserver.application.meeting.command.UpdateMeetingCommand;
import com.mobble.mobbleserver.application.meeting.error.MeetingBusinessError;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingCreatePort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingDeletePort;
import com.mobble.mobbleserver.application.meeting.port.provided.MeetingUpdatePort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingReadPort;
import com.mobble.mobbleserver.application.meeting.port.required.MeetingWritePort;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.image.ImageType;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meeting.MeetingSchedule;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingModifyService implements MeetingCreatePort, MeetingUpdatePort, MeetingDeletePort {

    private final ImageDeletePort imageDeletePort;

    private final MeetingWritePort meetingWritePort;
    private final ImageWritePort imageWritePort;

    private final MeetingReadPort meetingReadPort;
    private final ClubMemberReadPort clubMemberReadPort;
    private final ImageReadPort imageReadPort;

    @Override
    public Meeting createMeeting(CreateMeetingCommand command) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(command.clubId(), command.memberId());
        Image mainImage = resolveMainImage(command.mainImageId());

        assertCanManageMeeting(clubMember);

        Club club = clubMember.getClub();
        Member owner = clubMember.getMember();

        MeetingSchedule meetingSchedule = MeetingSchedule.of(command.schedule());

        Meeting meeting = Meeting.create(
                club,
                owner,
                command.title(),
                mainImage,
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

        Image mainImage = resolveMainImage(command.mainImageId());
        MeetingSchedule meetingSchedule = MeetingSchedule.of(command.schedule());

        Meeting meeting = assertMeetingByMeetingId(command.meetingId());

        return meeting.update(
                command.title(),
                mainImage,
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

        imageWritePort.delete(meeting.getMainImage());
        meetingWritePort.delete(meeting);
    }

    @Override
    public void deleteAll(Long memberId, Long clubId) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(clubId, memberId);
        assertLeader(clubMember);

        List<Meeting> meetings = meetingReadPort.findMeetingsByClubId(clubId);
        if (meetings.isEmpty()) return;

        List<Long> imageIds = meetingReadPort.findMainImageIdsByClubId(clubId);

        imageDeletePort.deleteAll(imageIds);
        meetingWritePort.deleteAll(meetings);
    }

    /* ==== Private Helper ==== */
    private Meeting assertMeetingByMeetingId(Long meetingId) {
        return meetingReadPort.findById(meetingId)
                .orElseThrow(() -> new BusinessException(MeetingBusinessError.NOT_FOUND));
    }

    private ClubMember assertClubMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new BusinessException(ClubMemberBusinessError.NOT_JOINED_CLUB));
    }

    private void assertCanManageMeeting(ClubMember clubMember) {
        if (!clubMember.canManage()) throw new BusinessException(ClubMemberBusinessError.NO_PERMISSION);
    }

    private void assertLeader(ClubMember clubMember) {
        if (!clubMember.isLeader()) throw new BusinessException(ClubMemberBusinessError.ONLY_LEADER_ALLOWED);
    }

    private Image assertImageByImageId(Long imageId) {
        return imageReadPort.findById(imageId)
                .orElseThrow(() -> new BusinessException(ImageBusinessError.NOT_FOUND));
    }

    private Image assertDefaultImageByImageType() {
        return imageReadPort.findDefaultByType(ImageType.MEETING_MAIN)
                .orElseThrow(() -> new BusinessException(ImageBusinessError.NOT_FOUND));
    }

    private Image resolveMainImage(Long imageId) {
        return (imageId == null)
                ? assertDefaultImageByImageType()
                : assertImageByImageId(imageId);
    }
}
