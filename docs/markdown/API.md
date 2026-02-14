# Documentação da API - Sistema de Fórum Web

## Índice

1. [Visão Geral](#visão-geral)
2. [Informações Gerais](#informações-gerais)
3. [Autenticação](#autenticação)
4. [Usuários](#usuários)
5. [Categorias](#categorias)
6. [Tags](#tags)
7. [Tópicos](#tópicos)
8. [Posts](#posts)
9. [Likes](#likes)
10. [Chats](#chats)
11. [Administração](#administração)
12. [Códigos de Status HTTP](#códigos-de-status-http)
13. [Tratamento de Erros](#tratamento-de-erros)

---

## Visão Geral

Esta é a documentação oficial da API REST do Sistema de Fórum Web desenvolvido para a disciplina de Programação Web. A API permite a criação e gerenciamento de fóruns de discussão com funcionalidades de categorias, tópicos, posts, likes e chats em tempo real.

### Tecnologias Utilizadas

- **Framework**: Spring Boot 4.0.2
- **Linguagem**: Java 17
- **Banco de Dados**: H2 (desenvolvimento) / PostgreSQL (produção)
- **Autenticação**: Spring Security (JWT)
- **ORM**: Hibernate/JPA

---

## Informações Gerais

### URL Base

```
http://localhost:8080/api
```

### Formato de Dados

- **Request**: JSON (`Content-Type: application/json`)
- **Response**: JSON

### Cabeçalhos Comuns

```http
Content-Type: application/json
Accept: application/json
Authorization: Bearer {token}  # Para endpoints autenticados
```

### Convenções

- Todos os endpoints retornam JSON
- Datas no formato ISO 8601: `2026-02-14T10:30:00Z`
- IDs são do tipo `Long` (número inteiro)
- Campos obrigatórios são validados automaticamente

---

## Autenticação

### Registrar Novo Usuário

Cria uma nova conta de usuário no sistema.

**Endpoint**: `POST /auth/register`

**Request Body**:
```json
{
  "username": "jefferson",
  "email": "jefferson@example.com",
  "password": "senha123",
  "fullName": "Jefferson Silva"
}
```

**Validações**:
- `username`: obrigatório, 3-50 caracteres, único
- `email`: obrigatório, formato válido de email, único
- `password`: obrigatório, mínimo 6 caracteres
- `fullName`: obrigatório, 3-100 caracteres

**Response** (201 Created):
```json
{
  "userId": 1,
  "username": "jefferson",
  "email": "jefferson@example.com",
  "fullName": "Jefferson Silva",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresAt": "2026-02-15T10:30:00Z"
}
```

**Códigos de Status**:
- `201`: Usuário criado com sucesso
- `400`: Dados inválidos ou usuário já existe
- `500`: Erro interno do servidor

---

### Login

Autentica um usuário existente.

**Endpoint**: `POST /auth/login`

**Request Body**:
```json
{
  "username": "jefferson",
  "password": "senha123"
}
```

**Validações**:
- `username`: obrigatório
- `password`: obrigatório

**Response** (200 OK):
```json
{
  "userId": 1,
  "username": "jefferson",
  "email": "jefferson@example.com",
  "fullName": "Jefferson Silva",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresAt": "2026-02-15T10:30:00Z"
}
```

**Códigos de Status**:
- `200`: Login realizado com sucesso
- `400`: Credenciais inválidas
- `401`: Não autorizado

---

## Usuários

### Listar Todos os Usuários

Retorna a lista de todos os usuários cadastrados.

**Endpoint**: `GET /users`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "username": "jefferson",
    "email": "jefferson@example.com",
    "fullName": "Jefferson Silva",
    "role": "USER"
  },
  {
    "id": 2,
    "username": "maria",
    "email": "maria@example.com",
    "fullName": "Maria Santos",
    "role": "ADMIN"
  }
]
```

---

### Buscar Usuário por ID

Retorna os dados de um usuário específico.

**Endpoint**: `GET /users/{userId}`

**Parâmetros de URL**:
- `userId` (Long): ID do usuário

**Response** (200 OK):
```json
{
  "id": 1,
  "username": "jefferson",
  "email": "jefferson@example.com",
  "fullName": "Jefferson Silva",
  "role": "USER"
}
```

**Códigos de Status**:
- `200`: Usuário encontrado
- `404`: Usuário não encontrado

---

## Categorias

### Listar Todas as Categorias

Retorna todas as categorias disponíveis no fórum.

**Endpoint**: `GET /categories`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "name": "Programação",
    "description": "Discussões sobre desenvolvimento de software",
    "topicCount": 45
  },
  {
    "id": 2,
    "name": "Design",
    "description": "UI/UX e design de interfaces",
    "topicCount": 23
  }
]
```

---

### Buscar Categoria por ID

Retorna os detalhes de uma categoria específica.

**Endpoint**: `GET /categories/{categoryId}`

**Parâmetros de URL**:
- `categoryId` (Long): ID da categoria

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Programação",
  "description": "Discussões sobre desenvolvimento de software",
  "topicCount": 45
}
```

**Códigos de Status**:
- `200`: Categoria encontrada
- `404`: Categoria não encontrada

---

## Tags

### Listar Todas as Tags

Retorna todas as tags disponíveis.

**Endpoint**: `GET /tags`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "name": "Java",
    "topicCount": 32
  },
  {
    "id": 2,
    "name": "Spring Boot",
    "topicCount": 28
  },
  {
    "id": 3,
    "name": "React",
    "topicCount": 15
  }
]
```

---

### Buscar Tag por ID

Retorna os detalhes de uma tag específica.

**Endpoint**: `GET /tags/{tagId}`

**Parâmetros de URL**:
- `tagId` (Long): ID da tag

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Java",
  "topicCount": 32
}
```

**Códigos de Status**:
- `200`: Tag encontrada
- `404`: Tag não encontrada

---

## Tópicos

### Criar Novo Tópico

Cria um novo tópico de discussão.

**Endpoint**: `POST /topics`

**Requer Autenticação**: Sim

**Request Body**:
```json
{
  "title": "Como usar Spring Security?",
  "content": "Gostaria de saber as melhores práticas para implementar autenticação com Spring Security.",
  "categoryId": 1,
  "tagIds": [1, 2]
}
```

**Validações**:
- `title`: obrigatório, 5-200 caracteres
- `content`: obrigatório, mínimo 10 caracteres
- `categoryId`: obrigatório, deve existir
- `tagIds`: opcional, lista de IDs de tags válidas

**Response** (200 OK):
```json
{
  "id": 15,
  "title": "Como usar Spring Security?",
  "content": "Gostaria de saber as melhores práticas para implementar autenticação com Spring Security.",
  "categoryId": 1,
  "authorId": 1,
  "authorUsername": "jefferson",
  "tags": [
    {
      "id": 1,
      "name": "Java",
      "topicCount": 33
    },
    {
      "id": 2,
      "name": "Spring Boot",
      "topicCount": 29
    }
  ],
  "viewCount": 0,
  "postCount": 1,
  "createdAt": "2026-02-14T10:30:00Z",
  "lastActivityAt": "2026-02-14T10:30:00Z",
  "isPinned": false,
  "isClosed": false
}
```

**Códigos de Status**:
- `200`: Tópico criado com sucesso
- `400`: Dados inválidos
- `401`: Não autenticado
- `404`: Categoria ou tag não encontrada

---

### Listar Todos os Tópicos

Retorna todos os tópicos do fórum.

**Endpoint**: `GET /topics`

**Response** (200 OK):
```json
[
  {
    "id": 15,
    "title": "Como usar Spring Security?",
    "content": "Gostaria de saber as melhores práticas...",
    "categoryId": 1,
    "authorId": 1,
    "authorUsername": "jefferson",
    "tags": [
      {
        "id": 1,
        "name": "Java",
        "topicCount": 33
      }
    ],
    "viewCount": 42,
    "postCount": 8,
    "createdAt": "2026-02-14T10:30:00Z",
    "lastActivityAt": "2026-02-14T15:20:00Z",
    "isPinned": false,
    "isClosed": false
  }
]
```

---

### Buscar Tópico por ID

Retorna os detalhes completos de um tópico.

**Endpoint**: `GET /topics/{topicId}`

**Parâmetros de URL**:
- `topicId` (Long): ID do tópico

**Response** (200 OK):
```json
{
  "id": 15,
  "title": "Como usar Spring Security?",
  "content": "Gostaria de saber as melhores práticas para implementar autenticação com Spring Security.",
  "categoryId": 1,
  "authorId": 1,
  "authorUsername": "jefferson",
  "tags": [
    {
      "id": 1,
      "name": "Java",
      "topicCount": 33
    },
    {
      "id": 2,
      "name": "Spring Boot",
      "topicCount": 29
    }
  ],
  "viewCount": 42,
  "postCount": 8,
  "createdAt": "2026-02-14T10:30:00Z",
  "lastActivityAt": "2026-02-14T15:20:00Z",
  "isPinned": false,
  "isClosed": false
}
```

**Códigos de Status**:
- `200`: Tópico encontrado
- `404`: Tópico não encontrado

---

## Posts

### Criar Novo Post

Adiciona uma resposta a um tópico existente.

**Endpoint**: `POST /posts`

**Requer Autenticação**: Sim

**Request Body**:
```json
{
  "content": "Para usar Spring Security, você precisa adicionar a dependência no pom.xml e criar uma classe de configuração...",
  "topicId": 15
}
```

**Validações**:
- `content`: obrigatório, mínimo 10 caracteres
- `topicId`: obrigatório, deve existir

**Response** (201 Created):
```json
{
  "id": 42,
  "topicId": 15,
  "authorId": 2,
  "content": "Para usar Spring Security, você precisa adicionar a dependência no pom.xml e criar uma classe de configuração...",
  "createdAt": "2026-02-14T11:00:00Z",
  "updatedAt": null
}
```

**Códigos de Status**:
- `201`: Post criado com sucesso
- `400`: Dados inválidos
- `401`: Não autenticado
- `404`: Tópico não encontrado

---

### Buscar Post por ID

Retorna um post específico.

**Endpoint**: `GET /posts/{postId}`

**Parâmetros de URL**:
- `postId` (Long): ID do post

**Response** (200 OK):
```json
{
  "id": 42,
  "topicId": 15,
  "authorId": 2,
  "content": "Para usar Spring Security, você precisa adicionar a dependência no pom.xml...",
  "createdAt": "2026-02-14T11:00:00Z",
  "updatedAt": null
}
```

**Códigos de Status**:
- `200`: Post encontrado
- `404`: Post não encontrado

---

### Listar Posts de um Tópico

Retorna todos os posts de um tópico específico, ordenados cronologicamente.

**Endpoint**: `GET /posts/topic/{topicId}`

**Parâmetros de URL**:
- `topicId` (Long): ID do tópico

**Response** (200 OK):
```json
[
  {
    "id": 41,
    "topicId": 15,
    "authorId": 1,
    "content": "Conteúdo do post principal do tópico...",
    "createdAt": "2026-02-14T10:30:00Z",
    "updatedAt": null
  },
  {
    "id": 42,
    "topicId": 15,
    "authorId": 2,
    "content": "Para usar Spring Security...",
    "createdAt": "2026-02-14T11:00:00Z",
    "updatedAt": null
  }
]
```

---

### Editar Post

Atualiza o conteúdo de um post existente.

**Endpoint**: `PATCH /posts/{postId}`

**Requer Autenticação**: Sim (apenas o autor do post)

**Parâmetros de URL**:
- `postId` (Long): ID do post

**Request Body**:
```json
{
  "content": "Para usar Spring Security, você precisa adicionar a dependência no pom.xml e criar uma classe SecurityConfig..."
}
```

**Validações**:
- `content`: obrigatório, mínimo 10 caracteres

**Response** (200 OK):
```json
{
  "id": 42,
  "topicId": 15,
  "authorId": 2,
  "content": "Para usar Spring Security, você precisa adicionar a dependência no pom.xml e criar uma classe SecurityConfig...",
  "createdAt": "2026-02-14T11:00:00Z",
  "updatedAt": "2026-02-14T11:30:00Z"
}
```

**Códigos de Status**:
- `200`: Post atualizado
- `400`: Dados inválidos
- `401`: Não autenticado
- `403`: Não autorizado (não é o autor)
- `404`: Post não encontrado

---

### Deletar Post

Remove um post do sistema.

**Endpoint**: `DELETE /posts/{postId}`

**Requer Autenticação**: Sim (apenas o autor ou admin)

**Parâmetros de URL**:
- `postId` (Long): ID do post

**Response** (204 No Content):
```
(corpo vazio)
```

**Códigos de Status**:
- `204`: Post deletado com sucesso
- `401`: Não autenticado
- `403`: Não autorizado
- `404`: Post não encontrado

---

## Likes

### Curtir Post

Adiciona um like a um post.

**Endpoint**: `POST /posts/{postId}/likes`

**Requer Autenticação**: Sim

**Parâmetros de URL**:
- `postId` (Long): ID do post

**Response** (201 Created):
```
(corpo vazio)
```

**Códigos de Status**:
- `201`: Like adicionado
- `400`: Usuário já curtiu este post
- `401`: Não autenticado
- `404`: Post não encontrado

---

### Remover Curtida

Remove o like de um post.

**Endpoint**: `DELETE /posts/{postId}/likes`

**Requer Autenticação**: Sim

**Parâmetros de URL**:
- `postId` (Long): ID do post

**Response** (204 No Content):
```
(corpo vazio)
```

**Códigos de Status**:
- `204`: Like removido
- `400`: Usuário não tinha curtido este post
- `401`: Não autenticado
- `404`: Post não encontrado

---

### Contar Likes de um Post

Retorna o número total de likes de um post.

**Endpoint**: `GET /posts/{postId}/likes/count`

**Parâmetros de URL**:
- `postId` (Long): ID do post

**Response** (200 OK):
```json
15
```

**Códigos de Status**:
- `200`: Contagem retornada
- `404`: Post não encontrado

---

## Chats

### Criar Chat para Tópico

Cria uma sala de chat associada a um tópico.

**Endpoint**: `POST /chats/topic/{topicId}`

**Requer Autenticação**: Sim

**Parâmetros de URL**:
- `topicId` (Long): ID do tópico

**Response** (201 Created):
```json
1
```

**Códigos de Status**:
- `201`: Chat criado com sucesso
- `400`: Chat já existe para este tópico
- `401`: Não autenticado
- `404`: Tópico não encontrado

---

### Buscar Chat por ID

Retorna informações de um chat específico.

**Endpoint**: `GET /chats/{chatId}`

**Parâmetros de URL**:
- `chatId` (Long): ID do chat

**Response** (200 OK):
```json
{
  "id": 1,
  "topicId": 15,
  "name": "Chat - Como usar Spring Security?",
  "active": true,
  "createdAt": "2026-02-14T10:30:00Z",
  "participantCount": 8
}
```

**Códigos de Status**:
- `200`: Chat encontrado
- `404`: Chat não encontrado

---

### Listar Mensagens do Chat

Retorna todas as mensagens de um chat.

**Endpoint**: `GET /chats/{chatId}/messages`

**Parâmetros de URL**:
- `chatId` (Long): ID do chat

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "chatId": 1,
    "userId": 1,
    "username": "jefferson",
    "content": "Alguém pode me ajudar com isso?",
    "messageType": "TEXT",
    "sentAt": "2026-02-14T11:00:00Z"
  },
  {
    "id": 2,
    "chatId": 1,
    "userId": 2,
    "username": "maria",
    "content": "Claro! Qual é a dúvida específica?",
    "messageType": "TEXT",
    "sentAt": "2026-02-14T11:02:00Z"
  }
]
```

---

### Enviar Mensagem no Chat

Envia uma nova mensagem em um chat.

**Endpoint**: `POST /chats/{chatId}/messages`

**Requer Autenticação**: Sim

**Parâmetros de URL**:
- `chatId` (Long): ID do chat

**Request Body**:
```json
{
  "chatId": 1,
  "content": "Obrigado pela ajuda!",
  "messageType": "TEXT"
}
```

**Validações**:
- `chatId`: obrigatório
- `content`: obrigatório, não vazio
- `messageType`: obrigatório (TEXT, IMAGE, FILE)

**Response** (201 Created):
```json
{
  "id": 3,
  "chatId": 1,
  "userId": 1,
  "username": "jefferson",
  "content": "Obrigado pela ajuda!",
  "messageType": "TEXT",
  "sentAt": "2026-02-14T11:05:00Z"
}
```

**Códigos de Status**:
- `201`: Mensagem enviada
- `400`: Dados inválidos
- `401`: Não autenticado
- `404`: Chat não encontrado

---

### Entrar no Chat

Adiciona o usuário atual como participante do chat.

**Endpoint**: `POST /chats/{chatId}/participants/join`

**Requer Autenticação**: Sim

**Parâmetros de URL**:
- `chatId` (Long): ID do chat

**Response** (200 OK):
```
(corpo vazio)
```

**Códigos de Status**:
- `200`: Usuário adicionado ao chat
- `400`: Usuário já está no chat
- `401`: Não autenticado
- `404`: Chat não encontrado

---

### Sair do Chat

Remove o usuário atual dos participantes do chat.

**Endpoint**: `POST /chats/{chatId}/participants/leave`

**Requer Autenticação**: Sim

**Parâmetros de URL**:
- `chatId` (Long): ID do chat

**Response** (200 OK):
```
(corpo vazio)
```

**Códigos de Status**:
- `200`: Usuário removido do chat
- `400`: Usuário não está no chat
- `401`: Não autenticado
- `404`: Chat não encontrado

---

### Contar Participantes Ativos

Retorna o número de participantes ativos em um chat.

**Endpoint**: `GET /chats/{chatId}/participants/count`

**Parâmetros de URL**:
- `chatId` (Long): ID do chat

**Response** (200 OK):
```json
8
```

**Códigos de Status**:
- `200`: Contagem retornada
- `404`: Chat não encontrado

---

## Administração

### Criar Categoria (Admin)

Cria uma nova categoria no sistema.

**Endpoint**: `POST /admin/categories`

**Requer Autenticação**: Sim (role: ADMIN)

**Request Body**:
```json
{
  "name": "Inteligência Artificial",
  "description": "Discussões sobre IA, Machine Learning e Deep Learning"
}
```

**Validações**:
- `name`: obrigatório, 3-100 caracteres, único
- `description`: obrigatório, 10-500 caracteres

**Response** (201 Created):
```json
{
  "id": 5,
  "name": "Inteligência Artificial",
  "description": "Discussões sobre IA, Machine Learning e Deep Learning"
}
```

**Códigos de Status**:
- `201`: Categoria criada
- `400`: Dados inválidos ou nome duplicado
- `401`: Não autenticado
- `403`: Sem permissão de admin

---

### Atualizar Categoria (Admin)

Atualiza uma categoria existente.

**Endpoint**: `PUT /admin/categories/{categoryId}`

**Requer Autenticação**: Sim (role: ADMIN)

**Parâmetros de URL**:
- `categoryId` (Long): ID da categoria

**Request Body**:
```json
{
  "name": "Inteligência Artificial",
  "description": "Discussões sobre IA, Machine Learning, Deep Learning e Data Science"
}
```

**Response** (200 OK):
```json
{
  "id": 5,
  "name": "Inteligência Artificial",
  "description": "Discussões sobre IA, Machine Learning, Deep Learning e Data Science"
}
```

**Códigos de Status**:
- `200`: Categoria atualizada
- `400`: Dados inválidos
- `401`: Não autenticado
- `403`: Sem permissão de admin
- `404`: Categoria não encontrada

---

### Deletar Categoria (Admin)

Remove uma categoria do sistema.

**Endpoint**: `DELETE /admin/categories/{categoryId}`

**Requer Autenticação**: Sim (role: ADMIN)

**Parâmetros de URL**:
- `categoryId` (Long): ID da categoria

**Response** (204 No Content):
```
(corpo vazio)
```

**Códigos de Status**:
- `204`: Categoria deletada
- `400`: Categoria contém tópicos
- `401`: Não autenticado
- `403`: Sem permissão de admin
- `404`: Categoria não encontrada

---

### Criar Tag (Admin)

Cria uma nova tag no sistema.

**Endpoint**: `POST /admin/tags`

**Requer Autenticação**: Sim (role: ADMIN)

**Request Body**:
```json
{
  "name": "Kubernetes"
}
```

**Validações**:
- `name`: obrigatório, 2-50 caracteres, único

**Response** (201 Created):
```json
{
  "id": 8,
  "name": "Kubernetes"
}
```

**Códigos de Status**:
- `201`: Tag criada
- `400`: Dados inválidos ou nome duplicado
- `401`: Não autenticado
- `403`: Sem permissão de admin

---

### Atualizar Tag (Admin)

Atualiza uma tag existente.

**Endpoint**: `PUT /admin/tags/{tagId}`

**Requer Autenticação**: Sim (role: ADMIN)

**Parâmetros de URL**:
- `tagId` (Long): ID da tag

**Request Body**:
```json
{
  "name": "K8s"
}
```

**Response** (200 OK):
```json
{
  "id": 8,
  "name": "K8s"
}
```

**Códigos de Status**:
- `200`: Tag atualizada
- `400`: Dados inválidos
- `401`: Não autenticado
- `403`: Sem permissão de admin
- `404`: Tag não encontrada

---

### Deletar Tag (Admin)

Remove uma tag do sistema.

**Endpoint**: `DELETE /admin/tags/{tagId}`

**Requer Autenticação**: Sim (role: ADMIN)

**Parâmetros de URL**:
- `tagId` (Long): ID da tag

**Response** (204 No Content):
```
(corpo vazio)
```

**Códigos de Status**:
- `204`: Tag deletada
- `400`: Tag está sendo usada em tópicos
- `401`: Não autenticado
- `403`: Sem permissão de admin
- `404`: Tag não encontrada

---

## Códigos de Status HTTP

### Códigos de Sucesso

| Código | Significado | Uso |
|--------|-------------|-----|
| `200 OK` | Requisição bem-sucedida | GET, PUT, PATCH |
| `201 Created` | Recurso criado com sucesso | POST |
| `204 No Content` | Operação bem-sucedida sem conteúdo | DELETE |

### Códigos de Erro do Cliente

| Código | Significado | Causa Comum |
|--------|-------------|-------------|
| `400 Bad Request` | Dados inválidos | Validação falhou, dados duplicados |
| `401 Unauthorized` | Não autenticado | Token ausente ou inválido |
| `403 Forbidden` | Sem permissão | Tentativa de acessar recurso sem autorização |
| `404 Not Found` | Recurso não encontrado | ID inexistente |

### Códigos de Erro do Servidor

| Código | Significado | Causa Comum |
|--------|-------------|-------------|
| `500 Internal Server Error` | Erro interno | Erro não tratado no servidor |

---

## Tratamento de Erros

### Formato de Resposta de Erro

Todos os erros retornam um JSON padronizado:

```json
{
  "timestamp": "2026-02-14T11:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/topics",
  "errors": [
    {
      "field": "title",
      "message": "O título deve ter entre 5 e 200 caracteres"
    },
    {
      "field": "categoryId",
      "message": "Categoria não encontrada"
    }
  ]
}
```

### Exemplos de Erros Comuns

#### Erro de Validação
```json
{
  "timestamp": "2026-02-14T11:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "O conteúdo deve ter no mínimo 10 caracteres",
  "path": "/api/posts"
}
```

#### Recurso Não Encontrado
```json
{
  "timestamp": "2026-02-14T11:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Post not found with id: 999",
  "path": "/api/posts/999"
}
```

#### Não Autorizado
```json
{
  "timestamp": "2026-02-14T11:30:00Z",
  "status": 403,
  "error": "Forbidden",
  "message": "You don't have permission to edit this post",
  "path": "/api/posts/42"
}
```

---

## Notas Importantes

### Autenticação

- A maioria dos endpoints requer autenticação via token JWT
- O token deve ser incluído no header `Authorization: Bearer {token}`
- Tokens expiram após 24 horas
- Após expiração, faça login novamente para obter novo token

### Paginação

- **Nota**: A paginação não está implementada na versão MVP
- Futuras versões incluirão parâmetros `page`, `size` e `sort`

### WebSockets

- **Nota**: Comunicação em tempo real para chats não está implementada na versão MVP
- A versão atual utiliza polling para mensagens
- Futuras versões incluirão suporte a WebSocket/STOMP

### Rate Limiting

- **Nota**: Limitação de taxa não está implementada na versão MVP
- Recomendado para produção: máximo 100 requisições por minuto

### CORS

- Atualmente configurado para aceitar requisições de qualquer origem (desenvolvimento)
- Em produção, configurar origens específicas permitidas

---

## Exemplos de Uso com cURL

### Registrar e Fazer Login

```bash
# Registrar novo usuário
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jefferson",
    "email": "jefferson@example.com",
    "password": "senha123",
    "fullName": "Jefferson Silva"
  }'

# Fazer login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jefferson",
    "password": "senha123"
  }'
```

### Criar Tópico

```bash
curl -X POST http://localhost:8080/api/topics \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "title": "Como usar Spring Security?",
    "content": "Gostaria de saber as melhores práticas...",
    "categoryId": 1,
    "tagIds": [1, 2]
  }'
```

### Listar Tópicos

```bash
curl -X GET http://localhost:8080/api/topics
```

### Criar Post

```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "content": "Para usar Spring Security...",
    "topicId": 15
  }'
```

### Curtir Post

```bash
curl -X POST http://localhost:8080/api/posts/42/likes \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

**Última Atualização**: 14 de fevereiro de 2026  
**Versão da API**: 1.0.0 (MVP)  
**Desenvolvido para**: Programação Web - 5º Período - IFF
