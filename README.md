# Smart Parking DSA — Backend

Sistema de Gerenciamento de Estacionamento Inteligente desenvolvido como
trabalho acadêmico para a disciplina de Estruturas de Dados. As estruturas
clássicas são implementadas do zero em Java.

## Stack
- Java 17 + Spring Boot 3.2 (Web)
- Maven
- Lombok
- API REST (JSON) — sem banco de dados, estado em memória
- CORS aberto para `http://localhost:3000` (frontend Next.js)

## Estruturas de dados

### Árvore Binária de Busca (BST)
Organiza as vagas por número. Busca, inserção e remoção em **O(log n)** no
caso médio. A próxima vaga livre é localizada por traversal in-order.

### Min-Heap (Fila de prioridade)
Gerencia a fila quando o estacionamento lota. Prioridades:

| Prioridade | Tipo     |
|-----------:|----------|
| 1          | PCD      |
| 2          | Idoso    |
| 3          | Gestante |
| 4          | Comum    |

Desempate por horário de chegada (FIFO dentro do mesmo nível).

## Endpoints

| Método | Rota                              | Descrição                                |
|-------:|-----------------------------------|------------------------------------------|
| GET    | `/api/estacionamento/vagas`       | Lista todas as vagas (in-order)          |
| GET    | `/api/estacionamento/fila`        | Fila de espera com posição e prioridade  |
| GET    | `/api/estacionamento/stats`       | Estatísticas agregadas                   |
| POST   | `/api/estacionamento/entrada`     | Body: `{ "placa", "prioridade" }`        |
| POST   | `/api/estacionamento/saida`       | Body: `{ "placa" }`                      |
| POST   | `/api/estacionamento/reset`       | Reinicia o estado (demonstração)         |

Exemplo de resposta de `/entrada`:
```json
{
  "sucesso": true,
  "tipo": "ESTACIONADO",
  "mensagem": "Veículo ABC1A23 estacionado na vaga 1.",
  "placa": "ABC1A23",
  "vagaNumero": 1,
  "prioridade": 1,
  "prioridadeLabel": "PCD"
}
```

Quando o estacionamento está lotado, `tipo` vira `FILA` e `vagaNumero` é
`null`. Em erro (placa duplicada, prioridade inválida), `sucesso: false` e
`tipo: ERRO`.

## Como executar

```bash
mvn clean install
mvn spring-boot:run
```

Servidor sobe em `http://localhost:8080`. Em seguida suba o frontend Next.js
em `../Estacionamento_frontend`.

## Estrutura
```
src/main/java/com/dsa/estacionamento/
├── EstacionamentoApplication.java
├── DataSeeder.java              # Seed inicial: 10 vagas
├── config/WebConfig.java        # CORS
├── controller/                  # Endpoints REST
├── service/                     # Regras de negócio
├── dsa/bst/                     # Implementação BST
├── dsa/heap/                    # Implementação Min-Heap
├── dto/                         # Respostas estruturadas
└── model/                       # Vaga, Veiculo
```
