package br.edu.iff.ccc.webdev.controller.restapi.forum;

import br.edu.iff.ccc.webdev.dto.request.forum.CreateTopicRequest;
import br.edu.iff.ccc.webdev.dto.response.forum.TopicResponse;
import br.edu.iff.ccc.webdev.service.forum.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    public TopicResponse create(@Valid @RequestBody CreateTopicRequest request) {
        return topicService.create(request);
    }

    @GetMapping
    public List<TopicResponse> list() {
        return topicService.list();
    }

    @GetMapping("/{topicId}")
    public TopicResponse getById(@PathVariable Long topicId) {
        return topicService.getById(topicId);
    }
}
