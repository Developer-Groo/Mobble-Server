package com.mobble.mobbleserver.domain.meetingMember.repository;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.entity.JoinStatus;
import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meetingMember.entity.MeetingMember;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.clubCategory.ClubCategoryTestFixture;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class MeetingMemberRepositoryTest {

    @Autowired
    private MeetingMemberRepository meetingMemberRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("Meeting ID와 Member ID로 MeetingMember 조회 성공")
    void success_find_meeting_member_by_meeting_id_member_id() {
        // given
        Member member = MemberTestFixture.createDefaultMember();
        ClubCategory clubCategory = ClubCategoryTestFixture.createDefaultCategory();
        Club club = ClubTestFixture.createDefaultClub(clubCategory);
        ClubMember clubMember = ClubMemberTestFixture.createDefaultClubMember(
                member,
                club,
                ClubMemberRole.LEADER,
                JoinStatus.APPROVED
        );
        em.persist(member);
        em.persist(clubCategory);
        em.persist(club);
        em.persist(clubMember);

        Meeting meeting = MeetingTestFixture.createDefaultMeeting(clubMember);
        em.persist(meeting);

        MeetingMember meetingMember = MeetingMember.createMeetingMember(meeting, member);
        em.persist(meetingMember);
        em.flush();
        em.clear();

        // when
        Optional<MeetingMember> result = meetingMemberRepository
                .findMeetingMemberByMeetingIdAndMemberId(meeting.getId(), member.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getMeeting().getId()).isEqualTo(meeting.getId());
        assertThat(result.get().getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("MeetingId로 MeetingMember 리스트 조회 성공")
    void success_find_meeting_member_by_meeting_id() {
        ClubCategory clubCategory = ClubCategoryTestFixture.createDefaultCategory();
        Club club = ClubTestFixture.createDefaultClub(clubCategory);
        Member hostMember = MemberTestFixture.createDefaultMember();
        ClubMember hostClubMember = ClubMemberTestFixture.createDefaultClubMember(
                hostMember,
                club,
                ClubMemberRole.LEADER,
                JoinStatus.APPROVED
        );
        Meeting meeting = MeetingTestFixture.createDefaultMeeting(hostClubMember);

        em.persist(clubCategory);
        em.persist(club);
        em.persist(hostMember);
        em.persist(hostClubMember);
        em.persist(meeting);

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
        ClubCategory clubCategory = ClubCategoryTestFixture.createDefaultCategory();
        Club club = ClubTestFixture.createDefaultClub(clubCategory);
        Member hostMember = MemberTestFixture.createDefaultMember();
        ClubMember hostClubMember = ClubMemberTestFixture.createDefaultClubMember(
                hostMember,
                club,
                ClubMemberRole.LEADER,
                JoinStatus.APPROVED
        );
        Meeting meeting = MeetingTestFixture.createDefaultMeeting(hostClubMember);

        em.persist(clubCategory);
        em.persist(club);
        em.persist(hostMember);
        em.persist(hostClubMember);
        em.persist(meeting);

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
