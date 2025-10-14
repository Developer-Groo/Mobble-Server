package com.mobble.mobbleserver.application.member.service;

import com.mobble.mobbleserver.application.member.port.provided.DeleteWithdrawnMembersPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberWritePort;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeleteWithdrawnMembersService implements DeleteWithdrawnMembersPort {

    private final MemberReadPort memberReadPort;
    private final MemberWritePort memberWritePort;

    @Override
    public void deleteWithdrawnMembers(LocalDateTime withdrewDate) {
        log.info("withdrew members delete time: {}", withdrewDate);

        List<Member> withdrawnMembers = memberReadPort.findAllByIsDeletedTrueAndDeletedAtBefore(withdrewDate);

        if (withdrawnMembers.isEmpty()) {
            log.info("Not found withdrew members to delete");
            return;
        }
        log.info("Deleting {} withdrew members.", withdrawnMembers.size());

        memberWritePort.deleteAll(withdrawnMembers);
        log.info("Finished deleting withdrew members.");
    }
}
