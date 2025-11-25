package com.mobble.mobbleserver.domain.meeting;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.common.exception.DomainException;
import com.mobble.mobbleserver.domain.meeting.error.MeetingError;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_member_id")
    private ClubMember clubMember;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Embedded
    private MeetingSchedule schedule;

    @Column(name = "location", nullable = false, length = 50)
    private String location;

    @Column(name = "cost", nullable = false, length = 10)
    private String cost;

    @Column(name = "member_limit", nullable = false)
    private int memberLimit;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MeetingType type;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingMember> meetingMembers = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Meeting(
            ClubMember clubMember,
            String title,
            MeetingSchedule schedule,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        this.clubMember = clubMember;
        this.title = title;
        this.schedule = schedule;
        this.location = location;
        this.cost = cost;
        this.memberLimit = memberLimit;
        this.type = type;
    }

    public static Meeting create(
            ClubMember clubMember,
            String title,
            MeetingSchedule schedule,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        assertCreate(clubMember, title, schedule, location, cost, memberLimit, type);

        return Meeting.builder()
                .clubMember(clubMember)
                .title(title)
                .schedule(schedule)
                .location(location)
                .cost(cost)
                .memberLimit(memberLimit)
                .type(type)
                .build();
    }

    public Meeting update(
            String title,
            MeetingSchedule schedule,
            String location,
            String cost,
            Integer memberLimit,
            MeetingType type
    ) {
        assertUpdate(title, schedule, location, cost, memberLimit, type);

        this.title = title;
        this.schedule = schedule;
        this.location = location;
        this.cost = cost;
        this.memberLimit = memberLimit;
        this.type = type;

        return this;
    }

    public int getAttendeeCount() {
        return meetingMembers.size();
    }

    public int calculateDDay() {
        LocalDate today = LocalDate.now();
        LocalDate meetingDate = this.schedule.getDatetime().toLocalDate();

        return (int) Duration.between(today.atStartOfDay(), meetingDate.atStartOfDay()).toDays();
    }

    public List<Member> getAttendedMembers() {
        return meetingMembers.stream()
                .map(MeetingMember::getMember)
                .toList();
    }

    public boolean hasAttendee(Long memberId) {
        requireNonNull(memberId, "memberId must not be null");

        return meetingMembers.stream()
                .anyMatch(meetingMember -> meetingMember.getMember().getId().equals(memberId));
    }

    public void attend(Member member) {
        requireNonNull(member, "member must not be null");

        if (hasAttendee(member.getId())) return;
        if (memberLimit <= meetingMembers.size()) throw new DomainException(MeetingError.FULL_CAPACITY);

        MeetingMember newMeetingMember = MeetingMember.createMeetingMember(this, member);
        meetingMembers.add(newMeetingMember);
    }

    public void cancelAttend(Long memberId) {
        requireNonNull(memberId, "memberId must not be null");

        meetingMembers.removeIf(meetingMember -> {
            boolean equals = meetingMember.getMember().getId().equals(memberId);
            if (equals) meetingMember.detach();
            return equals;
        });
    }

    /* Assert 검증 */
    private static void assertCreate(
            ClubMember clubMember,
            String title,
            MeetingSchedule schedule,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        requireNonNull(clubMember, "clubMember must not be null");
        assertUpdate(title, schedule, location, cost, memberLimit, type);
    }

    private static void assertUpdate(
            String title,
            MeetingSchedule schedule,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        requireNonNull(title, "title must not be null");
        requireNonNull(schedule, "schedule must not be null");
        requireNonNull(location, "location must not be null");
        requireNonNull(cost, "cost must not be null");
        requireNonNull(type, "type must not be null");
        if (memberLimit < 1) throw new DomainException(MeetingError.INVALID_MEMBER_LIMIT);
    }
}
