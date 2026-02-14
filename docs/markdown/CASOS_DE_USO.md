# Casos de Uso - Exemplos Práticos

Este documento apresenta cenários práticos de uso da API do Sistema de Fórum Web.

---

## Índice de Casos de Uso

1. [Novo Usuário Registrando e Criando Primeiro Tópico](#caso-1-novo-usuário-registrando-e-criando-primeiro-tópico)
2. [Usuário Participando de Discussão Existente](#caso-2-usuário-participando-de-discussão-existente)
3. [Admin Organizando o Fórum](#caso-3-admin-organizando-o-fórum)
4. [Usuário Editando Conteúdo Próprio](#caso-4-usuário-editando-conteúdo-próprio)
5. [Chat em Tempo Real para Dúvidas Rápidas](#caso-5-chat-em-tempo-real-para-dúvidas-rápidas)
6. [Busca e Navegação no Fórum](#caso-6-busca-e-navegação-no-fórum)
7. [Sistema de Reputação via Likes](#caso-7-sistema-de-reputação-via-likes)
8. [Moderação de Conteúdo](#caso-8-moderação-de-conteúdo)

---

## Caso 1: Novo Usuário Registrando e Criando Primeiro Tópico

### Cenário
Jefferson é estudante de Ciência da Computação e está aprendendo Spring Boot. Ele quer tirar uma dúvida no fórum.

### Fluxo Completo

#### Passo 1: Registrar no Sistema
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jefferson_dev",
    "email": "jefferson@aluno.iff.edu.br",
    "password": "senha@123",
    "fullName": "Jefferson Silva"
  }'
```

**Resposta:**
```json
{
  "userId": 5,
  "username": "jefferson_dev",
  "email": "jefferson@aluno.iff.edu.br",
  "fullName": "Jefferson Silva",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjUsInVzZXJuYW1lIjoiamVmZmVyc29uX2RldiIsImV4cCI6MTcwODU3MjAwMH0.xyz",
  "expiresAt": "2026-02-15T10:30:00Z"
}
```

#### Passo 2: Explorar Categorias Disponíveis
```bash
curl http://localhost:8080/api/categories
```

**Resposta:**
```json
[
  {
    "id": 1,
    "name": "Programação",
    "description": "Discussões sobre desenvolvimento de software",
    "topicCount": 23
  },
  {
    "id": 2,
    "name": "Banco de Dados",
    "description": "SQL, NoSQL e modelagem de dados",
    "topicCount": 15
  }
]
```

#### Passo 3: Ver Tags Disponíveis
```bash
curl http://localhost:8080/api/tags
```

**Resposta:**
```json
[
  {
    "id": 1,
    "name": "Java",
    "topicCount": 18
  },
  {
    "id": 2,
    "name": "Spring Boot",
    "topicCount": 12
  },
  {
    "id": 3,
    "name": "JPA",
    "topicCount": 8
  }
]
```

#### Passo 4: Criar Novo Tópico
```bash
curl -X POST http://localhost:8080/api/topics \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -d '{
    "title": "Como mapear relacionamento ManyToMany com JPA?",
    "content": "Estou tentando criar um relacionamento entre User e Role usando @ManyToMany, mas estou tendo problemas com a tabela intermediária. Alguém pode me ajudar com um exemplo completo?",
    "categoryId": 1,
    "tagIds": [1, 2, 3]
  }'
```

**Resposta:**
```json
{
  "id": 42,
  "title": "Como mapear relacionamento ManyToMany com JPA?",
  "content": "Estou tentando criar um relacionamento entre User e Role...",
  "categoryId": 1,
  "authorId": 5,
  "authorUsername": "jefferson_dev",
  "tags": [
    {"id": 1, "name": "Java", "topicCount": 19},
    {"id": 2, "name": "Spring Boot", "topicCount": 13},
    {"id": 3, "name": "JPA", "topicCount": 9}
  ],
  "viewCount": 0,
  "postCount": 1,
  "createdAt": "2026-02-14T14:30:00Z",
  "lastActivityAt": "2026-02-14T14:30:00Z",
  "isPinned": false,
  "isClosed": false
}
```

### Resultado
Jefferson agora tem uma conta e criou seu primeiro tópico de discussão!

---

## Caso 2: Usuário Participando de Discussão Existente

### Cenário
Maria é desenvolvedora sênior e viu a dúvida do Jefferson. Ela quer ajudar.

### Fluxo Completo

#### Passo 1: Maria Faz Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "maria_senior",
    "password": "senhaSegura"
  }'
```

#### Passo 2: Visualizar o Tópico
```bash
curl http://localhost:8080/api/topics/42
```

#### Passo 3: Ver Posts Existentes
```bash
curl http://localhost:8080/api/posts/topic/42
```

**Resposta:**
```json
[
  {
    "id": 85,
    "topicId": 42,
    "authorId": 5,
    "content": "Estou tentando criar um relacionamento entre User e Role usando @ManyToMany...",
    "createdAt": "2026-02-14T14:30:00Z",
    "updatedAt": null
  }
]
```

#### Passo 4: Maria Responde com Solução Detalhada
```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DA_MARIA" \
  -d '{
    "content": "Olá Jefferson! Para mapear ManyToMany com JPA, você precisa:\n\n1. Na classe User:\n```java\n@ManyToMany\n@JoinTable(\n  name = \"user_roles\",\n  joinColumns = @JoinColumn(name = \"user_id\"),\n  inverseJoinColumns = @JoinColumn(name = \"role_id\")\n)\nprivate Set<Role> roles = new HashSet<>();\n```\n\n2. Na classe Role:\n```java\n@ManyToMany(mappedBy = \"roles\")\nprivate Set<User> users = new HashSet<>();\n```\n\nIsso criará automaticamente a tabela user_roles com as colunas user_id e role_id. Precisa de mais detalhes?",
    "topicId": 42
  }'
```

**Resposta:**
```json
{
  "id": 86,
  "topicId": 42,
  "authorId": 8,
  "content": "Olá Jefferson! Para mapear ManyToMany com JPA...",
  "createdAt": "2026-02-14T14:45:00Z",
  "updatedAt": null
}
```

#### Passo 5: Jefferson Curte a Resposta
```bash
curl -X POST http://localhost:8080/api/posts/86/likes \
  -H "Authorization: Bearer TOKEN_DO_JEFFERSON"
```

#### Passo 6: Jefferson Agradece
```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DO_JEFFERSON" \
  -d '{
    "content": "Muito obrigado Maria! Funcionou perfeitamente! A explicação ficou super clara.",
    "topicId": 42
  }'
```

### Resultado
Discussão produtiva estabelecida! O problema foi resolvido e o conhecimento foi compartilhado.

---

## Caso 3: Admin Organizando o Fórum

### Cenário
Carlos é administrador do fórum e precisa criar novas categorias e tags para organizar melhor as discussões.

### Fluxo Completo

#### Passo 1: Criar Nova Categoria
```bash
curl -X POST http://localhost:8080/api/admin/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DO_ADMIN" \
  -d '{
    "name": "DevOps",
    "description": "Docker, Kubernetes, CI/CD e automação de infraestrutura"
  }'
```

#### Passo 2: Criar Várias Tags
```bash
# Tag 1
curl -X POST http://localhost:8080/api/admin/tags \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DO_ADMIN" \
  -d '{"name": "Docker"}'

# Tag 2
curl -X POST http://localhost:8080/api/admin/tags \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DO_ADMIN" \
  -d '{"name": "Kubernetes"}'

# Tag 3
curl -X POST http://localhost:8080/api/admin/tags \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DO_ADMIN" \
  -d '{"name": "GitHub Actions"}'
```

#### Passo 3: Atualizar Descrição de Categoria
```bash
curl -X PUT http://localhost:8080/api/admin/categories/3 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DO_ADMIN" \
  -d '{
    "name": "DevOps",
    "description": "Docker, Kubernetes, CI/CD, automação de infraestrutura e práticas DevOps"
  }'
```

#### Passo 4: Renomear Tag Incorreta
```bash
curl -X PUT http://localhost:8080/api/admin/tags/15 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DO_ADMIN" \
  -d '{
    "name": "GitLab CI/CD"
  }'
```

### Resultado
Fórum melhor organizado com novas categorias e tags relevantes!

---

## Caso 4: Usuário Editando Conteúdo Próprio

### Cenário
Jefferson percebeu um erro no código que postou e quer corrigi-lo.

### Fluxo Completo

#### Passo 1: Visualizar Post Atual
```bash
curl http://localhost:8080/api/posts/87
```

**Resposta:**
```json
{
  "id": 87,
  "topicId": 42,
  "authorId": 5,
  "content": "Consegui implementar! Aqui está meu código:\n```java\n@ManytoMany // erro aqui\nprivate Set<Role> roles;\n```",
  "createdAt": "2026-02-14T15:00:00Z",
  "updatedAt": null
}
```

#### Passo 2: Editar Post Corrigindo o Erro
```bash
curl -X PATCH http://localhost:8080/api/posts/87 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DO_JEFFERSON" \
  -d '{
    "content": "Consegui implementar! Aqui está meu código:\n```java\n@ManyToMany\n@JoinTable(\n  name = \"user_roles\",\n  joinColumns = @JoinColumn(name = \"user_id\"),\n  inverseJoinColumns = @JoinColumn(name = \"role_id\")\n)\nprivate Set<Role> roles = new HashSet<>();\n```\nFuncionou perfeitamente!"
  }'
```

**Resposta:**
```json
{
  "id": 87,
  "topicId": 42,
  "authorId": 5,
  "content": "Consegui implementar! Aqui está meu código...",
  "createdAt": "2026-02-14T15:00:00Z",
  "updatedAt": "2026-02-14T15:10:00Z"
}
```

### Resultado
Post corrigido! O campo `updatedAt` indica que houve edição.

---

## Caso 5: Chat em Tempo Real para Dúvidas Rápidas

### Cenário
Vários usuários querem discutir em tempo real sobre um tópico específico.

### Fluxo Completo

#### Passo 1: Admin Cria Chat para o Tópico
```bash
curl -X POST http://localhost:8080/api/chats/topic/42 \
  -H "Authorization: Bearer TOKEN_DO_ADMIN"
```

**Resposta:**
```json
1
```

#### Passo 2: Usuários Entram no Chat
```bash
# Jefferson entra
curl -X POST http://localhost:8080/api/chats/1/participants/join \
  -H "Authorization: Bearer TOKEN_DO_JEFFERSON"

# Maria entra
curl -X POST http://localhost:8080/api/chats/1/participants/join \
  -H "Authorization: Bearer TOKEN_DA_MARIA"

# Pedro entra
curl -X POST http://localhost:8080/api/chats/1/participants/join \
  -H "Authorization: Bearer TOKEN_DO_PEDRO"
```

#### Passo 3: Ver Informações do Chat
```bash
curl http://localhost:8080/api/chats/1
```

**Resposta:**
```json
{
  "id": 1,
  "topicId": 42,
  "name": "Chat - Como mapear relacionamento ManyToMany com JPA?",
  "active": true,
  "createdAt": "2026-02-14T15:20:00Z",
  "participantCount": 3
}
```

#### Passo 4: Conversação no Chat
```bash
# Jefferson pergunta
curl -X POST http://localhost:8080/api/chats/1/messages \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DO_JEFFERSON" \
  -d '{
    "chatId": 1,
    "content": "E se eu quiser adicionar campos extras na tabela intermediária?",
    "messageType": "TEXT"
  }'

