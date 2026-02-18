# Implementação WebSocket - Resumo

## O que foi implementado

### 1. Dependências
- **Adicionado** `spring-boot-starter-websocket` ao [pom.xml](pom.xml)

### 2. Configuração

#### WebSocketConfig
**Arquivo:** [src/main/java/br/edu/iff/ccc/webdev/config/WebSocketConfig.java](src/main/java/br/edu/iff/ccc/webdev/config/WebSocketConfig.java)
- Configuração STOMP sobre SockJS
- Endpoint principal: `/ws`
- Prefixos de destino:
  - `/app` - destinos de aplicação (client → server)
  - `/topic` - broadcast para múltiplos usuários
  - `/queue` - mensagens para usuário específico
  - `/user` - prefixo para destinos de usuário
- CORS configurado para `localhost:3000` e `localhost:8080`
- Suporta SockJS fallback e WebSocket nativo

#### SecurityConfig
**Modificado:** [src/main/java/br/edu/iff/ccc/webdev/config/SecurityConfig.java](src/main/java/br/edu/iff/ccc/webdev/config/SecurityConfig.java)
- Adicionado `.requestMatchers("/ws/**").permitAll()` para permitir handshake WebSocket
- Autenticação JWT é feita pelos interceptors customizados

### 3. Segurança JWT para WebSocket

#### JwtHandshakeInterceptor
**Arquivo:** [src/main/java/br/edu/iff/ccc/webdev/security/JwtHandshakeInterceptor.java](src/main/java/br/edu/iff/ccc/webdev/security/JwtHandshakeInterceptor.java)
- Valida token JWT durante handshake HTTP inicial
- Token enviado via query parameter: `?token=...`
- Extrai e valida username e UserDetails
- Armazena informações do usuário nos atributos da sessão WebSocket

#### JwtChannelInterceptor
**Arquivo:** [src/main/java/br/edu/iff/ccc/webdev/security/JwtChannelInterceptor.java](src/main/java/br/edu/iff/ccc/webdev/security/JwtChannelInterceptor.java)
- Autentica mensagens STOMP após handshake
- Cria `UsernamePasswordAuthenticationToken` e armazena no contexto da mensagem

### 4. DTOs WebSocket

#### ChatMessageWsDto
**Arquivo:** [src/main/java/br/edu/iff/ccc/webdev/dto/websocket/ChatMessageWsDto.java](src/main/java/br/edu/iff/ccc/webdev/dto/websocket/ChatMessageWsDto.java)
- DTO para mensagens de chat transmitidas via WebSocket
- Campos: `messageId`, `chatId`, `senderId`, `senderUsername`, `content`, `timestamp`, `status`

#### ChatEventDto
**Arquivo:** [src/main/java/br/edu/iff/ccc/webdev/dto/websocket/ChatEventDto.java](src/main/java/br/edu/iff/ccc/webdev/dto/websocket/ChatEventDto.java)
- DTO para eventos de participantes (JOIN, LEAVE, TYPING, STOP_TYPING)
- Campos: `type`, `chatId`, `userId`, `username`, `timestamp`

#### SendMessageRequest
**Arquivo:** [src/main/java/br/edu/iff/ccc/webdev/dto/websocket/SendMessageRequest.java](src/main/java/br/edu/iff/ccc/webdev/dto/websocket/SendMessageRequest.java)
- DTO para requisição de envio de mensagem via WebSocket
- Campo: `content` (validado, max 5000 caracteres)

#### ErrorMessageDto
**Arquivo:** [src/main/java/br/edu/iff/ccc/webdev/dto/websocket/ErrorMessageDto.java](src/main/java/br/edu/iff/ccc/webdev/dto/websocket/ErrorMessageDto.java)
- DTO para mensagens de erro enviadas via WebSocket
- Campos: `errorType`, `message`, `timestamp`

### 5. Controllers WebSocket

#### ChatWebSocketController
**Arquivo:** [src/main/java/br/edu/iff/ccc/webdev/controller/websocket/ChatWebSocketController.java](src/main/java/br/edu/iff/ccc/webdev/controller/websocket/ChatWebSocketController.java)

**Endpoints:**
- `@MessageMapping("/chat/{chatId}/send")` - Enviar mensagem
  - Valida participação do usuário no chat
  - Persiste mensagem via service existente
  - Broadcast automático via service
  
- `@MessageMapping("/chat/{chatId}/typing")` - Indicador de digitação
  - Broadcast para `/topic/chat/{chatId}/events`
  
- `@MessageMapping("/chat/{chatId}/stop-typing")` - Parar de digitar
  - Broadcast para `/topic/chat/{chatId}/events`

### 6. Services Atualizados

#### ChatServiceImpl
**Modificado:** [src/main/java/br/edu/iff/ccc/webdev/service/chat/impl/ChatServiceImpl.java](src/main/java/br/edu/iff/ccc/webdev/service/chat/impl/ChatServiceImpl.java)
- Injetado `SimpMessagingTemplate`
- Método `broadcastMessage()` adicionado
- Após salvar mensagem no `sendMessage()`, faz broadcast para `/topic/chat/{chatId}`

#### ChatParticipationServiceImpl
**Modificado:** [src/main/java/br/edu/iff/ccc/webdev/service/chat/impl/ChatParticipationServiceImpl.java](src/main/java/br/edu/iff/ccc/webdev/service/chat/impl/ChatParticipationServiceImpl.java)
- Injetado `SimpMessagingTemplate`
- Métodos `broadcastJoinEvent()` e `broadcastLeaveEvent()` adicionados
- Broadcasts eventos para `/topic/chat/{chatId}/events` em `join()` e `leave()`

