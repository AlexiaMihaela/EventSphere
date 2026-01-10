package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "session_enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "user_id"})
)
public class SessionEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    @JsonIgnore
    private Session session;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private LocalDateTime enrolledAt = LocalDateTime.now();

    public SessionEnrollment() {}

    public Long getId() { return id; }

    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
}
