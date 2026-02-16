package br.edu.iff.ccc.webdev.repository;

import br.edu.iff.ccc.webdev.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    boolean existsByName(String name);
    List<Tag> findByIdIn(Set<Long> ids);
}
