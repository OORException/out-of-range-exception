package br.edu.iff.ccc.webdev.repository;

import br.edu.iff.ccc.webdev.model.support.TopicView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicViewRepository extends JpaRepository<TopicView, Long> {
    Optional<TopicView> findByTopicIdAndUserId(Long topicId, Long userId);
    long countByTopicId(Long topicId);
}
