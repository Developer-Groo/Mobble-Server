package com.mobble.mobbleserver.application.member.service;

import com.mobble.mobbleserver.application.ground.required.GroundReadPort;
import com.mobble.mobbleserver.application.member.port.provided.MemberSoftDeletePort;
import com.mobble.mobbleserver.application.member.port.provided.MemberUpdatePort;
import com.mobble.mobbleserver.application.member.port.provided.MembersDeletePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberWritePort;
import com.mobble.mobbleserver.domain.ground.Ground;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.infrastructure.web.member.dto.request.MemberUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberModifyService implements MemberUpdatePort, MemberSoftDeletePort, MembersDeletePort {

    private final MemberReadPort memberReadPort;
    private final MemberWritePort memberWritePort;
    private final GroundReadPort groundReadPort;

    @Override
    public Member updateMember(Long memberId, MemberUpdateRequestDto dto) {
        Member member = findMemberByMemberIdOrThrow(memberId);
        Ground ground = groundReadPort.findById(dto.groundCode())
                .orElseThrow();

        return member.updateMember(ground, dto.profileImage());
    }

    @Override
    public void softDeleteMember(Long memberId) {
        Member member = findMemberByMemberIdOrThrow(memberId);
        member.softDelete();
    }

    /**
     * Scheduler 에서 호출하는 탈퇴 회원 삭제 메서드
     */
    @Override
    public void deleteMembers(LocalDateTime softDeletedDate) {
        log.info("soft deleted members delete time: {}", softDeletedDate);

        List<Member> softDeletedMembers = memberReadPort.findAllByIsDeletedTrueAndDeletedAtBefore(softDeletedDate);

        if (softDeletedMembers.isEmpty()) {
            log.info("Not found soft deleted members to delete");
            return;
        }
        log.info("Deleting {} soft deleted members.", softDeletedMembers.size());

        memberWritePort.deleteAll(softDeletedMembers);
        log.info("Finished deleting soft deleted members.");
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
