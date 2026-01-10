package org.example.repository;

import org.example.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsBySessionIdAndUserId(Long sessionId, Long userId);

    long countBySessionId(Long sessionId);
}
