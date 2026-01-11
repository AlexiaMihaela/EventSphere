package org.example.repository;

import org.example.dto.analytics.SessionAttendanceRow;
import org.example.dto.analytics.SessionPerformanceRow;
import org.example.model.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.example.dto.SessionResponse;



import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByEventId(Long eventId);

    @Query("""
        select new org.example.controller.dto.analytics.SessionAttendanceRow(
            s.id, s.title, s.startTime, count(e.id)
        )
        from Session s
        left join SessionEnrollment e on e.session.id = s.id
        where s.event.id = :eventId
        group by s.id, s.title, s.startTime
        having count(e.id) < :threshold
        order by count(e.id) asc
    """)
    List<SessionAttendanceRow> findLowAttendanceSessions(Long eventId, long threshold);

    // Analytics 2: top sessions by enrollments + avg rating + feedback count
    @Query("""
        select new org.example.controller.dto.analytics.SessionPerformanceRow(
            s.id,
            s.title,
            s.startTime,
            count(distinct e.id),
            coalesce(avg(f.rating), 0),
            count(distinct f.id)
        )
        from Session s
        left join SessionEnrollment e on e.session.id = s.id
        left join Feedback f on f.session.id = s.id
        where s.event.id = :eventId
        group by s.id, s.title, s.startTime
        order by count(distinct e.id) desc, coalesce(avg(f.rating), 0) desc
    """)
    List<SessionPerformanceRow> findTopSessionPerformance(Long eventId);

    @Query("select s.id from Session s where s.event.id = :eventId")
    List<Long> findSessionIdsByEventId(Long eventId);

    @Modifying
    @Transactional
    @Query("delete from Session s where s.event.id = :eventId")
    int deleteByEventId(Long eventId);

    @Query("""
   select new org.example.controller.dto.SessionResponse(
      s.id, s.title, s.startTime, s.capacity, s.event.id
   )
   from Session s
   where s.event.id = :eventId
   order by s.startTime asc
""")
    List<SessionResponse> findSessionResponsesByEventId(Long eventId);


}
