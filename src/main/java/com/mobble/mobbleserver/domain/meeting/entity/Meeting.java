package com.mobble.mobbleserver.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "club_member_id")
//    private ClubMember clubMemberId;

    private String title;

    private LocalDateTime datetime;

    private String location;

    private String cost;

    private int limit;

    @Enumerated(EnumType.STRING)
    private MeetingType type;

    //TODO ClubMember 필요
    @Builder
    private Meeting(/*ClubMember clubMember, */ String title, LocalDateTime datetime, String location, String cost, int limit, MeetingType type) {
        //this.clubMember = clubMember
        this.title = title;
        this.datetime = datetime;
        this.location = location;
        this.cost = cost;
        this.limit = limit;
        this.type = type;
    }

    //TODO ClubMember 필요
    public static Meeting createMeeting(
//            ClubMember clubMember,
            String title,
            LocalDateTime datetime,
            String location,
            String cost,
            int limit,
            MeetingType type
    ) {
        return Meeting.builder()
                .title(title)
                .datetime(datetime)
                .location(location)
                .cost(cost)
                .limit(limit)
                .type(type)
                .build();
    }
}
