package br.edu.iff.ccc.webdev.service.forum.impl;

import br.edu.iff.ccc.webdev.dto.request.forum.CreateTopicRequest;
import br.edu.iff.ccc.webdev.dto.response.forum.TopicResponse;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Category;
import br.edu.iff.ccc.webdev.model.entity.Tag;
import br.edu.iff.ccc.webdev.model.entity.Topic;
import br.edu.iff.ccc.webdev.model.entity.User;
import br.edu.iff.ccc.webdev.repository.CategoryRepository;
import br.edu.iff.ccc.webdev.repository.TagRepository;
import br.edu.iff.ccc.webdev.repository.TopicRepository;
import br.edu.iff.ccc.webdev.repository.TopicViewRepository;
import br.edu.iff.ccc.webdev.repository.UserRepository;
import br.edu.iff.ccc.webdev.service.forum.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final TopicViewRepository topicViewRepository;

    @Override
    @Transactional
    public TopicResponse create(CreateTopicRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + request.categoryId()));

        // TODO: Get actual user from security context - for now use hardcoded ID
        User creator = userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Set<Tag> tags = new HashSet<>();
        if (request.tagIds() != null && !request.tagIds().isEmpty()) {
            tags = new HashSet<>(tagRepository.findByIdIn(request.tagIds()));
        }

        Instant now = Instant.now();
        Topic topic = Topic.builder()
                .title(request.title())
                .description(request.description())
                .category(category)
                .createdBy(creator)
                .tags(tags)
                .createdAt(now)
                .lastActivityAt(now)
                .build();

        topic = topicRepository.save(topic);
        return toResponse(topic);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicResponse> list() {
        return topicRepository.findAllByOrderByLastActivityAtDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TopicResponse getById(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Topic not found with id: " + topicId));

        return toResponse(topic);
    }

    private TopicResponse toResponse(Topic topic) {
        long viewCount = topicViewRepository.countByTopicId(topic.getId());

        return new TopicResponse(
                topic.getId(),
                topic.getTitle(),
                topic.getDescription(),
                topic.getCategory().getId(),
                topic.getCreatedBy().getId(),
                topic.getCreatedAt(),
                topic.getLastActivityAt(),
                viewCount
        );
    }
}
