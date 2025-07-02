package com.mobble.mobbleserver.meeting.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @Column(name = "club_member_id")
    private Long clubMemberId;

    private String title;

    private LocalDateTime datetime;

    private String location;

    private String cost;

    private int limit;

    private MeetingType type;



}
