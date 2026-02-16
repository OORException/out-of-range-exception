package br.edu.iff.ccc.webdev.service.chat;

public interface ChatParticipationService {
    void join(Long chatId);
    void leave(Long chatId);
    long countActiveParticipants(Long chatId);
}
