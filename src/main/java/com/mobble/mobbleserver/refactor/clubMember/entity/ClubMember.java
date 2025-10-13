package com.mobble.mobbleserver.refactor.clubMember.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import com.mobble.mobbleserver.refactor.article.entity.ArticleType;
import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder(access = AccessLevel.PRIVATE)
    private ClubMember(
            Member member,
            Club club,
            ClubMemberRole clubMemberRole,
            JoinStatus joinStatus
    ) {
        this.member = member;
        this.club = club;
        this.clubMemberRole = clubMemberRole;
        this.joinStatus = joinStatus;
    }

    public static ClubMember createClubMember(
            Member member,
            Club club,
            ClubMemberRole clubMemberRole,
            JoinStatus joinStatus
    ) {
        return ClubMember.builder()
                .member(member)
                .club(club)
                .clubMemberRole(clubMemberRole)
                .joinStatus(joinStatus)
                .build();
    }

    public void updateStatus(JoinStatus joinStatus) {
        this.joinStatus = joinStatus;
    }

    public void updateRole(ClubMemberRole newRole) {
        this.clubMemberRole = newRole;
    }

    public boolean isLeader() {
        return this.clubMemberRole == ClubMemberRole.LEADER;
    }

    public boolean canPost(ArticleType articleType) {
        return articleType != ArticleType.NOTICE || this.clubMemberRole != ClubMemberRole.MEMBER;
    }
}
