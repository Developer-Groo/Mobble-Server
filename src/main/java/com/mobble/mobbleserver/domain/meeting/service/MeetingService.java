package com.mobble.mobbleserver.domain.meeting.service;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.meeting.dto.request.MeetingRequestDto;
import com.mobble.mobbleserver.domain.meeting.dto.request.MeetingUpdateRequestDto;
import com.mobble.mobbleserver.domain.meeting.dto.response.MeetingResponseDto;
import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.repository.MeetingRepository;
import com.mobble.mobbleserver.domain.meeting.validator.MeetingValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingValidator meetingValidator;
    private final ClubMemberValidator clubMemberValidator;

    @Transactional
    public MeetingResponseDto createMeeting(Long memberId, Long clubId, MeetingRequestDto dto) {
        ClubMember hostMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        Meeting meeting = dto.toEntity(hostMember);
        //Todo d-day 표시 추가

        Meeting saveMeeting = meetingRepository.save(meeting);
        int attendeeCount = 0;

        return MeetingResponseDto.toDto(saveMeeting, attendeeCount);
    }

    public List<MeetingResponseDto> findMeetingsByClubId(Long memberId, Long clubId) {
        clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        List<Meeting> meetings = meetingRepository.findByClubMember_Club_Id(clubId);


        return meetings.stream()
                .map(meeting -> MeetingResponseDto.toDto(meeting, meeting.getMeetingMembers().size()))
                .toList();
    }

    @Transactional
    public MeetingResponseDto updateMeeting(Long memberId, Long meetingId, MeetingUpdateRequestDto dto) {
        Meeting meeting = meetingValidator.findMeetingByMeetingIdOrThrow(meetingId);

        Long clubId = meeting.getClubMember().getClub().getId();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        ClubMemberRole clubMemberRole = clubMember.getClubMemberRole();

        if (clubMemberRole == ClubMemberRole.MEMBER) throw new IllegalArgumentException(""); //Todo 커스텀 예외 적용

        meeting.updateMeeting(
                dto.title(),
                dto.dateTime(),
                dto.location(),
                dto.cost(),
                dto.memberLimit(),
                dto.type()
        );

        Meeting updateMeeting = meetingRepository.save(meeting);
        int attendeeCount = meeting.getMeetingMembers().size();

        return MeetingResponseDto.toDto(updateMeeting, attendeeCount);
    }

    @Transactional
    public void deleteMeeting(Long memberId, Long meetingId) {
        Meeting meeting = meetingValidator.findMeetingByMeetingIdOrThrow(meetingId);

        Long clubId = meeting.getClubMember().getClub().getId();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        ClubMemberRole clubMemberRole = clubMember.getClubMemberRole();

        if (clubMemberRole == ClubMemberRole.MEMBER) throw new IllegalArgumentException(""); //Todo 커스텀 예외 적용

        meetingRepository.delete(meeting);
    }
}
