package org.example.repository;

import org.example.model.SessionEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SessionEnrollmentRepository extends JpaRepository<SessionEnrollment, Long> {

    boolean existsBySessionIdAndUserId(Long sessionId, Long userId);

    @Modifying
    @Transactional
    @Query("delete from SessionEnrollment e where e.session.id in :sessionIds")
    int deleteBySessionIds(Iterable<Long> sessionIds);

    long countBySessionId(Long sessionId);
}
