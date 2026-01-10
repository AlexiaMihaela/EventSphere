package org.example.repository;

import org.example.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    @Modifying
    @Transactional
    @Query("delete from EventRegistration r where r.event.id = :eventId")
    int deleteByEventId(Long eventId);

    long countByEventId(Long eventId);
}
