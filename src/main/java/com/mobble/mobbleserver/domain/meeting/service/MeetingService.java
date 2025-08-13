package com.mobble.mobbleserver.domain.meeting.service;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.meeting.dto.request.MeetingRequestDto;
import com.mobble.mobbleserver.domain.meeting.dto.response.MeetingResponseDto;
import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.repository.MeetingRepository;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MemberValidator memberValidator;
    private final ClubMemberValidator clubMemberValidator;

    @Transactional
    public MeetingResponseDto createMeeting(Long memberId, Long clubId, MeetingRequestDto dto) {
        ClubMember hostMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        ClubMemberRole clubMemberRole = hostMember.getClubMemberRole();

        if (clubMemberRole == ClubMemberRole.MEMBER) {
            throw new IllegalArgumentException(""); //Todo 커스텀 예외 적용
        }

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

    private Meeting findMeetingByMeetingId(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException(""));
    }
}
