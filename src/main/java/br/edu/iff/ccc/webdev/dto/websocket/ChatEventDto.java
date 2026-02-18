package br.edu.iff.ccc.webdev.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para eventos de participantes em chat (join/leave)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatEventDto {
    
    public enum EventType {
        JOIN,
        LEAVE,
        TYPING,
        STOP_TYPING
    }
    
    private EventType type;
    private Long chatId;
    private Long userId;
    private String username;
    private LocalDateTime timestamp;
}
