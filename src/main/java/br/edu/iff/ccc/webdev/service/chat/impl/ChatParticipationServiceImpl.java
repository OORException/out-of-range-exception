package br.edu.iff.ccc.webdev.service.chat.impl;

import br.edu.iff.ccc.webdev.exception.BadRequestException;
import br.edu.iff.ccc.webdev.exception.ConflictException;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Chat;
import br.edu.iff.ccc.webdev.model.entity.User;
import br.edu.iff.ccc.webdev.model.support.ChatParticipation;
import br.edu.iff.ccc.webdev.repository.ChatParticipationRepository;
import br.edu.iff.ccc.webdev.repository.ChatRepository;
import br.edu.iff.ccc.webdev.repository.UserRepository;
import br.edu.iff.ccc.webdev.service.chat.ChatParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatParticipationServiceImpl implements ChatParticipationService {

    private final ChatParticipationRepository chatParticipationRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void join(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Chat not found with id: " + chatId));

        // TODO: Get actual user from security context - for now use hardcoded ID
        Long userId = 1L;
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
    }

    @Override
    @Transactional
    public void leave(Long chatId) {
        // TODO: Get actual user from security context - for now use hardcoded ID
        Long userId = 1L;

        ChatParticipation participation = chatParticipationRepository
                .findByChatIdAndUserId(chatId, userId)
                .orElseThrow(() -> new NotFoundException("Participation not found"));

        if (!participation.isActive()) {
            throw new BadRequestException("User is not actively participating in this chat");
        }

        participation.markLeftNow();
        chatParticipationRepository.save(participation);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveParticipants(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new NotFoundException("Chat not found with id: " + chatId);
        }

        return chatParticipationRepository.countByChatIdAndActiveTrue(chatId);
    }
}
