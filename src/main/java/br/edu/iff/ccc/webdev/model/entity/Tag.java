package br.edu.iff.ccc.webdev.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(
    name = "tags",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_tag_name", columnNames = "name")
    },
    indexes = {
        @Index(name = "idx_tag_name", columnList = "name")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 60)
    private String name;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private Set<Topic> topics = new HashSet<>();


    public void rename(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
    }

    public Set<Topic> getTopics() {
        return Collections.unmodifiableSet(topics);
    }
}