# Maria responde (2 minutos depois)
curl -X POST http://localhost:8080/api/chats/1/messages \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DA_MARIA" \
  -d '{
    "chatId": 1,
    "content": "Nesse caso você precisa criar uma entidade separada para a tabela intermediária com @ManyToOne para ambos os lados",
    "messageType": "TEXT"
  }'

# Pedro complementa
curl -X POST http://localhost:8080/api/chats/1/messages \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_DO_PEDRO" \
  -d '{
    "chatId": 1,
    "content": "Exato! Vou te mandar um link com exemplo: https://exemplo.com/manytomany-extra-columns",
    "messageType": "TEXT"
  }'
```

#### Passo 5: Listar Todas as Mensagens
```bash
curl http://localhost:8080/api/chats/1/messages
```

**Resposta:**
```json
[
  {
    "id": 1,
    "chatId": 1,
    "userId": 5,
    "username": "jefferson_dev",
    "content": "E se eu quiser adicionar campos extras na tabela intermediária?",
    "messageType": "TEXT",
    "sentAt": "2026-02-14T15:25:00Z"
  },
  {
    "id": 2,
    "chatId": 1,
    "userId": 8,
    "username": "maria_senior",
    "content": "Nesse caso você precisa criar uma entidade separada...",
    "messageType": "TEXT",
    "sentAt": "2026-02-14T15:27:00Z"
  },
  {
    "id": 3,
    "chatId": 1,
    "userId": 12,
    "username": "pedro_fullstack",
    "content": "Exato! Vou te mandar um link com exemplo...",
    "messageType": "TEXT",
    "sentAt": "2026-02-14T15:28:00Z"
  }
]
```

#### Passo 6: Pedro Sai do Chat
```bash
curl -X POST http://localhost:8080/api/chats/1/participants/leave \
  -H "Authorization: Bearer TOKEN_DO_PEDRO"
