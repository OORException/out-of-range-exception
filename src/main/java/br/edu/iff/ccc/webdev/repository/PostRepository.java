package br.edu.iff.ccc.webdev.repository;

import br.edu.iff.ccc.webdev.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTopicIdOrderByCreatedAtAsc(Long topicId);
    long countByTopicId(Long topicId);
}
