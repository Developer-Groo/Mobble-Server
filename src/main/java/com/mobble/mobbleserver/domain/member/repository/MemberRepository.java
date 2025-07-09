package com.mobble.mobbleserver.domain.member.repository;

import com.mobble.mobbleserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    @Query(value =
            "SELECT * FROM member " +
            "WHERE is_deleted = true " +
            "AND deleted_at < :withdrewDate",
            nativeQuery = true)
    List<Member> findWithdrewMembers(@Param("withdrewDate") LocalDateTime withdrewDate);
}
