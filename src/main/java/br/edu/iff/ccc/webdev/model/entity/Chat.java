package br.edu.iff.ccc.webdev.model.entity;

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
    name = "chats",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_chat_topic", columnNames = "topic_id")
    },
    indexes = {
        @Index(name = "idx_chat_topic", columnList = "topic_id"),
        @Index(name = "idx_chat_active", columnList = "active")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 120)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "active", nullable = false)
    private boolean active;


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    @ToString.Exclude
    private Topic topic;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private List<ChatMessage> messages = new ArrayList<>();


    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public List<ChatMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    @PrePersist
    private void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        if (!this.active) {
            this.active = true;
        }
    }
}
