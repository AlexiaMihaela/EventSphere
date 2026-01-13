package org.example.service;

import org.example.dto.AddFeedbackRequest;
import org.example.dto.FeedbackResponse;
import org.example.model.Feedback;
import org.example.model.Session;
import org.example.model.User;
import org.example.repository.FeedbackRepository;
import org.example.repository.SessionEnrollmentRepository;
import org.example.repository.SessionRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SessionEnrollmentRepository enrollmentRepository;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           SessionRepository sessionRepository,
                           UserRepository userRepository,
                           SessionEnrollmentRepository enrollmentRepository) {
        this.feedbackRepository = feedbackRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public Feedback add(AddFeedbackRequest req) {
        Session session = sessionRepository.findById(req.sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + req.sessionId));

        User user = userRepository.findById(req.userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.userId));

        boolean enrolled = enrollmentRepository.existsBySessionIdAndUserId(session.getId(), user.getId());
        if (!enrolled) {
            throw new IllegalArgumentException("User must be enrolled in the session to leave feedback");
        }

        if (feedbackRepository.existsBySessionIdAndUserId(session.getId(), user.getId())) {
            throw new IllegalArgumentException("Feedback already submitted for this session by this user");
        }

        Feedback fb = new Feedback();
        fb.setSession(session);
        fb.setUser(user);
        fb.setRating(req.rating);
        fb.setComment(req.comment);

        return feedbackRepository.save(fb);
    }

    public List<FeedbackResponse> getBySession(Long sessionId) {
        return feedbackRepository.findFeedbackResponsesBySessionId(sessionId);
    }

    public List<FeedbackResponse> getByUser(Long userId) {
        return feedbackRepository.findFeedbackResponsesByUserId(userId);

    }
}
