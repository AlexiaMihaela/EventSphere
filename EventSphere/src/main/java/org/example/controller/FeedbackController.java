package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.AddFeedbackRequest;
import org.example.dto.FeedbackResponse;
import org.example.model.Feedback;
import org.example.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public Feedback add(@Valid @RequestBody AddFeedbackRequest req) {
        return feedbackService.add(req);
    }
    @GetMapping("/session/{sessionId}")
    public List<FeedbackResponse> bySession(@PathVariable Long sessionId) {
        return feedbackService.getBySession(sessionId);
    }

    @GetMapping("/user/{userId}")
    public List<FeedbackResponse> byUser(@PathVariable Long userId) {
        return feedbackService.getByUser(userId);
    }
}
