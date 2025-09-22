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
}
