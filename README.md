# Dog-Vision

API para a equipe de cao guia do IF Urutai.

O projeto e organizado em microservicos Java/Spring Boot, com descoberta de servicos via Eureka, entrada HTTP pelo Cloud Gateway, bancos PostgreSQL separados por dominio e auditoria de requisicoes via RabbitMQ.

## Servicos

- `EurekaServer`: service discovery dos microservicos.
- `CloudGateway`: porta de entrada da API, roteia as chamadas para os servicos internos.
- `user`: autenticacao e gerenciamento de usuarios/funcionarios.
- `DogManagement`: gerenciamento dos caes.
- `DogHealth`: consultas, partos e pesagens dos caes.
- `audit`: consumo e persistencia dos eventos de auditoria enviados pelo gateway.
- `rabbitmq`: broker usado no fluxo de auditoria.
- Bancos PostgreSQL: um banco/container para cada dominio principal.

## Pre-requisitos

Para rodar o projeto da forma recomendada, instale:

- Docker Desktop ou Docker Engine com Docker Compose.
- Git.

Voce nao precisa instalar Maven localmente para rodar com Docker. Os builds usam as imagens Maven definidas nos Dockerfiles. Para compilar um modulo fora do Docker, use o wrapper do proprio modulo, por exemplo `.\mvnw.cmd -DskipTests compile` no Windows.

## Configuracao do ambiente

Crie um arquivo `.env` na raiz do projeto, ao lado do `docker-compose.yml`.

Importante: o `.env` nao deve ser commitado. Ele ja esta no `.gitignore`.

Exemplo para desenvolvimento local:

```env
# JWT
JWT_SECRET=MINHA-CHAVE-SUPER-SECRETA

# DB DOG MANAGEMENT
BD_DOG_MANAGEMENT_USER=postgres
BD_DOG_MANAGEMENT_PASSWORD=password
BD_DOG_MANAGEMENT_HOST=ms_postgres_management
BD_DOG_MANAGEMENT_NAME=dog_management_db
BD_DOG_MANAGEMENT_PORT=5433

# DB USER MANAGEMENT
BD_USER_MANAGEMENT_USER=postgres
BD_USER_MANAGEMENT_PASSWORD=password
BD_USER_MANAGEMENT_HOST=ms_postgres_user_management
BD_USER_MANAGEMENT_NAME=user_management_db
BD_USER_MANAGEMENT_PORT=5434

# DB DOG HEALTH
BD_DOG_HEALTH_USER=postgres
BD_DOG_HEALTH_PASSWORD=password
BD_DOG_HEALTH_HOST=ms_postgres_health
BD_DOG_HEALTH_NAME=dog_health_db
BD_DOG_HEALTH_PORT=5435

# DB AUDITORY
BD_AUDITORY_USER=postgres
BD_AUDITORY_PASSWORD=password
BD_AUDITORY_HOST=ms_postgres_auditory
BD_AUDITORY_NAME=auditory_db
BD_AUDITORY_PORT=5436

# RABBITMQ
RABBIT_USER=guest
RABBIT_PASSWORD=guest
RABBIT_AMQP_PORT=5672
RABBITMQ_MANAGEMENT_PORT=15672

# Portas dos servicos
SERVICE_PORT_DOG_MANAGEMENT=3000
SERVICE_PORT_USER_MANAGEMENT=3001
EUREKA_SERVER_PORT=3002
CLOUD_GATEWAY_SERVER_PORT=3003
PORT_DOG_HEALTH=3004
AUDITORY_PORT=3005
```

Use valores fortes para `JWT_SECRET` e senhas quando sair do ambiente local.

## Como rodar com Docker

Na raiz do projeto:

```powershell
docker compose up -d --build
```

Para subir apenas um servico e suas dependencias:

```powershell
docker compose up -d --build ms_dog_management
docker compose up -d --build ms_user_management
docker compose up -d --build ms_dog_health
```

Para ver os containers:

```powershell
docker compose ps
```

Para acompanhar logs:

```powershell
docker compose logs -f cloud-gateway
docker compose logs -f ms_user_management
docker compose logs -f ms_dog_management
docker compose logs -f ms_dog_health
docker compose logs -f auditory
```

Para parar tudo:

```powershell
docker compose down
```

Para parar e remover tambem os volumes dos bancos:

```powershell
docker compose down -v
```

Use `down -v` com cuidado, porque apaga os dados persistidos nos volumes PostgreSQL.

## URLs uteis

- Cloud Gateway: `http://localhost:3003`
- Eureka Server: `http://localhost:3002`
- RabbitMQ Management: `http://localhost:15672`
- User Management: `http://localhost:3001`
- Dog Management: `http://localhost:3000`
- Dog Health: `http://localhost:3004`
- Auditory: `http://localhost:3005`

Swagger:

- User Management: `http://localhost:3001/swagger-ui/index.html`
- Dog Management: `http://localhost:3000/swagger-ui/index.html`
- Dog Health: `http://localhost:3004/swagger-ui/index.html`

## Fluxo basico de uso

1. Suba a stack com Docker Compose.
2. Faca login pelo endpoint `POST /api/v1/auth/login`, usando o gateway em `http://localhost:3003`.
3. Use o token JWT retornado no header:

```http
Authorization: Bearer <seu-token>
```

4. Chame os endpoints pelos caminhos do gateway:

```text
http://localhost:3003/api/v1/dogs/**
http://localhost:3003/api/v1/doghealth/**
http://localhost:3003/api/v1/employees/**
```

A pasta `requests/` contem exemplos `.http` para testar os endpoints pela IDE.

## Rodando modulos fora do Docker

O perfil local usa os bancos publicados no host pelas portas do `.env`. Primeiro suba as dependencias com Docker:

```powershell
docker compose up -d eureka-server dog_management_db user_management_db dog_health_db auditory_bd rabbitmq
```

Depois rode o modulo desejado com o perfil `local` pela IDE ou pelo Maven Wrapper, garantindo que as variaveis do `.env` estejam disponiveis no ambiente.

Exemplo:

```powershell
cd DogHealth
.\mvnw.cmd -Dspring-boot.run.profiles=local spring-boot:run
```

## Problemas comuns

Se aparecer `Connection refused`, normalmente o servico de destino nao subiu, esta reiniciando, ou a porta/host no `.env` esta incorreta.

Se aparecer `UnknownHostException` dentro de containers Docker, confira se o host usado no profile `docker` aponta para o nome do servico/container na rede do Compose.

Se o Swagger retornar `401` ou `403`, confira se `/v3/api-docs/**`, `/swagger-ui/**` e `/swagger-ui.html` estao liberados na configuracao de seguranca do modulo.

Se as portas ja estiverem em uso, altere as portas publicadas no `.env` e suba novamente a stack.
