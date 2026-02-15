package br.edu.iff.ccc.webdev.model.support;

import br.edu.iff.ccc.webdev.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(
    name = "user_sessions",
    indexes = {
        @Index(name = "idx_user_session_user", columnList = "user_id"),
        @Index(name = "idx_user_session_active", columnList = "active"),
        @Index(name = "idx_user_session_expires_at", columnList = "expires_at")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "session_token", nullable = false, unique = true, updatable = false)
    private UUID sessionToken;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "invalidated_at")
    private Instant invalidatedAt;

    @Column(name = "active", nullable = false)
    private boolean active;


    public void invalidateNow() {
        this.active = false;
        this.invalidatedAt = Instant.now();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    @PrePersist
    private void prePersist() {
        if (this.sessionToken == null) {
            this.sessionToken = UUID.randomUUID();
        }
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        this.active = true;
    }
}
