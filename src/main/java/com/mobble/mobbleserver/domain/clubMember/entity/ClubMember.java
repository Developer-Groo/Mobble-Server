package com.mobble.mobbleserver.domain.clubMember.entity;

import com.mobble.mobbleserver.common.baseEntity.CreatedAtEntity;
import com.mobble.mobbleserver.domain.club.entity.Club;
import com.mobble.mobbleserver.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubMember extends CreatedAtEntity {

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

    @Column(name = "jon_status")
    private boolean joinStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private ClubMember(
            Member member,
            Club club,
            ClubMemberRole clubMemberRole,
            boolean joinStatus
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
            boolean joinStatus
    ) {
        return ClubMember.builder()
                .member(member)
                .club(club)
                .clubMemberRole(clubMemberRole)
                .joinStatus(joinStatus)
                .build();
    }
}
