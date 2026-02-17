package br.edu.iff.ccc.webdev.controller.restapi.chat;

import br.edu.iff.ccc.webdev.dto.request.chat.SendChatMessageRequest;
import br.edu.iff.ccc.webdev.dto.response.chat.ChatMessageResponse;
import br.edu.iff.ccc.webdev.dto.response.chat.ChatResponse;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Chat;
import br.edu.iff.ccc.webdev.repository.ChatParticipationRepository;
import br.edu.iff.ccc.webdev.repository.ChatRepository;
import br.edu.iff.ccc.webdev.service.chat.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final ChatParticipationRepository chatParticipationRepository;

    @PostMapping("/topic/{topicId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createForTopic(@PathVariable Long topicId) {
        return chatService.createChatForTopic(topicId);
    }

    @GetMapping("/{chatId}")
    public ChatResponse getById(@PathVariable Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Chat not found with id: " + chatId));

        long participantCount = chatParticipationRepository.countByChatIdAndActiveTrue(chatId);

        return new ChatResponse(
                chat.getId(),
                chat.getTopic().getId(),
                chat.getName(),
                chat.isActive(),
                chat.getCreatedAt(),
                participantCount
        );
    }

    @GetMapping("/{chatId}/messages")
    public List<ChatMessageResponse> listMessages(@PathVariable Long chatId) {
        return chatService.listMessages(chatId);
    }

    @PostMapping("/{chatId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatMessageResponse sendMessage(@PathVariable Long chatId, @Valid @RequestBody SendChatMessageRequest request) {
        return chatService.sendMessage(request);
    }
}
