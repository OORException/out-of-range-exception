1. Visão Geral do Sistema

O Sistema de Fórum com Chat Online Integrado visa unir o modelo tradicional de fóruns de discussão (tópicos e postagens) com a dinâmica dos chats em tempo real.
O objetivo é criar um ambiente colaborativo onde os usuários possam trocar ideias, tirar dúvidas e debater assuntos, promovendo tanto conversas assíncronas (via postagens) quanto síncronas (via chat).

2. Requisitos Funcionais

| Código   | Requisito Funcional                | Descrição                                                                                                        |
| -------- | ---------------------------------- | ---------------------------------------------------------------------------------------------------------------- |
| **RF01** | Cadastro de Usuário                | O sistema deve permitir que novos usuários se cadastrem informando nome, e-mail e senha.                         |
| **RF02** | Autenticação                       | O sistema deve autenticar o usuário com base no e-mail e senha cadastrados.                                      |
| **RF03** | Criação de Tópico                  | O usuário autenticado deve poder criar novos tópicos em categorias específicas.                                  |
| **RF04** | Listagem de Tópicos                | O sistema deve listar os tópicos existentes, exibindo título, categoria, número de postagens e última atividade. |
| **RF05** | Postagem em Tópico                 | O usuário deve poder responder a um tópico com novas postagens.                                                  |
| **RF06** | Edição e Exclusão de Postagem      | O autor deve poder editar ou excluir suas próprias postagens.                                                    |
| **RF07** | Curtir Postagem                    | O usuário deve poder curtir postagens de outros participantes.                                                   |
| **RF08** | Criação de Chat                    | O usuário deve poder iniciar um chat associado a um tópico existente.                                            |
| **RF09** | Participação em Chat               | O usuário deve poder entrar em um chat existente e enviar mensagens em tempo real.                               |
| **RF10** | Edição de Mensagem no Chat         | O usuário deve poder editar mensagens enviadas.                                                                  |
| **RF11** | Marcar Mensagem como Lida          | O sistema deve permitir marcar mensagens como lidas.                                                             |
| **RF12** | Exibição de Participantes do Chat  | O sistema deve mostrar o número de participantes ativos em cada chat.                                            |
| **RF13** | Gerenciamento de Categorias e Tags | O administrador deve poder criar, editar e excluir categorias e tags.                                            |
| **RF14** | Visualização de Estatísticas       | O sistema deve exibir número de visualizações, curtidas e últimas atividades dos tópicos.                        |
| **RF15** | Interface Responsiva               | A interface deve se adaptar automaticamente a diferentes tamanhos de tela (desktop, tablet e celular).           |
