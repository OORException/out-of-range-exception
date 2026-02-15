package br.edu.iff.ccc.webdev.model.support;

import br.edu.iff.ccc.webdev.model.entity.Topic;
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
    name = "topic_views",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_topic_view_topic_user",
            columnNames = {"topic_id", "user_id"}
        )
    },
    indexes = {
        @Index(name = "idx_topic_view_topic", columnList = "topic_id"),
        @Index(name = "idx_topic_view_user", columnList = "user_id"),
        @Index(name = "idx_topic_view_viewed_at", columnList = "viewed_at")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class TopicView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    @ToString.Exclude
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "viewed_at", nullable = false, updatable = false)
    private Instant viewedAt;

    @PrePersist
    private void prePersist() {
        if (this.viewedAt == null) {
            this.viewedAt = Instant.now();
        }
    }
}
