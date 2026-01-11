package org.example.repository;

import org.example.dto.FeedbackResponse;
import org.example.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsBySessionIdAndUserId(Long sessionId, Long userId);

    @Modifying
    @Transactional
    @Query("delete from Feedback f where f.session.id in :sessionIds")
    int deleteBySessionIds(Iterable<Long> sessionIds);

    long countBySessionId(Long sessionId);

    @Modifying
    @Transactional
    @Query("delete from Feedback f where f.user.id = :userId")
    int deleteByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("delete from Feedback f where f.session.id = :sessionId")
    int deleteBySessionId(Long sessionId);

    @Query("""
        select new org.example.dto.FeedbackResponse(
            f.id, f.session.id, f.user.id, f.rating, f.comment, f.createdAt
        )
        from Feedback f
        where f.session.id = :sessionId
        order by f.createdAt desc
    """)
    List<FeedbackResponse> findFeedbackResponsesBySessionId(Long sessionId);

    @Query("""
        select new org.example.dto.FeedbackResponse(
            f.id, f.session.id, f.user.id, f.rating, f.comment, f.createdAt
        )
        from Feedback f
        where f.user.id = :userId
        order by f.createdAt desc
    """)
    List<FeedbackResponse> findFeedbackResponsesByUserId(Long userId);

}
