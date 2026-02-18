package br.edu.iff.ccc.webdev.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para mensagens de erro enviadas via WebSocket
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageDto {
    private String errorType;
    private String message;
    private LocalDateTime timestamp;
}