```

### Resultado
Discussão rápida e colaborativa realizada em tempo real!

---

## Caso 6: Busca e Navegação no Fórum

### Cenário
Ana é nova no fórum e quer encontrar tópicos sobre React.

### Fluxo Completo

#### Passo 1: Listar Todas as Tags
```bash
curl http://localhost:8080/api/tags
```

#### Passo 2: Encontrar Tag "React"
**Resposta (filtrada):**
```json
{
  "id": 7,
  "name": "React",
  "topicCount": 18
}
```

#### Passo 3: Listar Todos os Tópicos
```bash
curl http://localhost:8080/api/topics
```

#### Passo 4: Filtrar Tópicos por Categoria Frontend (ID 4)
```bash
# Na aplicação frontend, filtrar localmente os tópicos onde categoryId === 4
```

#### Passo 5: Acessar Tópico Específico
```bash
curl http://localhost:8080/api/topics/28
```

#### Passo 6: Ver Todas as Respostas
```bash
curl http://localhost:8080/api/posts/topic/28
```

#### Passo 7: Ver Quantas Curtidas um Post Tem
```bash
curl http://localhost:8080/api/posts/145/likes/count
```

**Resposta:**
```json
23
```

### Resultado
Ana encontrou vários tópicos relevantes sobre React e pode participar das discussões!

---

## Caso 7: Sistema de Reputação via Likes

### Cenário
Implementar um sistema simples de reputação baseado nos likes recebidos.

### Fluxo Completo

#### Passo 1: Buscar Todos os Posts de um Usuário (Backend)
```sql
SELECT id FROM posts WHERE author_id = 8;
```

#### Passo 2: Contar Likes de Cada Post
```bash
# Post 86
curl http://localhost:8080/api/posts/86/likes/count
# Retorna: 15

