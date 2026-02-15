package br.edu.iff.ccc.webdev.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(
    name = "categories",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_category_name", columnNames = "name")
    },
    indexes = {
        @Index(name = "idx_category_name", columnList = "name")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(length = 500)
    private String description;


    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private List<Topic> topics = new ArrayList<>();

    
    public void rename(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public List<Topic> getTopics() {
        return Collections.unmodifiableList(topics);
    }
}
