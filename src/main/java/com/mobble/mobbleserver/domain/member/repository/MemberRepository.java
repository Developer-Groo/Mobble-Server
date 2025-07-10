package com.mobble.mobbleserver.domain.member.repository;

import com.mobble.mobbleserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * isDeleted = true 포함 멤버 유무 조회
     * @param email
     * @return
     */
    @Query(value = "SELECT * FROM member m " +
            "WHERE m.email = :email",
            nativeQuery = true)
    Optional<Member> findMemberByEmail(@Param("email") String email);

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
    List<Member> findByIsDeletedTrueAndDeletedAtBefore(@Param("withdrewDate") LocalDateTime withdrewDate);
}
