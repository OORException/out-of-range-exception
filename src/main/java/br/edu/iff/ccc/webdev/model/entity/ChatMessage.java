package br.edu.iff.ccc.webdev.model.entity;

import br.edu.iff.ccc.webdev.model.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(
    name = "chat_messages",
    indexes = {
        @Index(name = "idx_chat_message_chat", columnList = "chat_id"),
        @Index(name = "idx_chat_message_sender", columnList = "sender_id"),
        @Index(name = "idx_chat_message_sent_at", columnList = "sent_at"),
        @Index(name = "idx_chat_message_status", columnList = "status")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 4000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageStatus status;

    @Column(name = "sent_at", nullable = false, updatable = false)
    private Instant sentAt;

    @Column(name = "edited_at")
    private Instant editedAt;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    @ToString.Exclude
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    @ToString.Exclude
    private User sender;


    public void markAsRead() {
        this.status = MessageStatus.READ;
    }

    public void editContent(String newContent) {
        if (newContent == null || newContent.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or blank");
        }
        this.content = newContent;
        this.status = MessageStatus.EDITED;
        this.editedAt = Instant.now();
    }

    @PrePersist
    private void prePersist() {
        if (this.sentAt == null) {
            this.sentAt = Instant.now();
        }
        if (this.status == null) {
            this.status = MessageStatus.SENT;
        }
    }
}
