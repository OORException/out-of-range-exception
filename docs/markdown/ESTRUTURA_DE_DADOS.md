# Estrutura de Dados - Modelos e Entidades

Este documento detalha a estrutura de dados do Sistema de Fórum Web.

---

## Diagrama de Relacionamentos

```
┌─────────────┐
│    User     │
└──────┬──────┘
       │ author (1:N)
       ├──────────────────┐
       │                  │
       ▼                  ▼
┌─────────────┐    ┌─────────────┐
│   Topic     │    │    Post     │
└──────┬──────┘    └──────┬──────┘
       │                  │
       │ (1:N)            │ (1:N)
       ▼                  ▼
┌─────────────┐    ┌─────────────┐
│    Post     │    │    Like     │
└─────────────┘    └─────────────┘

┌─────────────┐    ┌─────────────┐
│  Category   │◄───│   Topic     │
└─────────────┘    └──────┬──────┘
     (1:N)                │ (N:M)
                          ▼
                   ┌─────────────┐
                   │     Tag     │
                   └─────────────┘

┌─────────────┐    ┌─────────────┐
│   Topic     │───►│    Chat     │
└─────────────┘    └──────┬──────┘
     (1:1)                │
                          │ (1:N)
                    ┌─────┴──────┐
                    ▼            ▼
             ┌─────────────┐ ┌─────────────┐
             │ChatMessage  │ │ChatParticip.│
             └─────────────┘ └─────────────┘
```

---

## Entidades Detalhadas

### 1. User (Usuário)

**Descrição**: Representa um usuário do sistema.

**Campos**:
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `id` | Long |  | Identificador único (auto-incremento) |
| `username` | String |  | Nome de usuário (único, 3-50 caracteres) |
| `email` | String |  | Email do usuário (único, formato válido) |
| `password` | String |  | Senha criptografada (BCrypt) |
| `fullName` | String |  | Nome completo (3-100 caracteres) |
| `role` | Enum |  | Papel: USER, ADMIN, MODERATOR |
| `createdAt` | LocalDateTime |  | Data de criação da conta |
| `lastLoginAt` | LocalDateTime |  | Último login |
| `isActive` | Boolean |  | Conta ativa? (default: true) |

**Relacionamentos**:
- 1:N com Topic (author)
- 1:N com Post (author)
- 1:N com Like
- 1:N com ChatMessage
- 1:N com ChatParticipation

**Exemplo JSON**:
```json
{
  "id": 1,
  "username": "jefferson_dev",
  "email": "jefferson@example.com",
  "fullName": "Jefferson Silva",
  "role": "USER",
  "createdAt": "2026-02-10T10:00:00Z",
  "lastLoginAt": "2026-02-14T14:30:00Z",
  "isActive": true
}
```

**Validações**:
- Username: único, regex `^[a-zA-Z0-9_]{3,50}$`
- Email: único, formato válido
- Password: mínimo 6 caracteres
- Full Name: não vazio

---

### 2. Category (Categoria)

**Descrição**: Categoria que agrupa tópicos relacionados.

**Campos**:
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `id` | Long |  | Identificador único |
| `name` | String |  | Nome (único, 3-100 caracteres) |
| `description` | String |  | Descrição (10-500 caracteres) |
| `createdAt` | LocalDateTime |  | Data de criação |
| `updatedAt` | LocalDateTime |  | Data da última atualização |

**Relacionamentos**:
- 1:N com Topic

**Exemplo JSON**:
```json
{
  "id": 1,
  "name": "Programação",
  "description": "Discussões sobre desenvolvimento de software",
  "createdAt": "2026-02-01T10:00:00Z",
  "updatedAt": null
}
```

**Regras de Negócio**:
-  Não pode ser deletada se tiver tópicos
-  Nome deve ser único
-  Apenas ADMIN pode criar/editar/deletar

---

### 3. Tag (Etiqueta)

