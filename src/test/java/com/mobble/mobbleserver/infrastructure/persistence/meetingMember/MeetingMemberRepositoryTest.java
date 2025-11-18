package com.mobble.mobbleserver.infrastructure.persistence.meetingMember;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.ClubCategory.ClubCategory;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meetingMember.MeetingMember;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.clubCategory.ClubCategoryTestFixture;
import com.mobble.mobbleserver.support.fixture.clubMember.ClubMemberTestFixture;
import com.mobble.mobbleserver.support.fixture.meeting.MeetingTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class MeetingMemberRepositoryTest {

    @Autowired
    private JpaMeetingMemberRepository meetingMemberRepository;

    @Autowired
    private EntityManager em;

    private ClubCategory clubCategory;
    private Club club;
    private Member hostMember;
    private ClubMember hostClubMember;
    private Meeting meeting;

    @BeforeEach
    void setUp() {
        clubCategory = ClubCategoryTestFixture.createDefaultCategory();
        em.persist(clubCategory);

        club = ClubTestFixture.createDefaultClub(clubCategory);
        em.persist(club);

        hostMember = MemberTestFixture.createDefaultMember();
        em.persist(hostMember);

        hostClubMember = ClubMemberTestFixture.createDefaultClubMember(
                hostMember, club, ClubMemberRole.LEADER, JoinStatus.APPROVED
        );
        em.persist(hostClubMember);

        meeting = MeetingTestFixture.createDefaultMeeting(hostClubMember);
        em.persist(meeting);
    }

    @Test
    @DisplayName("Meeting ID와 Member ID로 MeetingMember 조회 성공")
    void success_find_meeting_member_by_meeting_id_member_id() {
        // given
        MeetingMember meetingMember = MeetingMember.createMeetingMember(meeting, hostMember);
        em.persist(meetingMember);
        em.flush();
        em.clear();

        // when
        Optional<MeetingMember> result = meetingMemberRepository
                .findMeetingMemberByMeetingIdAndMemberId(meeting.getId(), hostMember.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getMeeting().getId()).isEqualTo(meeting.getId());
        assertThat(result.get().getMember().getId()).isEqualTo(hostMember.getId());
    }

    @Test
    @DisplayName("MeetingId로 MeetingMember 리스트 조회 성공")
    void success_find_meeting_member_by_meeting_id() {
        // given
        Member member1 = MemberTestFixture.createDefaultMember();
        Member member2 = MemberTestFixture.createDefaultMember();
        Member member3 = MemberTestFixture.createDefaultMember();
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        MeetingMember meetingMember1 = MeetingMember.createMeetingMember(meeting, member1);
        MeetingMember meetingMember2 = MeetingMember.createMeetingMember(meeting, member2);
        MeetingMember meetingMember3 = MeetingMember.createMeetingMember(meeting, member3);
        em.persist(meetingMember1);
        em.persist(meetingMember2);
        em.persist(meetingMember3);
        em.flush();
        em.clear();

        // when
        List<MeetingMember> result = meetingMemberRepository.findByMeetingId(meeting.getId());

        // then
        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("meetingId로 MeetingMember 수 조회 성공")
    void success_count_meeting_member_by_meeting_id() {
        // given
        Member member1 = MemberTestFixture.createDefaultMember();
        Member member2 = MemberTestFixture.createDefaultMember();
        Member member3 = MemberTestFixture.createDefaultMember();
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        MeetingMember meetingMember1 = MeetingMember.createMeetingMember(meeting, member1);
        MeetingMember meetingMember2 = MeetingMember.createMeetingMember(meeting, member2);
        MeetingMember meetingMember3 = MeetingMember.createMeetingMember(meeting, member3);
        em.persist(meetingMember1);
        em.persist(meetingMember2);
        em.persist(meetingMember3);
        em.flush();
        em.clear();

        // when
        int count = meetingMemberRepository.countByMeetingId(meeting.getId());

        // then
        assertThat(count).isEqualTo(3);
    }
}
