package br.edu.iff.ccc.webdev.dto.response.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO para estatísticas de conexões WebSocket (apenas para admins)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketStatsResponse {
    private int totalConnections;
    private int totalUsers;
    private List<String> connectedUsers;
    private Map<String, Integer> subscriptionsByDestination;
}