**Descrição**: Marcador para classificar tópicos.

**Campos**:
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `id` | Long |  | Identificador único |
| `name` | String |  | Nome (único, 2-50 caracteres) |
| `createdAt` | LocalDateTime |  | Data de criação |

**Relacionamentos**:
- N:M com Topic (através de topic_tags)

**Exemplo JSON**:
```json
{
  "id": 1,
  "name": "Java",
  "createdAt": "2026-02-01T10:00:00Z"
}
```

**Regras de Negócio**:
-  Não pode ser deletada se vinculada a tópicos
-  Nome deve ser único
-  Case-insensitive para evitar duplicatas (Java = java)
-  Apenas ADMIN pode criar/editar/deletar

---

### 4. Topic (Tópico)

**Descrição**: Discussão principal no fórum.

**Campos**:
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `id` | Long |  | Identificador único |
| `title` | String |  | Título (5-200 caracteres) |
| `content` | String (TEXT) |  | Conteúdo (mínimo 10 caracteres) |
| `categoryId` | Long |  | FK para Category |
| `authorId` | Long |  | FK para User |
| `viewCount` | Integer |  | Número de visualizações (default: 0) |
| `isPinned` | Boolean |  | Fixado no topo? (default: false) |
| `isClosed` | Boolean |  | Fechado para respostas? (default: false) |
| `createdAt` | LocalDateTime |  | Data de criação |
| `updatedAt` | LocalDateTime |  | Data de edição |
| `lastActivityAt` | LocalDateTime |  | Última atividade (post/resposta) |

**Relacionamentos**:
- N:1 com Category
- N:1 com User (author)
- 1:N com Post
- N:M com Tag
- 1:1 com Chat

**Exemplo JSON**:
```json
{
  "id": 15,
  "title": "Como usar Spring Security?",
  "content": "Gostaria de saber as melhores práticas...",
  "categoryId": 1,
  "authorId": 5,
  "authorUsername": "jefferson_dev",
  "tags": [
    {"id": 1, "name": "Java"},
    {"id": 2, "name": "Spring Boot"}
  ],
  "viewCount": 42,
  "postCount": 8,
  "isPinned": false,
  "isClosed": false,
  "createdAt": "2026-02-14T10:30:00Z",
  "updatedAt": null,
  "lastActivityAt": "2026-02-14T15:20:00Z"
}
```

**Regras de Negócio**:
-  Criar tópico cria automaticamente o primeiro Post
-  Author pode editar apenas nas primeiras 24h
-  Admin pode fixar/fechar tópico
-  ViewCount incrementa a cada acesso único
-  lastActivityAt atualiza a cada novo Post

---

### 5. Post (Resposta)

**Descrição**: Resposta em um tópico de discussão.

**Campos**:
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `id` | Long |  | Identificador único |
| `topicId` | Long |  | FK para Topic |
| `authorId` | Long |  | FK para User |
| `content` | String (TEXT) |  | Conteúdo (mínimo 10 caracteres) |
| `createdAt` | LocalDateTime |  | Data de criação |
| `updatedAt` | LocalDateTime |  | Data de edição |
| `isDeleted` | Boolean |  | Soft delete (default: false) |

**Relacionamentos**:
- N:1 com Topic
- N:1 com User (author)
- 1:N com Like

**Exemplo JSON**:
```json
{
  "id": 42,
  "topicId": 15,
  "authorId": 2,
  "content": "Para usar Spring Security, você precisa...",
  "createdAt": "2026-02-14T11:00:00Z",
  "updatedAt": "2026-02-14T11:30:00Z"
}
```

**Regras de Negócio**:
-  Apenas author pode editar/deletar
-  Admin pode deletar qualquer post
-  Primeiro post de um tópico não pode ser deletado
-  Markdown suportado no conteúdo
-  Soft delete preserva histórico

---

### 6. Like (Curtida)

