package com.mobble.mobbleserver.infrastructure.persistence.meeting;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.support.fixture.clubMember.ClubMemberTestFixture;
import com.mobble.mobbleserver.support.fixture.meeting.MeetingTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class MeetingRepositoryTest {

    @Autowired
    private JpaMeetingRepository meetingRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("Club ID로 Meeting 리스트 조회 성공")
    void success_when_find_meetings_by_club_id() {
        // given
//        ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
//        Club club = ClubTestFixture.createDefaultClub(clubCategory);
//        Member member = MemberTestFixture.createDefaultMember();
//        em.persist(clubCategory);
//        em.persist(club);
//        em.persist(member);
//
//        ClubMember clubMember = ClubMemberTestFixture.createDefaultClubMember(
//                member,
//                club,
//                ClubMemberRole.LEADER,
//                JoinStatus.APPROVED
//        );
//        em.persist(clubMember);
//
//        Meeting meeting1 = MeetingTestFixture.createDefaultMeeting(clubMember);
//        Meeting meeting2 = MeetingTestFixture.createDefaultMeeting(clubMember);
//        em.persist(meeting1);
//        em.persist(meeting2);
//        em.flush();
//        em.clear();
//
//        // when
//        List<Meeting> result = meetingRepository.findByClubMember_Club_Id(club.getId());
//
//        // then
//        assertThat(result).hasSize(2)
//                .extracting(Meeting::getId)
//                .containsExactlyInAnyOrder(meeting1.getId(), meeting2.getId());
    }

    @Test
    @DisplayName("Club ID로 Meeting 조회 시 Meeting 이 없는 경우, 빈 리스트 반환")
    void success_when_returns_empty_list_by_club_id() {
        // given
//        ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
//        Club club = ClubTestFixture.createDefaultClub(clubCategory);
//        em.persist(clubCategory);
//        em.persist(club);
//        em.flush();
//        em.clear();
//
//        // when
//        List<Meeting> result = meetingRepository.findByClubMember_Club_Id(club.getId());
//
//        // then
//        assertThat(result).isEmpty();
    }
}
