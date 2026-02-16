package br.edu.iff.ccc.webdev.repository;

import br.edu.iff.ccc.webdev.model.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByCategoryId(Long categoryId);
    List<Topic> findAllByOrderByLastActivityAtDesc();
    long countByCategoryId(Long categoryId);
}
