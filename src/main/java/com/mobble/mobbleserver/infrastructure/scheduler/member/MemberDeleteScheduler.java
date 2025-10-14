package com.mobble.mobbleserver.infrastructure.scheduler.member;

import com.mobble.mobbleserver.application.member.port.provided.DeleteWithdrawnMembersPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberDeleteScheduler {

    private final DeleteWithdrawnMembersPort deleteWithdrawMembersPort;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteWithdrawnMembers() {
        LocalDateTime withdrewDate = LocalDateTime.now().minusDays(7);
        log.info("Start deleting withdrew members.");

        deleteWithdrawMembersPort.deleteWithdrawnMembers(withdrewDate);
        log.info("Finished deleting withdrew members.");
    }
}
