package org.example.controller;

import jakarta.validation.Valid;
import org.example.controller.dto.AddFeedbackRequest;
import org.example.model.Feedback;
import org.example.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

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
}
