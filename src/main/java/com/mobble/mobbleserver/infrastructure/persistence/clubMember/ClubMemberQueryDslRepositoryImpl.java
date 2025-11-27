package com.mobble.mobbleserver.infrastructure.persistence.clubMember;

import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

import static com.mobble.mobbleserver.domain.clubMember.QClubMember.clubMember;
import static com.mobble.mobbleserver.domain.member.QMember.member;

@RequiredArgsConstructor
public class ClubMemberQueryDslRepositoryImpl implements ClubMemberQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ClubMemberRole> findDistinctRolesByMemberIdAndRoleIn(Long memberId, Collection<ClubMemberRole> roles) {
        return queryFactory
                .select(clubMember.clubMemberRole)
                .distinct()
                .from(clubMember)
                .join(clubMember.member, member)
                .where(
                        member.id.eq(memberId),
                        clubMember.clubMemberRole.in(roles),
                        clubMember.joinStatus.eq(JoinStatus.APPROVED),
                        member.isDeleted.isFalse()
                )
                .fetch();
    }
}
