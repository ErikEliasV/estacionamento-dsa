# Smart Parking DSA 🚗🅿️

Este projeto é um **Sistema de Gerenciamento de Estacionamento Inteligente** desenvolvido como trabalho acadêmico para a disciplina de Estruturas de Dados e Análise de Algoritmos. O objetivo é utilizar estruturas de dados clássicas implementadas do zero em Java.

## Tecnologias e Stack
- Java 17
- Spring Boot (Web)
- Maven
- Lombok (Redução de boilerplate)
- API REST (Retornos JSON)
- **Sem Banco de Dados**: Todo o gerenciamento de estados acontece em memória.

## Algoritmos Utilizados
Neste projeto, NÃO utilizamos implementações nativas (como `TreeMap` ou `PriorityQueue`). Construímos as seguintes estruturas a partir da base:

### 1. Árvore Binária de Busca (BST)
**Objetivo:** Organizar e localizar vagas livres e ocupadas de forma eficiente. A árvore provê complexidade de busca e inserção de **O(log n)** no caso médio.
- **Nós:** Cada nó representa uma vaga, identificado por um código numérico único.
- **Métodos principais:** `insert()`, `search()`, `delete()` e `printTree()`.

### 2. Fila de Prioridade (Min-Heap)
**Objetivo:** Gerenciar a fila de espera do estacionamento quando todas as vagas estão ocupadas.
- **Prioridades:** 
  1. PCD (Maior prioridade - 1)
  2. Idoso (2)
  3. Gestante (3)
  4. Comum (Menor prioridade - 4)
- **Desempate:** Caso duas pessoas tenham a mesma prioridade, o desempate é feito pelo horário de chegada (timestamp).
- **Métodos principais:** `insert()`, `extractMin()`, `heapifyUp()`, `heapifyDown()` e `printHeap()`.

## Como Executar

1. Clone o repositório:
```bash
git clone <url-do-repo>
cd estacionamento-dsa
```

2. Compile o projeto com Maven:
```bash
mvn clean install
```

3. Inicie o servidor Spring Boot:
```bash
mvn spring-boot:run
```

O projeto rodará em `http://localhost:8080`.

## Git Flow
O repositório está organizado seguindo a padronização do Git Flow e as mensagens seguem o padrão *Conventional Commits*:
- `main`: Código estável, funcional e pronto para apresentação.
- `develop`: Integração das novas features.
- `feature/*`: Novas funcionalidades sendo construídas (ex: `feature/bst-vagas`).
- `hotfix/*`: Correções de bugs.
