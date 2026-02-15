package br.edu.iff.ccc.webdev.model.support;

import br.edu.iff.ccc.webdev.model.entity.Chat;
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
    name = "chat_participations",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_chat_participation_chat_user",
            columnNames = {"chat_id", "user_id"}
        )
    },
    indexes = {
        @Index(name = "idx_chat_participation_chat", columnList = "chat_id"),
        @Index(name = "idx_chat_participation_user", columnList = "user_id"),
        @Index(name = "idx_chat_participation_active", columnList = "active")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class ChatParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    @ToString.Exclude
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt;

    @Column(name = "left_at")
    private Instant leftAt;

    @Column(name = "active", nullable = false)
    private boolean active;


    public void markActive() {
        this.active = true;
        this.leftAt = null;
    }

    public void markLeftNow() {
        this.active = false;
        this.leftAt = Instant.now();
    }

    @PrePersist
    private void prePersist() {
        if (this.joinedAt == null) this.joinedAt = Instant.now();
        this.active = true;
    }
}
