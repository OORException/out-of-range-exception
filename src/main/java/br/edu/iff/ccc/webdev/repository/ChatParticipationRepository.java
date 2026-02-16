package br.edu.iff.ccc.webdev.repository;

import br.edu.iff.ccc.webdev.model.support.ChatParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatParticipationRepository extends JpaRepository<ChatParticipation, Long> {
    Optional<ChatParticipation> findByChatIdAndUserId(Long chatId, Long userId);
    List<ChatParticipation> findByChatIdAndActiveTrue(Long chatId);
    long countByChatIdAndActiveTrue(Long chatId);
}
