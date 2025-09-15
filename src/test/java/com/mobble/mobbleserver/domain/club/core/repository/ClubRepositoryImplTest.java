package com.mobble.mobbleserver.domain.club.core.repository;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.club.core.repository.dto.ClubLikeInfoDto;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.like.clubLike.entity.ClubLike;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
public class ClubRepositoryImplTest {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("클럽 좋아요 수 및 사용자의 좋아요 여부 조회 - 좋아요 O")
    void success_when_find_like_info_liked_true() {
        // given
        Member member = MemberTestFixture.createDefaultMember();
        Member other = MemberTestFixture.createDefaultMember();
        em.persist(member);
        em.persist(other);

        ClubCategory category = ClubCategory.createClubCategory("SOCCER");
        em.persist(category);

        Club club = ClubTestFixture.createDefaultClub(category);
        em.persist(club);

        ClubLike like1 = ClubLike.createClubLike(club, member);
        ClubLike like2 = ClubLike.createClubLike(club, other);
        em.persist(like1);
        em.persist(like2);

        em.flush();
        em.clear();

        // when
        ClubLikeInfoDto dto = clubRepository.findLikeInfoByClubIdAndMemberId(club.getId(), member.getId());

        // then
        assertThat(dto.likeCount()).isEqualTo(2);
        assertThat(dto.isLiked()).isTrue();
    }

    @Test
    @DisplayName("클럽 좋아요 수 및 사용자의 좋아요 여부 조회 - 좋아요 X")
    void success_when_find_like_info_liked_false() {
        // given
        Member member = MemberTestFixture.createDefaultMember();
        Member other = MemberTestFixture.createDefaultMember();
        em.persist(member);
        em.persist(other);

        ClubCategory category = ClubCategory.createClubCategory("SOCCER");
        em.persist(category);

        Club club = ClubTestFixture.createDefaultClub(category);
        em.persist(club);

        ClubLike like = ClubLike.createClubLike(club, other);
        em.persist(like);

        em.flush();
        em.clear();

        // when
        ClubLikeInfoDto dto = clubRepository.findLikeInfoByClubIdAndMemberId(club.getId(), member.getId());

        // then
        assertThat(dto.likeCount()).isEqualTo(1);
        assertThat(dto.isLiked()).isFalse();
    }

    @Test
    @DisplayName("클럽 좋아요가 하나도 없을 때 - count=0, isLiked=false")
    void success_when_find_like_info_empty() {
        // given
        Member member = MemberTestFixture.createDefaultMember();
        em.persist(member);

        ClubCategory category = ClubCategory.createClubCategory("SOCCER");
        em.persist(category);

        Club club = ClubTestFixture.createDefaultClub(category);
        em.persist(club);

        em.flush();
        em.clear();

        // when
        ClubLikeInfoDto dto = clubRepository.findLikeInfoByClubIdAndMemberId(club.getId(), member.getId());

        // then
        assertThat(dto.likeCount()).isEqualTo(0);
        assertThat(dto.isLiked()).isFalse();
    }
}
