package com.mobble.mobbleserver.domain.member.repository;

import com.mobble.mobbleserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    /**
     * softDelete 멤버 조회
     * @param withdrewDate
     * @return
     */
    @Query(value =
            "SELECT * FROM member " +
            "WHERE is_deleted = true " +
            "AND deleted_at < :withdrewDate",
            nativeQuery = true)
    List<Member> findWithdrewMembers(@Param("withdrewDate") LocalDateTime withdrewDate);
    
    /**
     * isDeleted 포함된 멤버 유무 조회
     * @param email
     * @return
     */
    @Query(value = "SELECT * FROM member m " +
            "WHERE m.email = :email",
            nativeQuery = true)
    Optional<Member> findMemberByEmail(@Param("email") String email);
}