**Descrição**: Curtida em um post.

**Campos**:
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `id` | Long |  | Identificador único |
| `postId` | Long |  | FK para Post |
| `userId` | Long |  | FK para User |
| `createdAt` | LocalDateTime |  | Data da curtida |

**Relacionamentos**:
- N:1 com Post
- N:1 com User

**Constraints**:
- UNIQUE(postId, userId) - Um usuário só pode curtir uma vez

**Exemplo JSON**:
```json
{
  "id": 123,
  "postId": 42,
  "userId": 5,
  "createdAt": "2026-02-14T11:05:00Z"
}
```

**Regras de Negócio**:
-  Usuário não pode curtir próprio post
-  Um usuário pode curtir cada post apenas uma vez
-  Não há "dislike" no MVP

---

### 7. Chat (Sala de Chat)

**Descrição**: Sala de chat associada a um tópico.

**Campos**:
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `id` | Long |  | Identificador único |
| `topicId` | Long |  | FK para Topic (único) |
| `name` | String |  | Nome (gerado: "Chat - {titulo do topico}") |
| `isActive` | Boolean |  | Chat ativo? (default: true) |
| `createdAt` | LocalDateTime |  | Data de criação |
| `closedAt` | LocalDateTime |  | Data de fechamento |

**Relacionamentos**:
- 1:1 com Topic
- 1:N com ChatMessage
- 1:N com ChatParticipation

**Exemplo JSON**:
```json
{
  "id": 1,
  "topicId": 15,
  "name": "Chat - Como usar Spring Security?",
  "isActive": true,
  "createdAt": "2026-02-14T10:30:00Z",
  "closedAt": null
}
```

**Regras de Negócio**:
-  Um tópico pode ter apenas um chat
-  Admin pode fechar chat
-  Chat fechado não aceita novas mensagens
-  Chat fechado não pode ser reaberto (MVP)

---

### 8. ChatMessage (Mensagem de Chat)

**Descrição**: Mensagem enviada em um chat.

**Campos**:
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `id` | Long |  | Identificador único |
| `chatId` | Long |  | FK para Chat |
| `userId` | Long |  | FK para User |
| `content` | String (TEXT) |  | Conteúdo da mensagem |
| `messageType` | Enum |  | TEXT, IMAGE, FILE, SYSTEM |
| `sentAt` | LocalDateTime |  | Data/hora de envio |
| `isEdited` | Boolean |  | Foi editada? (default: false) |
| `editedAt` | LocalDateTime |  | Data/hora de edição |

**Relacionamentos**:
- N:1 com Chat
- N:1 com User

**Exemplo JSON**:
```json
{
  "id": 1,
  "chatId": 1,
  "userId": 5,
  "username": "jefferson_dev",
  "content": "Alguém pode me ajudar com essa dúvida?",
  "messageType": "TEXT",
  "sentAt": "2026-02-14T11:00:00Z",
  "isEdited": false,
  "editedAt": null
}
```

**Regras de Negócio**:
-  Apenas mensagens TEXT podem ser editadas
-  Mensagens SYSTEM são automáticas (entrou/saiu)
-  Author pode editar nas primeiras 15 minutos
-  Admin pode deletar qualquer mensagem

**Tipos de Mensagem**:
- `TEXT`: Mensagem de texto comum
- `IMAGE`: URL de imagem
- `FILE`: URL de arquivo anexado
- `SYSTEM`: Mensagem automática do sistema

---

### 9. ChatParticipation (Participação em Chat)

**Descrição**: Relacionamento entre usuário e chat.

**Campos**:
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `id` | Long |  | Identificador único |
| `chatId` | Long |  | FK para Chat |
| `userId` | Long |  | FK para User |
| `joinedAt` | LocalDateTime |  | Data/hora de entrada |
| `leftAt` | LocalDateTime |  | Data/hora de saída |
| `isActive` | Boolean |  | Ainda no chat? (default: true) |

