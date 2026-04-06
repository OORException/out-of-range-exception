package br.edu.iff.ccc.webdev.service.chat;

import br.edu.iff.ccc.webdev.dto.request.chat.SendChatMessageRequest;
import br.edu.iff.ccc.webdev.dto.response.chat.ChatMessageResponse;

import java.util.List;

public interface ChatService {
    Long createChatForTopic(Long topicId);
    List<ChatMessageResponse> listMessages(Long chatId);
    ChatMessageResponse sendMessage(SendChatMessageRequest request);
    ChatMessageResponse sendMessageAsUser(SendChatMessageRequest request, String userEmail);
}
