package com.mobble.mobbleserver.domain.meetingMember;

import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingMemberErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder(access = AccessLevel.PRIVATE)
    private MeetingMember(Meeting meeting, Member member) {
        validateMeetingMember(meeting, member);
        this.meeting = meeting;
        this.member = member;
    }

    public static MeetingMember createMeetingMember(Meeting meeting, Member member) {
        return MeetingMember.builder()
                .meeting(meeting)
                .member(member)
                .build();
    }

    private void validateMeetingMember(Meeting meeting, Member member) {
        if (meeting == null) throw new DomainException(MeetingMemberErrorCode.MEETING_REQUIRED);
        if (member == null) throw new DomainException(MeetingMemberErrorCode.MEMBER_REQUIRED);
    }
}
