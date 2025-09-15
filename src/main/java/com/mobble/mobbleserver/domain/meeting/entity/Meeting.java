package com.mobble.mobbleserver.domain.meeting.entity;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.meetingMember.entity.MeetingMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_member_id")
    private ClubMember clubMember;

    private String title;

    private LocalDateTime datetime;

    private String location;

    private String cost;

    @Column(name = "member_limit")
    private int memberLimit;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY)
    private List<MeetingMember> meetingMembers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MeetingType type;

    @Builder(access = AccessLevel.PRIVATE)
    private Meeting(
            ClubMember clubMember,
            String title,
            LocalDateTime datetime,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        this.clubMember = clubMember;
        this.title = title;
        this.datetime = datetime;
        this.location = location;
        this.cost = cost;
        this.memberLimit = memberLimit;
        this.type = type;
    }

    public static Meeting createMeeting(
            ClubMember clubMember,
            String title,
            LocalDateTime datetime,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        return Meeting.builder()
                .clubMember(clubMember)
                .title(title)
                .datetime(datetime)
                .location(location)
                .cost(cost)
                .memberLimit(memberLimit)
                .type(type)
                .build();
    }

    public void updateMeeting(
            String title,
            LocalDateTime dateTime,
            String location,
            String cost,
            Integer memberLimit,
            MeetingType type
    ) {
        //Todo null 검증 로직 추가
        this.title = title;
        this.datetime = dateTime;
        this.location = location;
        this.cost = cost;
        this.memberLimit = memberLimit;
        this.type = type;
    }
}
