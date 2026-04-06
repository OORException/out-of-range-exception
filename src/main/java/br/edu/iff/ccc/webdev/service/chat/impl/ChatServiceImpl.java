package br.edu.iff.ccc.webdev.service.chat.impl;

import br.edu.iff.ccc.webdev.dto.request.chat.SendChatMessageRequest;
import br.edu.iff.ccc.webdev.dto.response.chat.ChatMessageResponse;
import br.edu.iff.ccc.webdev.dto.websocket.ChatMessageWsDto;
import br.edu.iff.ccc.webdev.exception.BadRequestException;
import br.edu.iff.ccc.webdev.exception.ConflictException;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Chat;
import br.edu.iff.ccc.webdev.model.entity.ChatMessage;
import br.edu.iff.ccc.webdev.model.entity.Topic;
import br.edu.iff.ccc.webdev.model.entity.User;
import br.edu.iff.ccc.webdev.model.enums.MessageStatus;
import br.edu.iff.ccc.webdev.repository.ChatMessageRepository;
import br.edu.iff.ccc.webdev.repository.ChatRepository;
import br.edu.iff.ccc.webdev.repository.TopicRepository;
import br.edu.iff.ccc.webdev.repository.UserRepository;
import br.edu.iff.ccc.webdev.security.SecurityUtils;
import br.edu.iff.ccc.webdev.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public Long createChatForTopic(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Topic not found with id: " + topicId));

        if (chatRepository.findByTopicId(topicId).isPresent()) {
            throw new ConflictException("This topic already has an associated chat");
        }

        Chat chat = Chat.builder()
                .topic(topic)
                .name("Chat for: " + topic.getTitle())
                .active(true)
                .createdAt(Instant.now())
                .build();

        chat = chatRepository.save(chat);
        return chat.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> listMessages(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new NotFoundException("Chat not found with id: " + chatId);
        }

        return chatMessageRepository.findByChatIdOrderBySentAtAsc(chatId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChatMessageResponse sendMessage(SendChatMessageRequest request) {
        return sendMessageInternal(request, null);
    }

    @Override
    @Transactional
    public ChatMessageResponse sendMessageAsUser(SendChatMessageRequest request, String userEmail) {
        return sendMessageInternal(request, userEmail);
    }

    private ChatMessageResponse sendMessageInternal(SendChatMessageRequest request, String userEmail) {
        Chat chat = chatRepository.findById(request.chatId())
                .orElseThrow(() -> new NotFoundException("Chat not found with id: " + request.chatId()));

        if (!chat.isActive()) {
            throw new BadRequestException("Chat is not active");
        }

        User sender;
        if (userEmail != null && !userEmail.isBlank()) {
            sender = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new NotFoundException("User not found: " + userEmail));
        } else {
            Long userId = securityUtils.getCurrentUserId();
            sender = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found"));
        }

        ChatMessage message = ChatMessage.builder()
                .chat(chat)
                .sender(sender)
                .content(request.content())
                .status(MessageStatus.SENT)
                .sentAt(Instant.now())
                .build();

        message = chatMessageRepository.save(message);

        broadcastMessage(message);

        return toResponse(message);
    }

    private ChatMessageResponse toResponse(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getChat().getId(),
                message.getSender().getId(),
                message.getContent(),
                message.getStatus().name(),
                message.getSentAt(),
                message.getEditedAt()
        );
    }

    /**
     * Faz broadcast da mensagem para todos os participantes via WebSocket
     */
    private void broadcastMessage(ChatMessage message) {
        ChatMessageWsDto wsDto = ChatMessageWsDto.builder()
                .messageId(message.getId())
                .chatId(message.getChat().getId())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .content(message.getContent())
                .timestamp(LocalDateTime.ofInstant(message.getSentAt(), ZoneId.systemDefault()))
                .status(message.getStatus())
                .build();

        messagingTemplate.convertAndSend(
                "/topic/chat/" + message.getChat().getId(),
                wsDto
        );
    }
}
