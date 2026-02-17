# Documentação do Sistema de Fórum Web

Esta pasta contém toda a documentação técnica do projeto Out-of-Range-Exception.

## Estrutura da Documentação

### Markdown (docs/markdown/)

Documentação técnica em formato Markdown:

- **[API.md](markdown/API.md)** - Documentação completa da API REST com todos os 33 endpoints, exemplos de requisições, respostas, validações e códigos de status

- **[GUIA_RAPIDO.md](markdown/GUIA_RAPIDO.md)** - Guia de início rápido para configurar e executar a aplicação em menos de 5 minutos, incluindo autenticação JWT e criação de dados iniciais

- **[CASOS_DE_USO.md](markdown/CASOS_DE_USO.md)** - 8 cenários práticos de uso do sistema com exemplos completos de requisições e respostas

- **[ESTRUTURA_DE_DADOS.md](markdown/ESTRUTURA_DE_DADOS.md)** - Detalhamento completo de todas as entidades do banco de dados, relacionamentos, validações e regras de negócio

- **[REQUISITOS.md](markdown/REQUISITOS.md)** - Especificação completa dos requisitos funcionais e não-funcionais do sistema

### Diagramas (docs/diagrams/)

Diagramas visuais da arquitetura:

- **DIAGRAMA_DE_CLASSES.drawio** - Arquivo editável do diagrama de classes (Draw.io)
- **DIAGRAMA_DE_CLASSES.png** - Imagem do diagrama de classes mostrando todas as entidades e seus relacionamentos

### Wireframes (docs/wireframes/)

Layouts das interfaces:

- **WIREFRAME-PAGINA_HOME.png** - Layout da página inicial do fórum
- **WIREFRAME-PAGINA_DO_TOPICO.png** - Layout da página de visualização de tópicos e respostas

## Segurança e Autenticação

O sistema implementa autenticação JWT completa:

- **Algoritmo**: HS256 (HMAC with SHA-256)
- **Password Hashing**: BCrypt com salt automático
- **Expiração de Token**: 24 horas (configurável)
- **RBAC**: Role-Based Access Control com níveis USER e ADMIN

### Configuração

As configurações de segurança estão no arquivo `.env`:

```properties
JWT_SECRET=OutOfRangeExceptionSecretKeyForJWT2026MustBeAtLeast256BitsLongForHS256Algorithm
JWT_EXPIRATION=86400000
```

**IMPORTANTE**: O arquivo `.env` não deve ser commitado (já incluído no `.gitignore`).

### Endpoints Públicos

Apenas estes endpoints não requerem autenticação:

- `POST /api/v1/auth/register` - Registro de usuário
- `POST /api/v1/auth/login` - Login de usuário
- `GET /h2-console/**` - Console H2 (desenvolvimento)
- `GET /actuator/health` - Health check

### Autenticação de Requisições

Todas as outras requisições requerem o header:

```
Authorization: Bearer <token>
```

### Controle de Acesso

- **USER**: Pode criar tópicos, posts, curtir, participar de chats
- **ADMIN**: Além das permissões USER, pode gerenciar categorias e tags (endpoints `/api/v1/admin/**`)

### Códigos de Status de Segurança

| Código | Significado | Quando Ocorre |
|--------|-------------|---------------|
| **401 Unauthorized** | Não autenticado | Token ausente, inválido ou expirado |
| **403 Forbidden** | Sem permissão | Usuário sem role necessária (ex: USER tentando acessar `/admin`) |

## Guia de Leitura Recomendado

### Para Iniciantes no Projeto

1. Leia o [README.md](../README.md) principal do projeto
2. Siga o [Guia de Início Rápido](markdown/GUIA_RAPIDO.md)
3. Explore os [Casos de Uso](markdown/CASOS_DE_USO.md)
4. Consulte a [Documentação da API](markdown/API.md) conforme necessário

### Para Desenvolvedores Backend

1. Estude a [Estrutura de Dados](markdown/ESTRUTURA_DE_DADOS.md)
2. Analise o [Diagrama de Classes](diagrams/DIAGRAMA_DE_CLASSES.png)
3. Revise os [Requisitos](markdown/REQUISITOS.md)
4. Consulte a [Documentação da API](markdown/API.md) para implementar novos endpoints
5. **Padrões de Segurança**:
   - Use `SecurityUtils.getCurrentUserId()` para obter usuário autenticado
   - Use `@PreAuthorize("hasRole('ADMIN')")` para endpoints administrativos
   - Nunca retorne `passwordHash` nos DTOs de response

