package org.example.repository;

import org.example.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsBySessionIdAndUserId(Long sessionId, Long userId);

    @Modifying
    @Transactional
    @Query("delete from Feedback f where f.session.id in :sessionIds")
    int deleteBySessionIds(Iterable<Long> sessionIds);

    long countBySessionId(Long sessionId);
}
