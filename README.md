# Projeto API Spring Boot - Trachours

## Descrição

Esta é uma API desenvolvida com Spring Boot que permite o gerenciamento de Usuários, Projetos, Tarefas e Lançamento de Horas. 
Ela fornece endpoints para:

- CRUD de Usuários
- CRUD de Proejtos
- CRUD de Tarefas
- CRUD de Lançamento de Horas
- Relatórios avançados entre as entidades.

--

## Pré-requisitos

- Java 21+
- Maven 3.9.9+
- Banco de Dados MySQL
- Git

### Tecnologias Utilizadas

- Spring Boot
- Spring Data JPA
- Spring Security
- MySQL
- Lombok
- Java JWT
- Springdocs OpenAPI
---

## Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/seuusuario/seurepositorio.git
   ```
2. Acesse o diretório do projeto:
   ```bash
   cd seurepositorio
   ```
3. Instale as dependências via Maven:
   ```bash
   mvn clean install
   ```

--

## Configuração

### Configurar o banco de dados e outras variáveis de ambiente:
Ajuste o seu arquivo ```application.properties``` conforme as configurações do banco de dados:

```properties
# Configuração do Banco de Dados
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/sistema_gerenciamento?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=suasenha
```

Certifique-se de definir as variáveis de ambiente para acesso ao banco de dados e outras configurações sensíveis.

--

## Execução

### Rodando a API

Execute o comando abaixo para iniciar a aplicação:

```bash
mvn spring-boot:run
```

A aplicação estará disponível em:

```
http://localhost:8080
```

Usuário padrão com perfil Administrador:

- E-mail: admin@admin.com
- Senha: admin

Usuário padrão com perfil Colaborador:

- E-mail: user@user.com
- Senha: user

--

## Documentação da API

A documentação da API está disponível em:

[Swagger Docs UI](http://localhost:8080/swagger-ui.html) ou [Postman Docs](https://documenter.getpostman.com/view/26615270/2sAYk7RirS)

### Exemplos de Endpoints

#### Autenticação

- **POST /auth/login** - Realiza o login do usuário
  - Corpo da Requisição (JSON):
  
    ```json
    {
      "username": "user@example.com",
      "password": "password123"
    }
    ```
    
  - Resposta (200):
  
    ```json
    {
      "token": "jwt-token"
    }
    ```

#### Usuários

- **GET /users** - Lista todos os usuários
- **GET /users/{id}** - Retorna um usuário específico
- **POST /users** - Cria um novo usuário
  - Corpo da Requisição (JSON):
  
    ```json
    {
      "name": "John Doe",
      "email": "john@example.com",
      "password": "password123"
    }
    ```
    
- **PUT /users/{id}** - Atualiza um usuário existente
  - Corpo da Requisição (JSON):
  
    ```json
    {
      "name": "John Doe",
      "email": "john.new@example.com"
    }
    ```
    
- **DELETE /users/{id}** - Remove um usuário (soft delete)

#### Projetos

- **GET /projects** - Retorna todos os projetos
- **GET /projects/{id}** - Retorna um projeto específico
- **POST /projects** - Cria um novo projeto
  - Corpo da Requisição (JSON):
  
    ```json
    {
      "name": "Teste",
      "startDate": "13/01/2025",
      "endDate": "14/03/2025",
      "responsibleUser": 2,
      "priority": "LOW"
    }
    ```
- **PUT /projects/{id}** - Atualiza um projeto existente
  - Corpo da Requisição (JSON):
  
    ```json
    {
      "name": "Projeto Change Name",
      "startDate": "27/02/2025",
      "endDate": "01/03/2025",
      "responsibleUser": 3,
      "status": "DONE",
      "priority": "LOW"
    }
    ```
- **DELETE /projects/{id}** - Exclui um projeto

#### Lançamentos de Horas

- **GET /releases** - Lista todos os lançamentos de horas
- **POST /releases** - Cria um novo lançamento de horas
  - Corpo da Requisição (JSON):
  
    ```json
    {
      "description": "Desenvolvimento de frontend",
      "startTime": "2025-03-13T08:00:00",
      "endTime": "2025-03-13T12:00:00"
      "userResponsibleId": 2,
      "taskId": 1,
    }
    ```
- **PUT /releases/{id}** - Atualiza um lançamento existente
  - Corpo da Requisição (JSON):
  
    ```json
    {
      "description": "Desenvolvimento de frontend",
      "startTime": "2025-03-13T08:00:00",
      "endTime": "2025-03-13T12:00:00"
      "userResponsibleId": 2,
      "taskId": 1,
    }
    ```
- **DELETE /releases/{id}** - Exclui um lançamento de horas (soft delete)

---

## Aproveite a aplicação

