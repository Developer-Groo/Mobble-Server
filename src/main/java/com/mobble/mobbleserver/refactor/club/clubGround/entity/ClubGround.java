package com.mobble.mobbleserver.refactor.club.clubGround.entity;

import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.ground.entity.Ground;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubGround {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_ground_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ground_code")
    private Ground ground;

    private ClubGround(Club club, Ground ground) {
        this.club = club;
        this.ground = ground;
    }

    public static ClubGround createClubGround(Club club, Ground ground) {
        ClubGround clubGround = new ClubGround(club, ground);

        return clubGround;
    }
}
