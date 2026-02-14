# Out-of-Range-Exception

> Sistema de Fórum Web desenvolvido para a disciplina de Programação Web - 5º Período - IFF

## Sobre o Projeto

**Out-of-Range-Exception** é um sistema completo de fórum de discussão desenvolvido com Spring Boot, oferecendo funcionalidades modernas para comunidades online, incluindo sistema de tópicos, posts, likes, chats em grupo e muito mais.

## Funcionalidades Principais

- Autenticação e Autorização - Sistema completo de registro e login
- Sistema de Fórum - Categorias, tópicos, posts e respostas
- Sistema de Tags - Classificação e organização de conteúdo
- Sistema de Likes - Curtidas em posts
- Chat em Grupo - Salas de chat vinculadas a tópicos
- Administração - Painel para gerenciar categorias e tags
- API REST Completa - 32 endpoints documentados

## Quick Start

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- Porta 8080 disponível

### Executar a Aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

### Primeiros Passos

1. Registre um usuário via `POST /api/auth/register`
2. Crie categorias e tags (como admin)
3. Crie seu primeiro tópico
4. Comece a interagir!

**Para instruções detalhadas**, consulte o [Guia de Início Rápido](docs/markdown/GUIA_RAPIDO.md).

## Documentação Completa

A documentação completa do projeto está organizada na pasta **docs/markdown/**:

- [**API.md**](docs/markdown/API.md) - Documentação completa de todos os 32 endpoints REST
- [**GUIA_RAPIDO.md**](docs/markdown/GUIA_RAPIDO.md) - Configure e rode em menos de 5 minutos
- [**CASOS_DE_USO.md**](docs/markdown/CASOS_DE_USO.md) - 8 exemplos práticos de uso
- [**ESTRUTURA_DE_DADOS.md**](docs/markdown/ESTRUTURA_DE_DADOS.md) - Detalhamento de todas as entidades
- [**REQUISITOS.md**](docs/markdown/REQUISITOS.md) - Especificação de requisitos

Também disponível:

- [**Diagrama de Classes**](docs/diagrams/DIAGRAMA_DE_CLASSES.png) - Arquitetura visual
- [**Wireframes**](docs/wireframes/) - Layouts das telas

## Tecnologias Utilizadas

### Backend
- **Spring Boot 4.0.2** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **Hibernate ORM** - Mapeamento objeto-relacional
- **H2 Database** - Banco de dados em memória (desenvolvimento)
- **Lombok** - Redução de código boilerplate
- **Bean Validation** - Validação de dados

### Estrutura do Projeto
```
src/main/java/br/edu/iff/ccc/webdev/
├── config/          # Configurações (Security, CORS)
├── controller/      # Controllers REST e Views
│   ├── restapi/
│   │   ├── admin/   # Endpoints administrativos
│   │   ├── auth/    # Autenticação
│   │   ├── chat/    # Chats e mensagens
│   │   └── forum/   # Fórum (tópicos, posts, likes)
│   └── view/        # Controllers de view (Thymeleaf)
├── dto/             # Data Transfer Objects
├── exception/       # Tratamento de exceções
├── model/           # Entidades JPA
├── repository/      # Repositories Spring Data
├── security/        # Segurança e autenticação
└── service/         # Lógica de negócio
```

## Principais Endpoints da API

### Entidades Principais

| Entidade | Descrição | Relações |
|----------|-----------|----------|
| **User** | Usuário do sistema | Autor de tópicos, posts e mensagens |
| **Category** | Categoria de tópicos | Agrupa tópicos por assunto |
| **Tag** | Etiqueta/marcador | Classifica tópicos (many-to-many) |
| **Topic** | Tópico de discussão | Pertence a uma categoria, possui posts |
| **Post** | Resposta em um tópico | Pertence a um tópico, possui likes |
| **Like** | Curtida em um post | Relaciona usuário e post |
| **Chat** | Sala de chat | Associada a um tópico |
| **ChatMessage** | Mensagem de chat | Pertence a um chat |
| **ChatParticipation** | Participação em chat | Relaciona usuário e chat |

### Fluxo de Dados

```
1. Usuário se registra → Recebe token JWT
2. Admin cria Categorias e Tags
3. Usuário cria Tópico em uma Categoria com Tags
4. Sistema cria Post inicial automaticamente
5. Outros usuários criam Posts (respostas)
6. Usuários dão Like nos Posts
7. Sistema pode criar Chat para o Tópico
8. Usuários entram no Chat e enviam mensagens
```

---

## Guias de Uso

### Para Desenvolvedores Frontend

1. Leia o [Guia de Início Rápido](docs/markdown/GUIA_RAPIDO.md)
2. Configure a aplicação backend
3. Consulte a [Documentação da API](docs/markdown/API.md)
4. Use os wireframes como referência de design
5. Implemente as chamadas aos endpoints documentados

### Para Desenvolvedores Backend

1. Estude o [Diagrama de Classes](docs/diagrams/DIAGRAMA_DE_CLASSES.png)
2. Analise os [Requisitos](docs/markdown/REQUISITOS.md)
3. Implemente novos recursos seguindo os padrões existentes
4. Teste usando os exemplos da [Documentação da API](docs/markdown/API.md)

### Para Testadores

1. Configure o ambiente com o [Guia Rápido](docs/markdown/GUIA_RAPIDO.md)
2. Importe a collection do Postman (estrutura no guia)
3. Execute os casos de teste baseados nos [Requisitos](docs/markdown/REQUISITOS.md)
4. Valide os códigos de status na [Documentação da API](docs/markdown/API.md)

---

## Endpoints por Módulo

### Autenticação
- POST `/api/auth/register` - Registrar usuário
- POST `/api/auth/login` - Fazer login

### Usuários
- GET `/api/users` - Listar usuários
- GET `/api/users/{id}` - Buscar usuário

### Categorias
- GET `/api/categories` - Listar categorias
- GET `/api/categories/{id}` - Buscar categoria

### Tags
- GET `/api/tags` - Listar tags
- GET `/api/tags/{id}` - Buscar tag

### Tópicos
- POST `/api/topics` - Criar tópico
- GET `/api/topics` - Listar tópicos
- GET `/api/topics/{id}` - Buscar tópico

### Posts
- POST `/api/posts` - Criar post
- GET `/api/posts/{id}` - Buscar post
- GET `/api/posts/topic/{topicId}` - Listar posts do tópico
- PATCH `/api/posts/{id}` - Editar post
- DELETE `/api/posts/{id}` - Deletar post

### Likes
- POST `/api/posts/{postId}/likes` - Curtir post
- DELETE `/api/posts/{postId}/likes` - Descurtir post
- GET `/api/posts/{postId}/likes/count` - Contar likes

### Chats
- POST `/api/chats/topic/{topicId}` - Criar chat
- GET `/api/chats/{id}` - Buscar chat
- GET `/api/chats/{id}/messages` - Listar mensagens
- POST `/api/chats/{id}/messages` - Enviar mensagem
- POST `/api/chats/{id}/participants/join` - Entrar no chat
- POST `/api/chats/{id}/participants/leave` - Sair do chat

### Administração
- POST `/api/admin/categories` - Criar categoria
- PUT `/api/admin/categories/{id}` - Atualizar categoria
- DELETE `/api/admin/categories/{id}` - Deletar categoria
- POST `/api/admin/tags` - Criar tag
- PUT `/api/admin/tags/{id}` - Atualizar tag
- DELETE `/api/admin/tags/{id}` - Deletar tag

**Total: 32 endpoints REST**

---

## Licença

[MIT](LICENSE)

## Contribuindo

1. Leia a [Documentação Completa](docs/)
2. Faça fork do projeto
3. Crie uma branch para sua feature (`git checkout -b feature/NovaFuncionalidade`)
4. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
5. Push para a branch (`git push origin feature/NovaFuncionalidade`)
6. Abra um Pull Request

## Suporte

Para dúvidas ou problemas:
- Consulte a [Documentação Completa](docs/README.md)
- Veja os [Casos de Uso](docs/markdown/CASOS_DE_USO.md)
- Leia o [Guia de Início Rápido](docs/markdown/GUIA_RAPIDO.md)
