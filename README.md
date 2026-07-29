# Forum API

API REST para um fórum de discussões, desenvolvida com Spring Boot. Suporta criação de fóruns, publicações, comentários aninhados e sistema de votação, com controle de acesso baseado em papéis (RBAC).

## Funcionalidades

- **Fóruns** — criação, edição de nome/descrição, exclusão, gestão de administradores e banimento de usuários dentro do fórum.
- **Publicações (posts)** — criação, edição de título/conteúdo, aprovação, bloqueio/desbloqueio e exclusão.
- **Comentários aninhados** — comentários em posts e respostas a comentários (replies), com edição e exclusão.
- **Sistema de votação** — upvote/downvote tanto em posts quanto em comentários.
- **Gestão de usuários** — cadastro, alteração de nome/e-mail/senha, banimento e desbanimento por administradores.
- **RBAC** — papéis `USER`, `MODERATOR` e `ADMIN`, com regras de negócio validando permissões antes de ações sensíveis (ex: apenas `ADMIN` pode banir/desbanir usuários).

## Stack técnica

- **Java** / **Spring Boot**
- **Spring Data JPA** / **Hibernate**
- **Bean Validation**
- **Maven**

## Arquitetura

```
resources/     → controllers REST (entrada HTTP)
services/      → regras de negócio (Forum, Post, Comment, User, votação)
repositories/  → acesso a dados (Spring Data JPA)
entities/      → modelos JPA (User, Forum, Post, Comment, Vote, PostVote, CommentVote)
dtos/          → objetos de transferência de dados (entrada/saída da API)
exceptions/    → exceções de domínio e handler global
```

## Endpoints principais

| Método | Rota | Descrição |
|---|---|---|
| POST | `/users/register` | Cadastra um novo usuário |
| PUT | `/users/{id}/name` \| `/email` \| `/password` | Atualiza dados do usuário |
| PUT | `/users/{id}/ban` \| `/unban` | Bane/desbane um usuário (`ADMIN`) |
| POST | `/forums` | Cria um novo fórum |
| PUT | `/forums/{id}/name` \| `/description` | Edita um fórum |
| POST | `/forums/{id}/admins/{adminId}` | Adiciona administrador ao fórum |
| POST | `/forums/{id}/ban/{banId}` | Bane usuário de um fórum |
| POST | `/posts` | Cria uma nova publicação |
| PUT | `/posts/{id}/approve` \| `/lock` \| `/unlock` | Modera uma publicação |
| POST | `/posts/{id}/vote` | Vota em uma publicação |
| POST | `/comments` | Adiciona comentário a um post |
| POST | `/comments/{id}/replies` | Responde a um comentário |
| POST | `/comments/{id}/vote` | Vota em um comentário |

## Autenticação — em desenvolvimento

Atualmente, a identificação do usuário autenticado é feita via header `User-Id`, enviado diretamente na requisição. A implementação de **autenticação real com Spring Security e JWT** (nos mesmos moldes do [simple-wallet](https://github.com/patrickFernands/simple-wallet)) está em andamento, substituindo o header por um token validado no backend.

## Como rodar

```bash
git clone https://github.com/patrickFernands/forum-api.git
cd forum-api
./mvnw spring-boot:run
```

## Melhorias futuras

- Autenticação e autorização via Spring Security + JWT
- Testes automatizados dos fluxos de moderação e votação
- Paginação nos endpoints de listagem
