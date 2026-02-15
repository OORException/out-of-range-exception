package br.edu.iff.ccc.webdev.model.support;

import br.edu.iff.ccc.webdev.model.entity.User;
import br.edu.iff.ccc.webdev.model.enums.AuditAction;
import br.edu.iff.ccc.webdev.model.enums.EntityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(
    name = "audit_logs",
    indexes = {
        @Index(name = "idx_audit_log_user", columnList = "user_id"),
        @Index(name = "idx_audit_log_entity", columnList = "entity_type"),
        @Index(name = "idx_audit_log_created_at", columnList = "created_at")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 60)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", length = 100)
    private EntityType entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "details", length = 2000)
    private String details;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    private void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }
}
