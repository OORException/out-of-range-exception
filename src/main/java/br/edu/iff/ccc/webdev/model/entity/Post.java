package br.edu.iff.ccc.webdev.model.entity;

import br.edu.iff.ccc.webdev.model.support.Like;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(
    name = "posts",
    indexes = {
        @Index(name = "idx_post_topic", columnList = "topic_id"),
        @Index(name = "idx_post_author", columnList = "author_id"),
        @Index(name = "idx_post_created_at", columnList = "created_at")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    @ToString.Exclude
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    @ToString.Exclude
    private User author;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();


    public void editContent(String newContent) {
        if (newContent == null || newContent.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or blank");
        }
        this.content = newContent;
        this.updatedAt = Instant.now();
    }

    public List<Like> getLikes() {
        return Collections.unmodifiableList(likes);
    }

    @PrePersist
    private void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }
}