### Para Desenvolvedores Frontend

1. Leia o [Guia de Início Rápido](markdown/GUIA_RAPIDO.md) para configurar o backend
2. Estude a [Documentação da API](markdown/API.md) para entender os endpoints disponíveis
3. **Implemente Autenticação JWT**:
   - Armazene o token do login (localStorage/sessionStorage)
   - Envie em todas as requisições autenticadas: `Authorization: Bearer <token>`
   - Trate erros 401 (redirecionar para login) e 403 (acesso negado)
4. Use os wireframes como referência visual
5. Consulte os [Casos de Uso](markdown/CASOS_DE_USO.md) para entender fluxos completos

### Para Testadores

1. Configure o ambiente com o [Guia de Início Rápido](markdown/GUIA_RAPIDO.md)
2. **Crie Usuários de Teste**:
   - User comum: `POST /api/v1/auth/register`
   - Admin: Registre e altere `USER_LEVEL` para `ADMIN` no H2 Console
3. **Teste Autenticação**:
   - Requisições sem token → 401
   - Token expirado → 401
   - USER acessando `/admin/**` → 403
4. Use os [Casos de Uso](markdown/CASOS_DE_USO.md) como base para casos de teste
5. Valide os endpoints conforme a [Documentação da API](markdown/API.md)
6. Verifique se os [Requisitos](markdown/REQUISITOS.md) estão sendo atendidos

## Visão Geral do Sistema

### Funcionalidades Principais

- **Autenticação JWT**: Sistema completo com registro, login e gestão de tokens
- **Controle de Acesso**: RBAC com níveis USER e ADMIN
- **Fórum**: Categorias, tópicos, posts e respostas organizados
- **Tags**: Sistema de etiquetas para classificação de conteúdo
- **Likes**: Curtidas em posts para engajamento
- **Chats**: Salas de chat vinculadas a tópicos específicos
- **Administração**: Gerenciamento de categorias e tags

### Tecnologias

- Spring Boot 4.0.2
- Spring Data JPA
- Spring Security + JWT (JJWT 0.12.5)
- BCrypt Password Encoder
- Hibernate ORM
- H2 Database (desenvolvimento)
- Lombok
- Bean Validation

### Arquitetura

O projeto segue uma arquitetura em camadas:

```
Controller (REST API) → Service (Lógica de negócio) → Repository (Acesso a dados) → Database
                              ↓
                        SecurityUtils (Contexto de autenticação)
```

## Principais Entidades

| Entidade | Descrição | Atributos Principais |
|----------|-----------|---------------------|
| **User** | Usuário do sistema | username, email, passwordHash, level (USER/ADMIN) |
| **Category** | Categoria que agrupa tópicos | name, description |
| **Tag** | Etiqueta para classificar tópicos | name |
| **Topic** | Tópico de discussão | title, description, category, tags, creator |
| **Post** | Resposta em um tópico | content, topic, author |
| **Like** | Curtida em um post | user, post |
| **Chat** | Sala de chat vinculada a tópico | name, topic, active |
| **ChatMessage** | Mensagem em um chat | content, chat, sender |
| **ChatParticipation** | Participação de usuário em chat | user, chat, active |

## Fluxo de Uso Típico

```
1. Usuário se registra → Senha criptografada (BCrypt) → Recebe token JWT
2. Usuário autentica requisições → Header: Authorization: Bearer <token>
3. Admin cria Categorias e Tags (requer role ADMIN)
4. Usuário cria Tópico em uma Categoria com Tags
5. Sistema cria Post inicial automaticamente
6. Outros usuários criam Posts (respostas)
7. Usuários dão Like nos Posts
8. Sistema cria Chat para o Tópico
9. Usuários entram no Chat e enviam mensagens
```

## Manutenção da Documentação

### Quando Atualizar

- **API.md**: Ao adicionar, modificar ou remover endpoints
- **GUIA_RAPIDO.md**: Ao mudar processo de setup ou configuração inicial
- **CASOS_DE_USO.md**: Ao implementar novos fluxos importantes
- **ESTRUTURA_DE_DADOS.md**: Ao modificar entidades ou relacionamentos
- **REQUISITOS.md**: Ao adicionar ou alterar requisitos funcionais

### Convenções

- Use exemplos reais de requisições e respostas
- Mantenha os códigos de status HTTP atualizados
- Documente validações e regras de negócio
- Inclua mensagens de erro comuns e suas soluções
