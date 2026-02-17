package br.edu.iff.ccc.webdev.service.forum.impl;

import br.edu.iff.ccc.webdev.dto.request.forum.CreateTopicRequest;
import br.edu.iff.ccc.webdev.dto.request.forum.UpdateTopicRequest;
import br.edu.iff.ccc.webdev.dto.response.forum.TopicResponse;
import br.edu.iff.ccc.webdev.exception.ForbiddenException;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Category;
import br.edu.iff.ccc.webdev.model.entity.Tag;
import br.edu.iff.ccc.webdev.model.entity.Topic;
import br.edu.iff.ccc.webdev.model.entity.User;
import br.edu.iff.ccc.webdev.model.enums.UserLevel;
import br.edu.iff.ccc.webdev.model.support.TopicView;
import br.edu.iff.ccc.webdev.repository.CategoryRepository;
import br.edu.iff.ccc.webdev.repository.TagRepository;
import br.edu.iff.ccc.webdev.repository.TopicRepository;
import br.edu.iff.ccc.webdev.repository.TopicViewRepository;
import br.edu.iff.ccc.webdev.repository.UserRepository;
import br.edu.iff.ccc.webdev.security.SecurityUtils;
import br.edu.iff.ccc.webdev.service.forum.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
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
    private final SecurityUtils securityUtils;
    private final HttpServletRequest request;

    @Override
    @Transactional
    public TopicResponse create(CreateTopicRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + request.categoryId()));

        Long userId = securityUtils.getCurrentUserId();
        User creator = userRepository.findById(userId)
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

        recordTopicView(topic);

        return toResponse(topic);
    }

    @Override
    @Transactional
    public TopicResponse update(Long topicId, UpdateTopicRequest request) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Topic not found with id: " + topicId));

        Long currentUserId = securityUtils.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        validateUpdatePermission(topic, currentUser);

        topic.changeTitle(request.title());

        if (request.description() != null) {
            topic.changeDescription(request.description());
        }

        if (request.tagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findByIdIn(request.tagIds()));
            topic.updateTags(tags);
        }

        topic.updateLastActivity();
        topic = topicRepository.save(topic);

        return toResponse(topic);
    }

    @Override
    @Transactional
    public void delete(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Topic not found with id: " + topicId));

        Long currentUserId = securityUtils.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        validateUpdatePermission(topic, currentUser);

        topicRepository.delete(topic);
    }

    private void validateUpdatePermission(Topic topic, User currentUser) {
        boolean isAdmin = currentUser.getLevel() == UserLevel.ADMIN;
        boolean isAuthor = topic.getCreatedBy().getId().equals(currentUser.getId());

        if (isAdmin) {
            return;
        }

        if (!isAuthor) {
            throw new ForbiddenException("Only the topic author or an admin can modify this topic");
        }

        Duration timeSinceCreation = Duration.between(topic.getCreatedAt(), Instant.now());
        if (timeSinceCreation.toHours() > 24) {
            throw new ForbiddenException("Authors can only modify topics within 24 hours of creation");
        }
    }

    private void recordTopicView(Topic topic) {
        Long userId = null;
        try {
            userId = securityUtils.getCurrentUserId();
        } catch (Exception e) {
            // User is not authenticated (anonymous)
        }

        if (userId != null) {
            if (topicViewRepository.findByTopicIdAndUserId(topic.getId(), userId).isEmpty()) {
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    TopicView view = TopicView.builder()
                            .topic(topic)
                            .user(user)
                            .ipAddress(getClientIpAddress())
                            .build();
                    topicViewRepository.save(view);
                }
            }
        }
    }

    private String getClientIpAddress() {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
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
