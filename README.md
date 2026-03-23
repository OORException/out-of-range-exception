# Out-of-Range-Exception

> Sistema de Fórum Web com autenticação JWT - Programação Web - 5º Período IFF

Sistema completo de fórum desenvolvido com Spring Boot, oferecendo autenticação JWT, controle de acesso baseado em roles (USER/ADMIN), categorias, tópicos, posts, likes e chats em grupo.

## Quick Start

### 1. Configurar Ambiente

Copie `.env.example` para `.env`:

```bash
cp .env.example .env
```

O arquivo contém configurações do banco de dados e JWT (não commitar).

### 2. Executar

```bash
./mvnw spring-boot:run
```

Aplicação disponível em: **http://localhost:8080**

### 3. Primeiro Acesso

**Registrar usuário:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","email":"admin@forum.com","password":"admin123","fullName":"Admin"}'
```

**Use o token retornado** em todas as requisições:
```
Authorization: Bearer <token>
```

## Funcionalidades

- **Autenticação JWT** com BCrypt
- **RBAC**: Níveis USER e ADMIN
- **Fórum**: Categorias, tópicos, posts
- **Interação**: Likes, chats em grupo
- **API REST**: 33 endpoints

## Tecnologias

- Spring Boot 4.0.2
- Spring Security + JWT (JJWT 0.12.5)
- Spring Data JPA + H2
- BCrypt, Lombok, Bean Validation

## Documentação

**Consulte a pasta [docs/](docs/)** para documentação completa:

- **[API.md](docs/markdown/API.md)** - Todos os 33 endpoints REST
- **[GUIA_RAPIDO.md](docs/markdown/GUIA_RAPIDO.md)** - Setup em 5 minutos
- **[CASOS_DE_USO.md](docs/markdown/CASOS_DE_USO.md)** - Exemplos práticos
- **[ESTRUTURA_DE_DADOS.md](docs/markdown/ESTRUTURA_DE_DADOS.md)** - Entidades e relacionamentos
- **[REQUISITOS.md](docs/markdown/REQUISITOS.md)** - Especificação funcional

## Endpoints Principais

- **Auth (público)**: `/api/v1/auth/register`, `/api/v1/auth/login`
- **Fórum**: `/api/v1/topics`, `/api/v1/posts`, `/api/v1/likes`
- **Chat**: `/api/v1/chats/*`
- **Admin (ADMIN only)**: `/api/v1/admin/categories`, `/api/v1/admin/tags`

**Total: 33 endpoints** - Veja [API.md](docs/markdown/API.md) para detalhes completos.

## Troubleshooting

**401 Unauthorized**: Token ausente/inválido → Faça login novamente  
**403 Forbidden**: Sem permissão → Verifique role (USER vs ADMIN)  
**Promover para ADMIN**: H2 Console → `UPDATE USERS SET LEVEL = 'ADMIN' WHERE ID = 1;`

Mais detalhes em [docs/markdown/GUIA_RAPIDO.md](docs/markdown/GUIA_RAPIDO.md)

---

## Licença

[MIT](LICENSE)