**Relacionamentos**:
- N:1 com Chat
- N:1 com User

**Constraints**:
- UNIQUE(chatId, userId) quando isActive = true

**Exemplo JSON**:
```json
{
  "id": 1,
  "chatId": 1,
  "userId": 5,
  "joinedAt": "2026-02-14T11:00:00Z",
  "leftAt": null,
  "isActive": true
}
```

**Regras de Negócio**:
-  Usuário pode entrar e sair múltiplas vezes
-  Apenas participantes ativos podem enviar mensagens
-  Sistema gera mensagem automática ao entrar/sair
-  Contador de participantes conta apenas isActive = true

---

### 10. TopicView (Visualização de Tópico)

**Descrição**: Registra visualizações de tópicos por usuário.

**Campos**:
| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `id` | Long |  | Identificador único |
| `topicId` | Long |  | FK para Topic |
| `userId` | Long |  | FK para User (nullable para anônimos) |
| `viewedAt` | LocalDateTime |  | Data/hora da visualização |
| `ipAddress` | String |  | IP do visitante |

**Relacionamentos**:
- N:1 com Topic
- N:1 com User

**Constraints**:
- UNIQUE(topicId, userId, DATE(viewedAt)) - Uma view por usuário por dia

**Regras de Negócio**:
-  Incrementa viewCount do Topic
-  Apenas uma view por usuário por dia
-  Visitantes anônimos contam (userId = null)

---

##  Tabelas Auxiliares (Many-to-Many)

### topic_tags

**Descrição**: Relacionamento N:M entre Topic e Tag.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `topic_id` | Long | FK para Topic |
| `tag_id` | Long | FK para Tag |

**Constraints**:
- PRIMARY KEY (topic_id, tag_id)
- FOREIGN KEY topic_id → topics(id)
- FOREIGN KEY tag_id → tags(id)

---

##  Índices para Performance

```sql
-- Índices em User
CREATE INDEX idx_user_username ON users(username);
CREATE INDEX idx_user_email ON users(email);

-- Índices em Topic
CREATE INDEX idx_topic_category ON topics(category_id);
CREATE INDEX idx_topic_author ON topics(author_id);
CREATE INDEX idx_topic_created ON topics(created_at DESC);
CREATE INDEX idx_topic_last_activity ON topics(last_activity_at DESC);

-- Índices em Post
CREATE INDEX idx_post_topic ON posts(topic_id);
CREATE INDEX idx_post_author ON posts(author_id);
CREATE INDEX idx_post_created ON posts(created_at);

-- Índices em Like
CREATE UNIQUE INDEX idx_like_unique ON likes(post_id, user_id);
CREATE INDEX idx_like_post ON likes(post_id);

-- Índices em ChatMessage
CREATE INDEX idx_chat_message_chat ON chat_messages(chat_id);
CREATE INDEX idx_chat_message_sent ON chat_messages(sent_at);

-- Índices em ChatParticipation
CREATE INDEX idx_chat_participation_chat ON chat_participations(chat_id);
CREATE INDEX idx_chat_participation_user ON chat_participations(user_id);
CREATE INDEX idx_chat_participation_active ON chat_participations(is_active);
```

---

##  Limites e Constraints

| Entidade | Campo | Limite |
|----------|-------|--------|
| User | username | 3-50 caracteres |
| User | email | 255 caracteres |
| User | password | Hash de 60 caracteres (BCrypt) |
| User | fullName | 3-100 caracteres |
| Category | name | 3-100 caracteres |
| Category | description | 10-500 caracteres |
| Tag | name | 2-50 caracteres |
| Topic | title | 5-200 caracteres |
| Topic | content | TEXT (65,535 bytes) |
| Post | content | TEXT (65,535 bytes) |
| Chat | name | 255 caracteres |
| ChatMessage | content | TEXT (65,535 bytes) |

