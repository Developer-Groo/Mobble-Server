package com.mobble.mobbleserver.domain.meeting.service;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.meeting.dto.request.MeetingRequestDto;
import com.mobble.mobbleserver.domain.meeting.dto.request.MeetingUpdateRequestDto;
import com.mobble.mobbleserver.domain.meeting.dto.response.MeetingResponseDto;
import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final ClubMemberValidator clubMemberValidator;

    @Transactional
    public MeetingResponseDto createMeeting(Long memberId, Long clubId, MeetingRequestDto dto) {
        ClubMember hostMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        Meeting meeting = dto.toEntity(hostMember);
        //Todo d-day 표시 추가

        return MeetingResponseDto.toDto(meetingRepository.save(meeting));
    }

    public List<MeetingResponseDto> findMeetingsByClubId(Long memberId, Long clubId) {
        clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        List<Meeting> meetings = meetingRepository.findByClubMember_Club_Id(clubId);

        return meetings.stream()
                .map(meeting -> MeetingResponseDto.toDto(meeting))
                .toList();
    }

    public MeetingResponseDto findMeetingById(Long meetingId, Long memberId) {
        Meeting meeting = findMeetingByMeetingId(meetingId); //Todo 커스텀 예외 적용

        Long clubId = meeting.getClubMember().getClub().getId();
        clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        return MeetingResponseDto.toDto(meeting);
    }

    @Transactional
    public MeetingResponseDto updateMeeting(Long memberId, Long meetingId, MeetingUpdateRequestDto dto) {
        Meeting meeting = findMeetingByMeetingId(meetingId);

        Long clubId = meeting.getClubMember().getClub().getId();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        ClubMemberRole clubMemberRole = clubMember.getClubMemberRole();

        if (clubMemberRole == ClubMemberRole.MEMBER) {
            throw new IllegalArgumentException(""); //Todo 커스텀 예외 적용?
        }

        meeting.updateMeeting(
                dto.title(),
                dto.dateTime(),
                dto.location(),
                dto.cost(),
                dto.memberLimit(),
                dto.type()
        );

        return MeetingResponseDto.toDto(meetingRepository.save(meeting));
    }

    @Transactional
    public void deleteMeeting(Long memberId, Long meetingId) {
        Meeting meeting = findMeetingByMeetingId(meetingId);

        Long clubId = meeting.getClubMember().getClub().getId();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        ClubMemberRole clubMemberRole = clubMember.getClubMemberRole();

        if (clubMemberRole == ClubMemberRole.MEMBER) {
            throw new IllegalArgumentException(""); //Todo 커스텀 예외 적용?
        }

        meetingRepository.delete(meeting);
    }

    private Meeting findMeetingByMeetingId(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException(""));
    }
}
