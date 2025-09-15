package com.mobble.mobbleserver.domain.like.clubLike.repository;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.like.clubLike.entity.ClubLike;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
class ClubLikeRepositoryTest {

    @Autowired
    private ClubLikeRepository clubLikeRepository;

    @Autowired
    private EntityManager em;

    @Nested
    @DisplayName("좋아요 조회 기능")
    class Find {

        @Test
        @DisplayName("클럽에 사용자가 좋아요를 누른 경우, ClubLike 조회 성공")
        void success_when_liked() {

            // given
            Member member = MemberTestFixture.createDefaultMember();
            ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
            Club club = ClubTestFixture.createDefaultClub(clubCategory);
            ClubLike like = ClubLike.createClubLike(club, member);

            em.persist(clubCategory);
            em.persist(member);
            em.persist(club);
            em.persist(like);
            em.flush();
            em.clear();

            // when
            Optional<ClubLike> result = clubLikeRepository.findLikedByClubIdAndMemberId(club.getId(), member.getId());

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getMember().getId()).isEqualTo(member.getId());
            assertThat(result.get().getClub().getId()).isEqualTo(club.getId());
        }

        @Test
        @DisplayName("클럽에 사용자가 좋아요를 누르지 않은 경우, ClubLike 조회 결과 없음")
        void success_when_not_liked() {
            // given
            Member member = MemberTestFixture.createDefaultMember();
            ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
            Club club = ClubTestFixture.createDefaultClub(clubCategory);

            em.persist(clubCategory);
            em.persist(member);
            em.persist(club);
            em.flush();
            em.clear();

            // when
            Optional<ClubLike> result = clubLikeRepository.findLikedByClubIdAndMemberId(club.getId(), member.getId());

            // then
            assertThat(result).isNotPresent();
        }
    }

    @Nested
    @DisplayName("좋아요 삭제 기능")
    class Delete {

        @Test
        @DisplayName("Club ID로 좋아요 전체 삭제 성공")
        void success_when_delete_all_by_club_id() {
            // given
            Member member = MemberTestFixture.createDefaultMember();
            ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
            Club club = ClubTestFixture.createDefaultClub(clubCategory);

            em.persist(clubCategory);
            em.persist(member);
            em.persist(club);

            ClubLike like = ClubLike.createClubLike(club, member);
            em.persist(like);
            em.flush();

            // when
            clubLikeRepository.deleteClubLikeAllByClub_Id(club.getId());
            em.flush();
            em.clear();

            // then
            Optional<ClubLike> result = clubLikeRepository.findLikedByClubIdAndMemberId(club.getId(), member.getId());
            assertThat(result).isNotPresent();
        }
    }
}