---

##  Regras de Cascata

### ON DELETE CASCADE
- User deleta → Soft delete em todos seus Topics/Posts
- Category deleta →  RESTRICT se tiver Topics
- Tag deleta →  RESTRICT se tiver Topics
- Topic deleta → CASCADE para Posts, Likes, Chat
- Post deleta → CASCADE para Likes
- Chat deleta → CASCADE para ChatMessages e ChatParticipations

### ON UPDATE CASCADE
- Todos os relacionamentos propagam atualizações de ID

---

##  Estados e Transições

### Estado de Topic
```
┌─────────┐
│  OPEN   │──┐
└────┬────┘  │
     │       │ admin.close()
     │       ▼
     │   ┌─────────┐
     │   │ CLOSED  │
     │   └─────────┘
     │
     │ admin.pin()
     ▼
┌─────────┐
│ PINNED  │
└─────────┘
```

### Estado de Chat
```
┌─────────┐
│ ACTIVE  │ admin.close()
└────┬────┘     ─────────►┌─────────┐
     │                    │ CLOSED  │
     │                    └─────────┘
     │ participations
     ▼
[mensagens permitidas]
```

### Estado de Post
```
┌─────────┐
│ CREATED │ author.edit()
└────┬────┘     ─────────►┌─────────┐
     │                    │ EDITED  │
     │                    └─────────┘
     │ admin.delete()
     ▼
┌─────────┐
│ DELETED │ (soft delete)
└─────────┘
```

---

##  Queries Comuns

### Top 10 Tópicos Mais Vistos
```sql
SELECT * FROM topics 
ORDER BY view_count DESC 
LIMIT 10;
```

### Posts Mais Curtidos do Mês
```sql
SELECT p.*, COUNT(l.id) as like_count
FROM posts p
LEFT JOIN likes l ON p.id = l.post_id
WHERE p.created_at >= DATE_SUB(NOW(), INTERVAL 1 MONTH)
GROUP BY p.id
ORDER BY like_count DESC
LIMIT 10;
```

### Usuários Mais Ativos
```sql
SELECT u.*, COUNT(p.id) as post_count
FROM users u
LEFT JOIN posts p ON u.id = p.author_id
GROUP BY u.id
ORDER BY post_count DESC;
```

### Tópicos Sem Respostas
```sql
SELECT t.* FROM topics t
WHERE (SELECT COUNT(*) FROM posts 
       WHERE topic_id = t.id) = 1;
```

### Categorias Mais Populares
```sql
SELECT c.*, COUNT(t.id) as topic_count
FROM categories c
LEFT JOIN topics t ON c.id = t.category_id
GROUP BY c.id
ORDER BY topic_count DESC;
```

---

##  Segurança dos Dados

### Dados Sensíveis
- **Password**: Criptografado com BCrypt (salt rounds: 10)
- **Email**: Usado apenas para autenticação (não exibido publicamente)
- **IP Address**: Armazenado apenas para auditoria

### Validações no Backend
-  Validação de formato de email
-  Sanitização de HTML em posts (prevenir XSS)
-  Rate limiting por IP (futuro)
-  Token JWT com expiração de 24h

### Permissões
```
USER pode:
- Criar topics
- Criar posts
- Editar/deletar próprios posts
- Curtir posts
- Participar de chats

ADMIN pode:
- Tudo que USER pode
- Criar/editar/deletar categories
- Criar/editar/deletar tags
- Deletar qualquer post/topic
- Fechar topics
- Fixar topics
- Fechar chats
```

---

##  Recursos Relacionados

- [Documentação da API](API.md)
- [Guia de Início Rápido](GUIA_RAPIDO.md)
- [Casos de Uso](CASOS_DE_USO.md)
- [Índice Geral](README.md)

---

**Última Atualização**: 14 de fevereiro de 2026  
**Versão**: 1.0.0 (MVP)
