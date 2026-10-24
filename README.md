# MoneyFlow

Aplicação de controle financeiro pessoal desenvolvida como projeto de portfólio, com foco em Java, Spring Boot e práticas de desenvolvimento full stack.

## Funcionalidades atuais

- Cadastro e consulta de categorias de receita e despesa.
- Meta percentual opcional por categoria.
- Registro de receitas e despesas associadas a categorias compatíveis.
- Alertas mensais quando o gasto de uma categoria ultrapassa sua meta em relação à renda do período.
- Migrations versionadas com Flyway.
- Testes de integração com PostgreSQL e execução automatizada no GitHub Actions.

## Tecnologias

- Java 21 e Spring Boot 4
- PostgreSQL 16
- Spring Data JPA e Flyway
- Maven Wrapper
- JUnit e Mockito
- GitHub Actions

## Executar localmente

Crie um banco PostgreSQL e forneça as variáveis de ambiente abaixo:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/moneyflow
export DB_USERNAME=moneyflow_user
export DB_PASSWORD=sua_senha
```

Depois, execute o backend:

```bash
cd backend
./mvnw spring-boot:run
```

Para executar os testes:

```bash
cd backend
./mvnw test
```

As migrations são aplicadas automaticamente na inicialização.

## API disponível

| Endpoint | Descrição |
| --- | --- |
| `GET /api/health` | Verifica o estado da aplicação. |
| `GET /api/categories` | Lista categorias. |
| `POST /api/categories` | Cria uma categoria. |
| `GET /api/budget-alerts?month=YYYY-MM` | Consulta alertas mensais de metas. |

## Próximos passos

- Autenticação e autorização com Spring Security e JWT.
- API de transações financeiras.
- Frontend com React e TypeScript.
- Docker e deploy.
