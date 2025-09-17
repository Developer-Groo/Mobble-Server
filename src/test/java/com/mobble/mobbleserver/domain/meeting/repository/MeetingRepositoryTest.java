package com.mobble.mobbleserver.domain.meeting.repository;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.entity.JoinStatus;
import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.clubMember.ClubMemberTestFixture;
import com.mobble.mobbleserver.support.fixture.meeting.MeetingTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(QueryDslConfig.class)
class MeetingRepositoryTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("Club ID로 Meeting 리스트 조회 성공")
    void success_when_find_meetings_by_club_id() {
        // given
        ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
        Club club = ClubTestFixture.createDefaultClub(clubCategory);
        Member member = MemberTestFixture.createDefaultMember();
        em.persist(clubCategory);
        em.persist(club);
        em.persist(member);

        ClubMember clubMember = ClubMemberTestFixture.createDefaultClubMember(
                member,
                club,
                ClubMemberRole.LEADER,
                JoinStatus.APPROVED
        );
        em.persist(clubMember);

        Meeting meeting1 = MeetingTestFixture.createDefaultMeeting(clubMember);
        Meeting meeting2 = MeetingTestFixture.createDefaultMeeting(clubMember);
        em.persist(meeting1);
        em.persist(meeting2);
        em.flush();
        em.clear();

        // when
        List<Meeting> result = meetingRepository.findByClubMember_Club_Id(club.getId());

        // then
        Assertions.assertThat(result).hasSize(2)
                .extracting(Meeting::getId)
                .containsExactlyInAnyOrder(meeting1.getId(), meeting2.getId());
    }
}
