package br.edu.iff.ccc.webdev.exception;

import br.edu.iff.ccc.webdev.dto.websocket.ErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;

/**
 * Handler global para exceções em controllers WebSocket
 */
@Slf4j
@ControllerAdvice
public class WebSocketExceptionHandler {

    @MessageExceptionHandler(NotFoundException.class)
    @SendToUser("/queue/errors")
    public ErrorMessageDto handleNotFoundException(NotFoundException ex) {
        log.error("WebSocket NotFoundException: {}", ex.getMessage());
        return ErrorMessageDto.builder()
                .errorType("NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @MessageExceptionHandler(ForbiddenException.class)
    @SendToUser("/queue/errors")
    public ErrorMessageDto handleForbiddenException(ForbiddenException ex) {
        log.error("WebSocket ForbiddenException: {}", ex.getMessage());
        return ErrorMessageDto.builder()
                .errorType("FORBIDDEN")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @MessageExceptionHandler(BadRequestException.class)
    @SendToUser("/queue/errors")
    public ErrorMessageDto handleBadRequestException(BadRequestException ex) {
        log.error("WebSocket BadRequestException: {}", ex.getMessage());
        return ErrorMessageDto.builder()
                .errorType("BAD_REQUEST")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @MessageExceptionHandler(ConflictException.class)
    @SendToUser("/queue/errors")
    public ErrorMessageDto handleConflictException(ConflictException ex) {
        log.error("WebSocket ConflictException: {}", ex.getMessage());
        return ErrorMessageDto.builder()
                .errorType("CONFLICT")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @MessageExceptionHandler(UnauthorizedException.class)
    @SendToUser("/queue/errors")
    public ErrorMessageDto handleUnauthorizedException(UnauthorizedException ex) {
        log.error("WebSocket UnauthorizedException: {}", ex.getMessage());
        return ErrorMessageDto.builder()
                .errorType("UNAUTHORIZED")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public ErrorMessageDto handleGenericException(Exception ex) {
        log.error("WebSocket unexpected error", ex);
        return ErrorMessageDto.builder()
                .errorType("INTERNAL_ERROR")
                .message("An unexpected error occurred")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
