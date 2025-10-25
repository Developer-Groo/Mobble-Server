package com.mobble.mobbleserver.refactor.club.clubGround.repository;

import com.mobble.mobbleserver.domain.ground.Ground;
import com.mobble.mobbleserver.refactor.club.clubGround.entity.ClubGround;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubGroundRepository extends JpaRepository<ClubGround, Long> {

    List<ClubGround> findByClubId(Long id);

    void deleteAllByClubId(Long id);

    @Query("SELECT cg.ground FROM ClubGround cg WHERE cg.club.id = :clubId")
    List<Ground> findGroundsByClubId(@Param("clubId") Long clubId);

}
