package com.mobble.mobbleserver.domain.meeting;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingErrorCode;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.meetingMember.entity.MeetingMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
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
        validateCommon(clubMember, title, datetime, location, cost, memberLimit, type);
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

    public Meeting updateMeeting(
            String title,
            LocalDateTime dateTime,
            String location,
            String cost,
            Integer memberLimit,
            MeetingType type
    ) {
        validateContents(title, dateTime, location, cost, memberLimit, type);
        this.title = title;
        this.datetime = dateTime;
        this.location = location;
        this.cost = cost;
        this.memberLimit = memberLimit;
        this.type = type;
        return this;
    }

    private void validateCommon(
            ClubMember clubMember,
            String title,
            LocalDateTime datetime,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        if (clubMember == null) throw new DomainException(MeetingErrorCode.CLUB_MEMBER_REQUIRED);
        validateContents(title, datetime, location, cost, memberLimit, type);
    }

    private void validateContents(
            String title,
            LocalDateTime datetime,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        if (title == null || title.isBlank()) throw new DomainException(MeetingErrorCode.TITLE_REQUIRED);
        if (datetime == null) throw new DomainException(MeetingErrorCode.DATETIME_REQUIRED);
        if (location == null || location.isBlank()) throw new DomainException(MeetingErrorCode.LOCATION_REQUIRED);
        if (cost == null || cost.isBlank()) throw new DomainException(MeetingErrorCode.COST_REQUIRED);
        if (memberLimit <= 0) throw new DomainException(MeetingErrorCode.INVALID_MEMBER_LIMIT);
        if (type == null) throw new DomainException(MeetingErrorCode.TYPE_REQUIRED);
    }

    public int getAttendeeCount() {
        return meetingMembers.size();
    }

    public int calculateDDay() {
        LocalDate today = LocalDate.now();
        LocalDate meetingDate = this.datetime.toLocalDate();

        return (int) Duration.between(today.atStartOfDay(), meetingDate.atStartOfDay()).toDays();
    }
}
