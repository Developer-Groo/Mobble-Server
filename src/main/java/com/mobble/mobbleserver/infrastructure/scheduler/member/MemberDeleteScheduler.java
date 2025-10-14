package com.mobble.mobbleserver.infrastructure.scheduler.member;

import com.mobble.mobbleserver.application.member.port.provided.MembersDeletePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberDeleteScheduler {

    private final MembersDeletePort membersDeletePort;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteMembers() {
        LocalDateTime softDeletedDate = LocalDateTime.now().minusDays(7);
        log.info("Start deleting soft deleted members.");

        membersDeletePort.deleteMembers(softDeletedDate);
        log.info("Finished deleting soft deleted members.");
    }
}
