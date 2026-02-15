package br.edu.iff.ccc.webdev.model.entity;

import br.edu.iff.ccc.webdev.model.enums.UserLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true),
        @Index(name = "idx_users_level", columnList = "level")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    private Long id;

    @Column(nullable = false, length = 120)
    @Getter
    private String name;

    @Column(nullable = false, length = 180, unique = true)
    @Getter
    private String email;

    // No public getter
    // This accessor is for internal security/authentication use
    @Column(name = "password_hash", nullable = false)
    @ToString.Exclude
    private String passwordHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Getter
    private Instant createdAt;

    @Column(name = "last_access_at")
    @Getter
    private Instant lastAccessAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Getter
    private UserLevel level;


    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private List<Topic> topics = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private List<ChatMessage> chatMessages = new ArrayList<>();


    public void changeName(String name) {
        this.name = name;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changePasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be null or blank");
        }
        this.passwordHash = passwordHash;
    }

    /**
     * Internal method for authentication purposes only
     * Should NEVER be exposed through API responses or DTOs
     * @return the password hash for authentication
     */
    public String getPasswordHashForAuthentication() {
        return this.passwordHash;
    }

    public List<Topic> getTopics() {
        return Collections.unmodifiableList(topics);
    }

    public List<Post> getPosts() {
        return Collections.unmodifiableList(posts);
    }

    public List<ChatMessage> getChatMessages() {
        return Collections.unmodifiableList(chatMessages);
    }

    public void markLastAccessNow() {
        this.lastAccessAt = Instant.now();
    }

    @PrePersist
    private void prePersist() {
        if (this.createdAt == null) this.createdAt = Instant.now();
        if (this.level == null) this.level = UserLevel.USER;
    }
}
