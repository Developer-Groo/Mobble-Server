package com.mobble.mobbleserver.domain.clubMember.entity;

import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.clubCategory.ClubCategoryTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClubMemberTest {

    private final Member member = MemberTestFixture.createDefaultMember();
    private final ClubCategory category = ClubCategoryTestFixture.createDefaultCategory();
    private final Club club = ClubTestFixture.createDefaultClub(category);

    @Nested
    @DisplayName("클럽 멤버 생성")
    class CreateClubMember {

        @Test
        @DisplayName("정상 생성")
        void create_success() {
            // given
            ClubMemberRole role = ClubMemberRole.MEMBER;
            JoinStatus status = JoinStatus.WAITING;

            // when
            ClubMember clubMember = ClubMember.createClubMember(member, club, role, status);

            // then
            assertThat(clubMember.getId()).isNull();
            assertThat(clubMember.getMember()).isSameAs(member);
            assertThat(clubMember.getClub()).isSameAs(club);
            assertThat(clubMember.getClubMemberRole()).isEqualTo(role);
            assertThat(clubMember.getJoinStatus()).isEqualTo(status);
        }
    }

    @Nested
    @DisplayName("상태/권한 업데이트")
    class Update {

        @Test
        @DisplayName("updateStatus: WAITING -> APPROVED")
        void update_status_success() {
            // given
            ClubMember clubMember = ClubMember.createClubMember(member, club, ClubMemberRole.MEMBER, JoinStatus.WAITING);

            // when
            clubMember.updateStatus(JoinStatus.APPROVED);

            // then
            assertThat(clubMember.getJoinStatus()).isEqualTo(JoinStatus.APPROVED);
        }

        @Test
        @DisplayName("updateRole: MEMBER -> LEADER")
        void update_role_success() {
            // given
            ClubMember clubMember = ClubMember.createClubMember(member, club, ClubMemberRole.MEMBER, JoinStatus.APPROVED);

            // when
            clubMember.updateRole(ClubMemberRole.LEADER);

            // then
            assertThat(clubMember.getClubMemberRole()).isEqualTo(ClubMemberRole.LEADER);
        }
    }