# Post 92
curl http://localhost:8080/api/posts/92/likes/count
# Retorna: 8

# Post 103
curl http://localhost:8080/api/posts/103/likes/count
# Retorna: 23

# Post 117
curl http://localhost:8080/api/posts/117/likes/count
# Retorna: 5
```

#### Passo 3: Calcular Reputação Total
```
Total de Likes = 15 + 8 + 23 + 5 = 51 pontos
```

#### Passo 4: Determinar Nível do Usuário
```
51 pontos = Usuário de Nível "Estabelecido"
- Novato: 0-10 likes
- Contribuidor: 11-30 likes
- Estabelecido: 31-100 likes
- Expert: 101-500 likes
- Mestre: 500+ likes
```

### Resultado
Maria tem reputação de "Usuário Estabelecido" com 51 pontos!

---

## Caso 8: Moderação de Conteúdo

### Cenário
Admin precisa remover conteúdo inapropriado.

### Fluxo Completo

#### Passo 1: Usuário Reporta Post Problemático (manual/email)
```
Post ID: 234
Motivo: Spam
```

#### Passo 2: Admin Revisa o Conteúdo
```bash
curl http://localhost:8080/api/posts/234
```

**Resposta:**
```json
{
  "id": 234,
  "topicId": 55,
  "authorId": 42,
  "content": "COMPRE AGORA!!! CLIQUE AQUI: spamlink.com",
  "createdAt": "2026-02-14T16:00:00Z",
  "updatedAt": null
}
```

#### Passo 3: Admin Deleta o Post
```bash
curl -X DELETE http://localhost:8080/api/posts/234 \
  -H "Authorization: Bearer TOKEN_DO_ADMIN"
```

**Resposta:**
```
204 No Content
```

#### Passo 4: Verificar Outros Posts do Mesmo Autor
```bash
curl http://localhost:8080/api/users/42
```

#### Passo 5: Se Necessário, Admin Deleta Tag Spam
```bash
# Se existir uma tag criada por engano
curl -X DELETE http://localhost:8080/api/admin/tags/99 \
  -H "Authorization: Bearer TOKEN_DO_ADMIN"
```

### Resultado
Conteúdo inapropriado removido e fórum mantém qualidade!

---

## Resumo dos Padrões de Uso

### Fluxo Típico de um Novo Usuário
1. Registrar → Receber token
2. Explorar categorias e tags
3. Ler tópicos existentes
4. Curtir posts úteis
5. Criar novo tópico ou responder
6. Participar de chats

### Fluxo Típico de um Admin
1. Login com credenciais de admin
2. Criar/atualizar categorias
3. Criar/atualizar tags
4. Moderar conteúdo quando necessário
5. Monitorar atividade do fórum

### Boas Práticas
-  Sempre incluir token em requisições autenticadas
-  Validar IDs antes de fazer operações
-  Tratar erros 404 graciosamente
-  Fazer polling de mensagens de chat a cada 2-3 segundos
-  Cachear lista de categorias e tags (mudam pouco)
-  Mostrar timestamp de edição quando `updatedAt !== null`

---

## Recursos Relacionados

- [Documentação Completa da API](API.md)
- [Guia de Início Rápido](GUIA_RAPIDO.md)
- [Índice da Documentação](README.md)

---

**Última Atualização**: 14 de fevereiro de 2026
