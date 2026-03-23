package br.edu.iff.ccc.webdev.repository;

import br.edu.iff.ccc.webdev.model.support.ChatParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatParticipationRepository extends JpaRepository<ChatParticipation, Long> {
    Optional<ChatParticipation> findByChatIdAndUserId(Long chatId, Long userId);
    Optional<ChatParticipation> findByChatIdAndUserEmail(Long chatId, String userEmail);
    // Query nativa
    @Query(value = "SELECT * FROM chat_participation WHERE chat_id = :chatId AND active = true", nativeQuery = true)
    List<ChatParticipation> findByChatIdAndActiveTrue(@org.springframework.data.repository.query.Param("chatId") Long chatId);
    // Query JPQL
    @Query("SELECT COUNT(cp) FROM ChatParticipation cp WHERE cp.chat.id = :chatId AND cp.active = true")
    long countByChatIdAndActiveTrue(@org.springframework.data.repository.query.Param("chatId") Long chatId);
    
}
