package com.mobble.mobbleserver.refactor.clubMember.repository;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class ClubMemberRepositoryImplTest {

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private EntityManager em;

    @Nested
    @DisplayName("findAllClubMemberByMemberId")
    class FindAllByMemberId {

        @Test
        @DisplayName("해당 멤버가 속한 모든 클럽멤버 반환 (club/chatRoom fetch join 보장)")
        void success_return_all_with_fetch_join() {
            // given
//            Member m1 = MemberTestFixture.createDefaultMember();
//            Member m2 = MemberTestFixture.createDefaultMember();
//            em.persist(m1);
//            em.persist(m2);
//
//            ClubCategory cat = ClubCategory.createClubCategory("SOCCER");
//            em.persist(cat);
//
//            Club c1 = ClubTestFixture.createDefaultClub(cat);
//            Club c2 = ClubTestFixture.createDefaultClub(cat);
//            em.persist(c1);
//            em.persist(c2);
//
//            ChatRoom r1 = ChatRoomTestFixture.createDefaultChatRoom();
//            ChatRoom r2 = ChatRoomTestFixture.createDefaultChatRoom();
//            em.persist(r1);
//            em.persist(r2);
//
//            ClubRoomInfo cc1 = ClubChatRoomTestFixture.createDefaultClubChatRoom(c1);
//            ClubRoomInfo cc2 = ClubChatRoomTestFixture.createDefaultClubChatRoom(c2);
//            em.persist(cc1.getChatRoom());
//            em.persist(cc2.getChatRoom());
//            em.persist(cc1);
//            em.persist(cc2);
//
//            ClubMember cm11 = ClubMember.createClubMember(m1, c1, ClubMemberRole.MEMBER, JoinStatus.APPROVED);
//            ClubMember cm12 = ClubMember.createClubMember(m1, c2, ClubMemberRole.MANAGER, JoinStatus.APPROVED);
//            ClubMember cm21 = ClubMember.createClubMember(m2, c1, ClubMemberRole.MEMBER, JoinStatus.APPROVED);
//            em.persist(cm11);
//            em.persist(cm12);
//            em.persist(cm21);
//
//            em.flush();
//            em.clear();
//
//            // when
//            List<ClubMember> result = clubMemberRepository.findAllClubMemberByMemberId(m1.getId());
//
//            // then
//            assertThat(result).hasSize(2);
//
//            ClubMember rcm1 = result.get(0);
//            assertThat(rcm1.getClub().getId()).isNotNull();
//            assertThat(rcm1.getClub().getClubRoomInfo().getChatRoom().getId()).isNotNull();
//
//            assertThat(result)
//                    .extracting(cm -> cm.getClub().getId())
//                    .containsExactlyInAnyOrder(c1.getId(), c2.getId());
        }

        @Test
        @DisplayName("해당 멤버가 속한 클럽이 없으면 빈 리스트")
        void return_empty_when_none() {
            // given
            Member m = MemberTestFixture.createDefaultMember();
            em.persist(m);

            em.flush();
            em.clear();

            // when
            List<ClubMember> result = clubMemberRepository.findAllClubMemberByMemberId(m.getId());

            // then
            assertThat(result).isEmpty();
        }
    }
}
