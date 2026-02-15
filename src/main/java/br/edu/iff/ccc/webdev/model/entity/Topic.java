package br.edu.iff.ccc.webdev.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(
    name = "topics",
    indexes = {
        @Index(name = "idx_topic_category", columnList = "category_id"),
        @Index(name = "idx_topic_creator", columnList = "created_by"),
        @Index(name = "idx_topic_last_activity", columnList = "last_activity_at")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 140)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "last_activity_at", nullable = false)
    private Instant lastActivityAt;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    @ToString.Exclude
    private User createdBy;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "topic_tags",
        joinColumns = @JoinColumn(name = "topic_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"),
        indexes = {
            @Index(name = "idx_topic_tag_topic", columnList = "topic_id"),
            @Index(name = "idx_topic_tag_tag", columnList = "tag_id")
        }
    )
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    @OneToOne(mappedBy = "topic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Chat chat;


    public void changeTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        this.title = title;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public List<Post> getPosts() {
        return Collections.unmodifiableList(posts);
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    @PrePersist
    private void prePersist() {
        if (this.createdAt == null) this.createdAt = Instant.now();
        if (this.lastActivityAt == null) this.lastActivityAt = this.createdAt;
    }
}
