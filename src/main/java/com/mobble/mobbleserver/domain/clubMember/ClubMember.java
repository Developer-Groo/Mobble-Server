package com.mobble.mobbleserver.domain.clubMember;

import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.error.ClubMemberError;
import com.mobble.mobbleserver.domain.common.BaseEntity;
import com.mobble.mobbleserver.domain.exception.DomainException;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Enumerated(EnumType.STRING)
    @Column(name = "club_member_role")
    private ClubMemberRole clubMemberRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_status", nullable = false)
    private JoinStatus joinStatus;

    @Column(name = "status_updated_at", nullable = false)
    private LocalDateTime statusUpdatedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private ClubMember(
            Member member,
            Club club,
            ClubMemberRole clubMemberRole,
            JoinStatus joinStatus,
            LocalDateTime statusUpdatedAt
    ) {
        this.member = member;
        this.club = club;
        this.clubMemberRole = clubMemberRole;
        this.joinStatus = joinStatus;
        this.statusUpdatedAt = statusUpdatedAt;
    }

    public static ClubMember createLeader(
            Member member,
            Club club
    ) {
        assertCreateLeader(member, club);

        return ClubMember.builder()
                .member(member)
                .club(club)
                .clubMemberRole(ClubMemberRole.LEADER)
                .joinStatus(JoinStatus.APPROVED)
                .statusUpdatedAt(LocalDateTime.now())
                .build();
    }

    public static ClubMember createMember(
            Member member,
            Club club,
            JoinStatus status
    ) {
        assertCreateMember(member, club, status);

        return ClubMember.builder()
                .member(member)
                .club(club)
                .clubMemberRole(ClubMemberRole.MEMBER)
                .joinStatus(status)
                .build();
    }

    public ClubMember updateStatus(JoinStatus joinStatus) {
        requireNonNull(joinStatus, "join status must not be null");

        this.joinStatus = joinStatus;
        this.statusUpdatedAt = LocalDateTime.now();

        return this;
    }

    public void updateRole(ClubMemberRole newRole) {
        requireNonNull(newRole, "role must not be null");

        this.clubMemberRole = newRole;
    }

    public boolean isLeader() {
        return this.clubMemberRole == ClubMemberRole.LEADER;
    }

    public boolean canPost(ArticleType articleType) {
        return !(articleType == ArticleType.NOTICE && this.clubMemberRole == ClubMemberRole.MEMBER);
    }

    public boolean canManage() {
        return this.clubMemberRole == ClubMemberRole.LEADER || this.clubMemberRole == ClubMemberRole.MANAGER;
    }

    public boolean isActive() {
        return this.joinStatus == JoinStatus.APPROVED || this.joinStatus == JoinStatus.WAITING;
    }

    public boolean canRejoin() {
        if (!this.joinStatus.isRejoinable()) return false;

        return !this.statusUpdatedAt.plusDays(7).isAfter(LocalDateTime.now());
    }

    public void assertApproved() {
        if (this.joinStatus != JoinStatus.APPROVED) throw new DomainException(ClubMemberError.MEMBER_NOT_APPROVED);
    }

    /* Assert 검증 */
    private static void assertCreateLeader(Member member, Club club) {
        requireNonNull(member, "member must not be null");
        requireNonNull(club, "club must not be null");
    }

    private static void assertCreateMember(Member member, Club club, JoinStatus joinStatus) {
        requireNonNull(member, "member must not be null");
        requireNonNull(club, "club must not be null");
        requireNonNull(joinStatus, "join status must not be null");
    }
}
