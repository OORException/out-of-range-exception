package br.edu.iff.ccc.webdev.model.support;

import br.edu.iff.ccc.webdev.model.entity.Post;
import br.edu.iff.ccc.webdev.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(
    name = "likes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_like_user_post",
            columnNames = {"user_id", "post_id"}
        )
    },
    indexes = {
        @Index(name = "idx_like_user", columnList = "user_id"),
        @Index(name = "idx_like_post", columnList = "post_id")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    @ToString.Exclude
    private Post post;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    private void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }
}
