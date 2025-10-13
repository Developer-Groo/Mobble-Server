package com.mobble.mobbleserver.refactor.member.scheduler;

import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.persistence.member.JpaMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberDeleteScheduler {

    private final JpaMemberRepository memberRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteWithdrewMembers() {
        log.info("Delete the withdrew members.");

        LocalDateTime withdrewDate = LocalDateTime.now().minusDays(7);
        log.info("withdrew members delete time: {}", withdrewDate);

        List<Member> withdrewMembers = memberRepository.findAllByIsDeletedTrueAndDeletedAtBefore(withdrewDate);

        if (withdrewMembers.isEmpty()) {
            log.info("Not found withdrew members to delete");
            return;
        }
        log.info("Deleting {} withdrew members.", withdrewMembers.size());

        memberRepository.deleteAll(withdrewMembers);
        log.info("Finished deleting withdrew members.");
    }
}
