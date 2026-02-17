package br.edu.iff.ccc.webdev.service.chat.impl;

import br.edu.iff.ccc.webdev.dto.websocket.ChatEventDto;
import br.edu.iff.ccc.webdev.exception.BadRequestException;
import br.edu.iff.ccc.webdev.exception.ConflictException;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Chat;
import br.edu.iff.ccc.webdev.model.entity.User;
import br.edu.iff.ccc.webdev.model.support.ChatParticipation;
import br.edu.iff.ccc.webdev.repository.ChatParticipationRepository;
import br.edu.iff.ccc.webdev.repository.ChatRepository;
import br.edu.iff.ccc.webdev.repository.UserRepository;
import br.edu.iff.ccc.webdev.security.SecurityUtils;
import br.edu.iff.ccc.webdev.service.chat.ChatParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatParticipationServiceImpl implements ChatParticipationService {

    private final ChatParticipationRepository chatParticipationRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public void join(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Chat not found with id: " + chatId));

        Long userId = securityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Optional<ChatParticipation> existing = chatParticipationRepository
                .findByChatIdAndUserId(chatId, userId);

        if (existing.isPresent()) {
            ChatParticipation participation = existing.get();
            if (participation.isActive()) {
                throw new ConflictException("You are already an active participant in this chat");
            }
            participation.markActive();
            chatParticipationRepository.save(participation);
        } else {
            ChatParticipation participation = ChatParticipation.builder()
                    .chat(chat)
                    .user(user)
                    .joinedAt(Instant.now())
                    .active(true)
                    .build();
            chatParticipationRepository.save(participation);
        }

        broadcastJoinEvent(chatId, user);
    }

    @Override
    @Transactional
    public void leave(Long chatId) {
        Long userId = securityUtils.getCurrentUserId();

        ChatParticipation participation = chatParticipationRepository
                .findByChatIdAndUserId(chatId, userId)
                .orElseThrow(() -> new NotFoundException("Participation not found"));

        if (!participation.isActive()) {
            throw new BadRequestException("User is not actively participating in this chat");
        }

        participation.markLeftNow();
        chatParticipationRepository.save(participation);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        broadcastLeaveEvent(chatId, user);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveParticipants(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new NotFoundException("Chat not found with id: " + chatId);
        }

        return chatParticipationRepository.countByChatIdAndActiveTrue(chatId);
    }

    /**
     * Faz broadcast de evento JOIN via WebSocket
     */
    private void broadcastJoinEvent(Long chatId, User user) {
        ChatEventDto event = ChatEventDto.builder()
                .type(ChatEventDto.EventType.JOIN)
                .chatId(chatId)
                .userId(user.getId())
                .username(user.getUsername())
                .timestamp(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend(
                "/topic/chat/" + chatId + "/events",
                event
        );
    }

    /**
     * Faz broadcast de evento LEAVE via WebSocket
     */
    private void broadcastLeaveEvent(Long chatId, User user) {
        ChatEventDto event = ChatEventDto.builder()
                .type(ChatEventDto.EventType.LEAVE)
                .chatId(chatId)
                .userId(user.getId())
                .username(user.getUsername())
                .timestamp(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend(
                "/topic/chat/" + chatId + "/events",
                event
        );
    }
}
