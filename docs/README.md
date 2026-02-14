# Documentação do Sistema de Fórum Web

Esta pasta contém toda a documentação técnica do projeto Out-of-Range-Exception.

## Estrutura da Documentação

### Markdown (docs/markdown/)

Documentação técnica em formato Markdown:

- **[API.md](markdown/API.md)** - Documentação completa da API REST com todos os 32 endpoints, exemplos de requisições, respostas, validações e códigos de status

- **[GUIA_RAPIDO.md](markdown/GUIA_RAPIDO.md)** - Guia de início rápido para configurar e executar a aplicação em menos de 5 minutos, incluindo criação de dados iniciais e comandos úteis

- **[CASOS_DE_USO.md](markdown/CASOS_DE_USO.md)** - 8 cenários práticos de uso do sistema com exemplos completos de requisições e respostas

- **[ESTRUTURA_DE_DADOS.md](markdown/ESTRUTURA_DE_DADOS.md)** - Detalhamento completo de todas as entidades do banco dedados, relacionamentos, validações e regras de negócio

- **[REQUISITOS.md](markdown/REQUISITOS.md)** - Especificação completa dos requisitos funcionais e não-funcionais do sistema

### Diagramas (docs/diagrams/)

Diagramas  visuais da arquitetura:

- **DIAGRAMA_DE_CLASSES.drawio** - Arquivo editável do diagrama de classes (Draw.io)
- **DIAGRAMA_DE_CLASSES.png** - Imagem do diagrama de classes mostrando todas as entidades e seus relacionamentos

### Wireframes (docs/wireframes/)

Layouts das interfaces:

- **WIREFRAME-PAGINA_HOME.png** - Layout da página inicial do fórum
- **WIREFRAME-PAGINA_DO_TOPICO.png** - Layout da página de visualização de tópicos e respostas

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

### Para Desenvolvedores Frontend

1. Leia o [Guia de Início Rápido](markdown/GUIA_RAPIDO.md) para configurar o backend
2. Estude a [Documentação da API](markdown/API.md) para entender os endpoints disponíveis
3. Use os wireframes como referência visual
4. Consulte os [Casos de Uso](markdown/CASOS_DE_USO.md) para entender fluxos completos

### Para Testadores

1. Configure o ambiente com o [Guia de Início Rápido](markdown/GUIA_RAPIDO.md)
2. Use os [Casos de Uso](markdown/CASOS_DE_USO.md) como base para casos de teste
3. Valide os endpoints conforme a [Documentação da API](markdown/API.md)
4. Verifique se os [Requisitos](markdown/REQUISITOS.md) estão sendo atendidos

## Visão Geral do Sistema

### Funcionalidades Principais

- **Autenticação**: Sistema completo de registro e login de usuários
- **Fórum**: Categorias, tópicos, posts e respostas organizados
- **Tags**: Sistema de etiquetas para classificação de conteúdo
- **Likes**: Curtidas em posts para engajamento
- **Chats**: Salas de chat vinculadas a tópicos específicos
- **Administração**: Gerenciamento de categorias e tags

### Tecnologias

- Spring Boot 4.0.2
- Spring Data JPA
- Spring Security
- Hibernate ORM
- H2 Database (desenvolvimento)
- Lombok
- Bean Validation

### Arquitetura

O projeto segue uma arquitetura em camadas:

```
Controller (REST API) → Service (Lógica de negócio) → Repository (Acesso a dados) → Database
```

## Principais Entidades

| Entidade | Descrição |
|----------|-----------|
| **User** | Usuário do sistema |
| **Category** | Categoria que agrupa tópicos |
| **Tag** | Etiqueta para classificar tópicos |
| **Topic** | Tópico de discussão |
| **Post** | Resposta em um tópico |
| **Like** | Curtida em um post |
| **Chat** | Sala de chat |
| **ChatMessage** | Mensagem de chat |
| **ChatParticipation** | Participação de usuário em chat |

## Totalização da API

- **32 endpoints REST** documentados
- **5 módulos principais**: Autenticação, Fórum, Likes, Chats e Administração
- **10 entidades** no banco de dados
- **40+ consultas customizadas** nos repositories
- **11 DTOs** para requisições e respostas

## Documentos por Tamanho

| Documento | Páginas | Conteúdo |
|-----------|---------|----------|
| API.md | ~40 | Documentação completa de endpoints |
| ESTRUTURA_DE_DADOS.md | ~35 | Detalhamento de entidades |
| CASOS_DE_USO.md | ~20 | Exemplos práticos |
| GUIA_RAPIDO.md | ~15 | Quick start guide |
| REQUISITOS.md | ~10 | Especificação de requisitos |

## Informações Acadêmicas

- **Instituição**: Instituto Federal Fluminense (IFF)
- **Curso**: Ciência da Computação
- **Disciplina**: Programação Web
- **Período**: 5º Período
- **Ano**: 2026

## Manutenção da Documentação

### Quando Atualizar

- **API.md**: Ao adicionar, modificar ou remover endpoints
- **ESTRUTURA_DE_DADOS.md**: Ao modificar entidades, adicionar campos ou alterar relacionamentos
- **CASOS_DE_USO.md**: Ao implementar novos fluxos importantes
- **GUIA_RAPIDO.md**: Ao mudar processo de configuração ou instalação
- **REQUISITOS.md**: Ao adicionar novos requisitos funcionais ou não-funcionais

### Padrões de Documentação

- Use Markdown puro sem HTML
- Inclua exemplos práticos sempre que possível
- Mantenha código formatado com syntax highlighting
- Use tabelas para informações estruturadas
- Adicione links internos entre documentos relacionados
- Mantenha consistência na formatação

## Licença

[MIT](../LICENSE)

---

**Última Atualização**: 14 de fevereiro de 2026  
**Versão da Documentação**: 1.0.0  
**Versão do Sistema**: MVP
