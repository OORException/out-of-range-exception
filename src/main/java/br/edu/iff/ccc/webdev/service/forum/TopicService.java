package br.edu.iff.ccc.webdev.service.forum;

import br.edu.iff.ccc.webdev.dto.request.forum.CreateTopicRequest;
import br.edu.iff.ccc.webdev.dto.response.forum.TopicResponse;

import java.util.List;

public interface TopicService {
    TopicResponse create(CreateTopicRequest request);
    List<TopicResponse> list();
    TopicResponse getById(Long topicId);
}
