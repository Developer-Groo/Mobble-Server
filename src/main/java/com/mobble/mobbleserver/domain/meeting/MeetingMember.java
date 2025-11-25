package com.mobble.mobbleserver.domain.meeting;

import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class MeetingMember {

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

    static MeetingMember createMeetingMember(Meeting meeting, Member member) {
        return MeetingMember.builder()
                .meeting(meeting)
                .member(member)
                .build();
    }

    void detach() {
        this.meeting = null;
    }

    private void validateMeetingMember(Meeting meeting, Member member) {
        requireNonNull(meeting, "meeting must not be null");
        requireNonNull(member, "member must not be null");
    }
}
