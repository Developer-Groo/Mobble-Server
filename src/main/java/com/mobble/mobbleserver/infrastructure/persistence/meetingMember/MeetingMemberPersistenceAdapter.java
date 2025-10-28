package com.mobble.mobbleserver.infrastructure.persistence.meetingMember;

import com.mobble.mobbleserver.application.meetingMember.port.required.MeetingMemberReadPort;
import com.mobble.mobbleserver.application.meetingMember.port.required.MeetingMemberWritePort;
import com.mobble.mobbleserver.domain.meetingMember.MeetingMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MeetingMemberPersistenceAdapter implements MeetingMemberWritePort, MeetingMemberReadPort {

    private final JpaMeetingMemberRepository jpaMeetingMemberRepository;

    /* MeetingMemberWritePort*/
    @Override
    public MeetingMember save(MeetingMember meetingMember) {
        return jpaMeetingMemberRepository.save(meetingMember);
    }

    @Override
    public void delete(MeetingMember meetingMember) {
        jpaMeetingMemberRepository.delete(meetingMember);
    }

    /* MeetingMemberReadPort*/
    @Override
    public Optional<MeetingMember> findMeetingMemberByMeetingIdAndMemberId(Long meetingId, Long memberId) {
        return jpaMeetingMemberRepository.findMeetingMemberByMeetingIdAndMemberId(meetingId, memberId);
    }

    @Override
    public boolean existsByMeeting_IdAndMember_Id(Long meetingId, Long memberId) {
        return jpaMeetingMemberRepository.existsByMeeting_IdAndMember_Id(meetingId, memberId);
    }

    @Override
    public List<MeetingMember> findByMeetingId(Long meetingId) {
        return jpaMeetingMemberRepository.findByMeetingId(meetingId);
    }

    @Override
    public int countByMeetingId(Long meetingId) {
        return jpaMeetingMemberRepository.countByMeetingId(meetingId);
    }
}
