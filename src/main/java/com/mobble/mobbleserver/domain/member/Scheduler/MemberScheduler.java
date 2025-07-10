package com.mobble.mobbleserver.domain.member.Scheduler;

import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberScheduler {

    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteWithdrewMembers() {

        LocalDateTime withdrewDate = LocalDateTime.now().minusDays(7);
        List<Member> withdrewMembers = memberRepository.findWithdrewMembers(withdrewDate);

        memberRepository.deleteAll(withdrewMembers);
    }
}