### 7. Tratamento de Erros

#### WebSocketExceptionHandler
**Arquivo:** [src/main/java/br/edu/iff/ccc/webdev/exception/WebSocketExceptionHandler.java](src/main/java/br/edu/iff/ccc/webdev/exception/WebSocketExceptionHandler.java)
- Handler global para exceções em controllers WebSocket
- Captura: `NotFoundException`, `ForbiddenException`, `BadRequestException`, `ConflictException`, `UnauthorizedException`
- Envia erros para `/queue/errors` do usuário específico

### 8. Event Listeners

#### WebSocketEventListener
**Arquivo:** [src/main/java/br/edu/iff/ccc/webdev/websocket/WebSocketEventListener.java](src/main/java/br/edu/iff/ccc/webdev/websocket/WebSocketEventListener.java)
- Escuta eventos de ciclo de vida das conexões WebSocket
- Eventos: `SessionConnectEvent`, `SessionConnectedEvent`, `SessionSubscribeEvent`, `SessionDisconnectEvent`
- Logging detalhado de conexões, subscrições e desconexões

### 9. Endpoints REST para Monitoramento

#### WebSocketStatsController
**Arquivo:** [src/main/java/br/edu/iff/ccc/webdev/controller/restapi/admin/WebSocketStatsController.java](src/main/java/br/edu/iff/ccc/webdev/controller/restapi/admin/WebSocketStatsController.java)
- `GET /api/v1/admin/websocket/stats` (apenas ADMIN)
- Retorna estatísticas de conexões WebSocket ativas
- Usa `SimpUserRegistry` para rastrear usuários conectados

#### WebSocketStatsResponse
**Arquivo:** [src/main/java/br/edu/iff/ccc/webdev/dto/response/websocket/WebSocketStatsResponse.java](src/main/java/br/edu/iff/ccc/webdev/dto/response/websocket/WebSocketStatsResponse.java)
- DTO para resposta de estatísticas

### 10. Repository Atualizado

#### ChatParticipationRepository
**Modificado:** [src/main/java/br/edu/iff/ccc/webdev/repository/ChatParticipationRepository.java](src/main/java/br/edu/iff/ccc/webdev/repository/ChatParticipationRepository.java)
- Adicionado método `findByChatIdAndUserEmail(Long chatId, String userEmail)`

---

## Como usar

### Conexão WebSocket

**1. Obter token JWT:**
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password"
}
```

**2. Conectar via WebSocket:**
```javascript
const token = 'seu-jwt-token';
const socket = new SockJS(`http://localhost:8080/ws?token=${token}`);
const stompClient = Stomp.over(socket);

stompClient.connect({}, (frame) => {
  console.log('Connected:', frame);
});
```

### Subscrições

**1. Receber mensagens do chat:**
```javascript
stompClient.subscribe('/topic/chat/{chatId}', (message) => {
  const chatMessage = JSON.parse(message.body);
  console.log('New message:', chatMessage);
});
```

**2. Receber eventos de participantes:**
```javascript
stompClient.subscribe('/topic/chat/{chatId}/events', (event) => {
  const chatEvent = JSON.parse(event.body);
  console.log('Chat event:', chatEvent);
});
```

**3. Receber erros pessoais:**
```javascript
stompClient.subscribe('/user/queue/errors', (error) => {
  const errorMsg = JSON.parse(error.body);
  console.error('WebSocket error:', errorMsg);
});
```

### Enviar mensagens

**1. Enviar mensagem:**
```javascript
stompClient.send('/app/chat/{chatId}/send', {}, JSON.stringify({
  content: 'Hello, world!'
}));
```

**2. Indicar digitação:**
```javascript
stompClient.send('/app/chat/{chatId}/typing', {}, '{}');
```

**3. Parar de digitar:**
```javascript
stompClient.send('/app/chat/{chatId}/stop-typing', {}, '{}');
```

### Endpoints REST (ainda funcionam)

Os endpoints REST existentes continuam funcionando normalmente:
- `POST /api/v1/chats/topic/{topicId}` - Criar chat
- `GET /api/v1/chats/{chatId}/messages` - Listar mensagens
- `POST /api/v1/chats/{chatId}/messages` - Enviar mensagem
- `POST /api/v1/chats/{chatId}/participants/join` - Entrar no chat
- `POST /api/v1/chats/{chatId}/participants/leave` - Sair do chat

---

## Fluxo de Mensagens

1. **Cliente envia mensagem** → `SEND /app/chat/{chatId}/send`
2. **Controller WebSocket recebe** → Valida participação
3. **Chama ChatService** → Persiste no banco de dados
4. **Service faz broadcast** → `SimpMessagingTemplate.convertAndSend()`
5. **Todos os participantes recebem** → Via subscription em `/topic/chat/{chatId}`

## Segurança

- JWT validado durante handshake
- Autenticação em cada mensagem STOMP
- Validação de participação antes de enviar mensagem
- Usuário só pode enviar mensagens para chats dos quais participa
- CORS configurado para origens permitidas

## Próximos Passos Sugeridos

1. **Implementar testes unitários e de integração** para controllers e services WebSocket
2. **Adicionar rate limiting** para evitar spam de mensagens
3. **Implementar sistema de presença** (online/offline) usando session tracking
4. **Adicionar suporte para mensagens de leitura** (read receipts)
5. **Implementar paginação** para histórico de mensagens via WebSocket
6. **Considerar Redis** como message broker para escalabilidade horizontal
7. **Adicionar métricas** (Micrometer) para monitorar performance do WebSocket

---

## Compilação

O projeto compila com sucesso:
```bash
mvn clean compile -DskipTests
```

**Resultado:** BUILD SUCCESS
