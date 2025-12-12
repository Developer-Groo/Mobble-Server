package com.mobble.mobbleserver.domain.meeting;

import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.exception.DomainException;
import com.mobble.mobbleserver.domain.image.Image;
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
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_image_id", nullable = false)
    private Image mainImage;

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
            Club club,
            Member owner,
            String title,
            Image mainImage,
            MeetingSchedule schedule,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        this.club = club;
        this.owner = owner;
        this.title = title;
        this.mainImage = mainImage;
        this.schedule = schedule;
        this.location = location;
        this.cost = cost;
        this.memberLimit = memberLimit;
        this.type = type;
    }

    public static Meeting create(
            Club club,
            Member owner,
            String title,
            Image mainImage,
            MeetingSchedule schedule,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        assertCreate(club, owner, title, mainImage, schedule, location, cost, memberLimit, type);

        return Meeting.builder()
                .club(club)
                .owner(owner)
                .title(title)
                .mainImage(mainImage)
                .schedule(schedule)
                .location(location)
                .cost(cost)
                .memberLimit(memberLimit)
                .type(type)
                .build();
    }

    public Meeting update(
            String title,
            Image mainImage,
            MeetingSchedule schedule,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        assertUpdate(title, mainImage, schedule, location, cost, memberLimit, type);

        this.title = title;
        this.mainImage = mainImage;
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
            Club club,
            Member owner,
            String title,
            Image mainImage,
            MeetingSchedule schedule,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        requireNonNull(club, "club must not be null");
        requireNonNull(owner, "meeting owner must not be null");
        assertCommon(title, mainImage, schedule, location, cost, memberLimit, type);
    }

    private static void assertUpdate(
            String title,
            Image mainImage,
            MeetingSchedule schedule,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        assertCommon(title, mainImage, schedule, location, cost, memberLimit, type);
    }

    private static void assertCommon(
            String title,
            Image mainImage,
            MeetingSchedule schedule,
            String location,
            String cost,
            int memberLimit,
            MeetingType type
    ) {
        requireNonNull(title, "title must not be null");
        requireNonNull(mainImage, "main image must not be null");
        requireNonNull(schedule, "schedule must not be null");
        requireNonNull(location, "location must not be null");
        requireNonNull(cost, "cost must not be null");
        requireNonNull(type, "type must not be null");
        if (memberLimit < 1) throw new DomainException(MeetingError.INVALID_MEMBER_LIMIT);
    }
}
