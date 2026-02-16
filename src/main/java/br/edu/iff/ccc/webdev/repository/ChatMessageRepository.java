package br.edu.iff.ccc.webdev.repository;

import br.edu.iff.ccc.webdev.model.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatIdOrderBySentAtAsc(Long chatId);
}
