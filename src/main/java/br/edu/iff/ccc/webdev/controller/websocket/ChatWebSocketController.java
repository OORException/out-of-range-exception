package br.edu.iff.ccc.webdev.controller.websocket;

import br.edu.iff.ccc.webdev.dto.websocket.ChatEventDto;
import br.edu.iff.ccc.webdev.dto.websocket.SendMessageRequest;
import br.edu.iff.ccc.webdev.dto.request.chat.SendChatMessageRequest;
import br.edu.iff.ccc.webdev.exception.ForbiddenException;
import br.edu.iff.ccc.webdev.model.support.ChatParticipation;
import br.edu.iff.ccc.webdev.repository.ChatParticipationRepository;
import br.edu.iff.ccc.webdev.repository.UserRepository;
import br.edu.iff.ccc.webdev.service.chat.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Controller WebSocket para gerenciar comunicação em tempo real do chat
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final ChatParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Recebe mensagem enviada por usuário via WebSocket
     * Endpoint: /app/chat/{chatId}/send
     * Broadcast para: /topic/chat/{chatId}
     */
    @MessageMapping("/chat/{chatId}/send")
    public void sendMessage(
            @DestinationVariable Long chatId,
            @Valid @Payload SendMessageRequest request,
            Authentication authentication) {

        String userEmail = Objects.requireNonNull(authentication, "Authentication is required").getName();
        log.info("User {} sending message to chat {}", userEmail, chatId);

        validateParticipation(chatId, userEmail);
        chatService.sendMessageAsUser(
                new SendChatMessageRequest(chatId, request.getContent()),
                userEmail
        );
    }

    /**
     * Recebe evento de digitação (typing indicator)
     * Endpoint: /app/chat/{chatId}/typing
     * Broadcast para: /topic/chat/{chatId}/events
     */
    @MessageMapping("/chat/{chatId}/typing")
    public void typingIndicator(
            @DestinationVariable Long chatId,
            Authentication authentication) {

        String userEmail = authentication.getName();

        validateParticipation(chatId, userEmail);

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatEventDto event = ChatEventDto.builder()
                .type(ChatEventDto.EventType.TYPING)
                .chatId(chatId)
                .userId(user.getId())
                .username(user.getUsername())
                .timestamp(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/chat/" + chatId + "/events", event);
        
        log.debug("User {} typing in chat {}", userEmail, chatId);
    }

    /**
     * Recebe evento de parar de digitar
     * Endpoint: /app/chat/{chatId}/stop-typing
     * Broadcast para: /topic/chat/{chatId}/events
     */
    @MessageMapping("/chat/{chatId}/stop-typing")
    public void stopTypingIndicator(
            @DestinationVariable Long chatId,
            Authentication authentication) {

        String userEmail = authentication.getName();

        validateParticipation(chatId, userEmail);

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatEventDto event = ChatEventDto.builder()
                .type(ChatEventDto.EventType.STOP_TYPING)
                .chatId(chatId)
                .userId(user.getId())
                .username(user.getUsername())
                .timestamp(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/chat/" + chatId + "/events", event);
        
        log.debug("User {} stopped typing in chat {}", userEmail, chatId);
    }

    /**
     * Valida se o usuário autenticado é participante ativo do chat
     */
    private void validateParticipation(Long chatId, String userEmail) {
        ChatParticipation participation = participationRepository
                .findByChatIdAndUserEmail(chatId, userEmail)
                .orElseThrow(() -> new ForbiddenException(
                        "User is not a participant of this chat"));

        if (!participation.isActive()) {
            throw new ForbiddenException("User is not an active participant of this chat");
        }
    }
}
