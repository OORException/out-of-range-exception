package br.edu.iff.ccc.webdev.controller.restapi.admin;

import br.edu.iff.ccc.webdev.dto.response.websocket.WebSocketStatsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller REST para estatísticas e debugging de WebSocket (apenas ADMIN)
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/websocket")
@RequiredArgsConstructor
public class WebSocketStatsController {

    private final SimpUserRegistry simpUserRegistry;

    /**
     * Retorna estatísticas de conexões WebSocket ativas
     * Apenas acessível por administradores
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public WebSocketStatsResponse getWebSocketStats() {
        log.info("Admin requesting WebSocket stats");

        List<String> connectedUsers = simpUserRegistry.getUsers().stream()
                .map(simpUser -> simpUser.getName())
                .collect(Collectors.toList());

        int totalConnections = simpUserRegistry.getUsers().stream()
                .mapToInt(simpUser -> simpUser.getSessions().size())
                .sum();

        Map<String, Integer> subscriptionsByDestination = Map.of(
                "total_users", connectedUsers.size(),
                "total_sessions", totalConnections
        );

        return WebSocketStatsResponse.builder()
                .totalConnections(totalConnections)
                .totalUsers(connectedUsers.size())
                .connectedUsers(connectedUsers)
                .subscriptionsByDestination(subscriptionsByDestination)
                .build();
    }
}
