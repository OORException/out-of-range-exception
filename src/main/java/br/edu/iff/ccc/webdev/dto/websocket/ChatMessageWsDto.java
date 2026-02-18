package br.edu.iff.ccc.webdev.dto.websocket;

import br.edu.iff.ccc.webdev.model.enums.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para mensagens de chat transmitidas via WebSocket
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageWsDto {
    
    private Long messageId;
    private Long chatId;
    private Long senderId;
    private String senderUsername;
    private String content;
    private LocalDateTime timestamp;
    private MessageStatus status;
}
