package br.edu.iff.ccc.webdev.controller.restapi.chat;

import br.edu.iff.ccc.webdev.service.chat.ChatParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chats/{chatId}/participants")
@RequiredArgsConstructor
public class ChatParticipationController {

    private final ChatParticipationService chatParticipationService;

    @PostMapping("/join")
    public void join(@PathVariable Long chatId) {
        chatParticipationService.join(chatId);
    }

    @PostMapping("/leave")
    public void leave(@PathVariable Long chatId) {
        chatParticipationService.leave(chatId);
    }

    @GetMapping("/count")
    public long countActive(@PathVariable Long chatId) {
        return chatParticipationService.countActiveParticipants(chatId);
    }
}
