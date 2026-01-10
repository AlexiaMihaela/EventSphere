package org.example.repository;

import org.example.model.SessionEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionEnrollmentRepository extends JpaRepository<SessionEnrollment, Long> {

    boolean existsBySessionIdAndUserId(Long sessionId, Long userId);

    long countBySessionId(Long sessionId);
}
