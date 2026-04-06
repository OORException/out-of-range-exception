package br.edu.iff.ccc.webdev.controller.view.chat;

import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Chat;
import br.edu.iff.ccc.webdev.model.entity.ChatMessage;
import br.edu.iff.ccc.webdev.repository.ChatMessageRepository;
import br.edu.iff.ccc.webdev.repository.ChatRepository;
import br.edu.iff.ccc.webdev.service.chat.ChatParticipationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatViewController {

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());

    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipationService chatParticipationService;

    @GetMapping("/{chatId}")
    @Transactional(readOnly = true)
    public String chatRoom(@PathVariable Long chatId, Model model, HttpServletRequest request) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Chat não encontrado"));

        List<ChatMessage> messages = chatMessageRepository.findByChatIdOrderBySentAtAsc(chatId);
        long participantCount = chatParticipationService.countActiveParticipants(chatId);
        String token = extractTokenFromCookie(request);

        model.addAttribute("chat", chat);
        model.addAttribute("topicId", chat.getTopic().getId());
        model.addAttribute("topicTitle", chat.getTopic().getTitle());
        model.addAttribute("messages", messages);
        model.addAttribute("participantCount", participantCount);
        model.addAttribute("timeFmt", TIME_FMT);
        model.addAttribute("token", token);
        return "chat/room";
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return "";
        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
