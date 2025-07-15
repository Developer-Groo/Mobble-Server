package com.mobble.mobbleserver.domain.meetingMember.entity;

import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.member.entity.Member;
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

    @Builder
    private MeetingMember(Meeting meeting, Member member) {
        this.meeting = meeting;
        this.member = member;
    }

    public static MeetingMember createMeetingMember(Meeting meeting, Member member) {
        return MeetingMember.builder()
                .meeting(meeting)
                .member(member)
                .build();
    }
}
