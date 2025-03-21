# CSI5347 Distributed Systems Course Project

This project is a distributed systems implementation for the CSI 5347 course. It consists of multiple microservices that interact through REST APIs and shared configurations. The system includes an **Inventory Service**, a **Loyalty Program Service**, and a **Point of Sale (POS) Service**, all managed via a **Spring Cloud Config Server**. The project is containerized using **Docker** and can be orchestrated through **Docker Compose** for testing and deployment.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Clone the Repository](#clone-the-repository)
- [Project Structure](#project-structure)
- [Compiling](#compiling)
- [Running the Application](#running-the-application)
- [Configuration Management](#configuration-management)
- [Testing](#testing)
- [Planned Features](#planned-features)

---

## Project Overview

This project implements a microservices-based distributed system with the following components:

- **Configuration Server (`configuration-server/`)**

  - A Spring Cloud Config Server that centralizes configuration management.
  - Loads configuration from a separate repository located in `configuration-repository/`.

- **Inventory Service (`inventory-service/`)**

  - Manages vendors, products, and purchase orders.
  - Uses a PostgreSQL database for persistence.

- **Loyalty Program Service (`loyalty-program-service/`)**

  - Manages customer accounts and loyalty rewards.
  - Uses a separate PostgreSQL database for persistence.

- **Point of Sale (POS) Service (`point-of-sale-service/`)**

  - Handles sales transactions, registers, and employees.
  - Uses another PostgreSQL database for persistence.

- **Docker Compose (`docker-compose.yml`)**

  - Orchestrates all microservices, their databases, and environment configurations.

- **Security Service (Future Work) (`csi-5347-course-project-security/`)**

  - A planned service for implementing authentication and authorization using **Keycloak**.

- **Submission Artifacts (`submission-artifacts/`)**
  - Contains documentation and required artifacts for the project submission.

---

## Clone the Repository

Since this repository contains multiple submodules, it should be cloned using the following command:

```bash
git clone --recurse-submodules https://github.com/Matt-Hays/csi-5347-course-project.git
cd csi-5347-course-project
```

If the repository was already cloned without submodules:

```bash
git submodule update --init
```

To pull updates from all submodules:

```bash
git submodule update --recursive --remote
```

---

## Project Structure

```
./csi-5347-course-project
│── configuration-server/        # Spring Cloud Config Server
│── configuration-repository/    # External configuration files
│── inventory-service/           # Inventory management microservice
│── loyalty-program-service/     # Loyalty program microservice
│── point-of-sale-service/       # POS system microservice
│── csi-5347-course-project-security/ # Planned security service (Keycloak)
│── submission-artifacts/        # Documentation and submission files
│── docker-compose.yml           # Docker Compose setup for running all services
```

---

## Compiling

Each microservice can be compiled separately, or all can be built at once.

To build all services at once:

```bash
mvn clean package spring-boot:build-image -DskipTests
```

---

## Running the Application

The services are designed to be run as Docker containers. Use the following command to start all services together:

```bash
CONFIG_PORT=8071 INVENTORY_DB_PORT=5432 LOYALTY_DB_PORT=5433 POS_DB_PORT=5434 \
INVENTORY_SERVICE_PORT=8080 INVENTORY_SERVICE_PROFILE=dev LOYALTY_SERVICE_PORT=8081 \
LOYALTY_SERVICE_PROFILE=dev POS_SERVICE_PORT=8082 POS_SERVICE_PROFILE=dev \
GATEWAY_PORT=8072 \
docker compose up -d
```

This will:

- Start the **Configuration Server** at port `8071`.
- Start **PostgreSQL instances** for each microservice (`5432`, `5433`, `5434`).
- Start the **Inventory Service** (`8080`), **Loyalty Program Service** (`8081`), and **POS Service** (`8082`).

To stop all running containers:

```bash
docker compose down
```

To check logs for a specific service:

```bash
docker logs -f <container_name>
```

---

## Configuration Management

The microservices retrieve their configurations from the **Configuration Server** (`configuration-server/`), which in turn loads properties from the **Configuration Git Repository** (`https://github.com/Matt-Hays/configuration-repository.git`).

To access the configuration properties for a service, make a GET request to:

```
http://localhost:8071/{application}/{profile}
```

For example, to fetch configurations for the **Inventory Service** in the `dev` profile:

```
http://localhost:8071/inventory-service/dev
```

---

## Testing

Unit and integration tests can be executed with:

```bash
mvn test
```

For individual services, navigate to the service directory and run:

```bash
cd inventory-service && mvn test
```

---

## Planned Features

- **Security Implementation**

  - The `csi-5347-course-project-security/` module will integrate **Keycloak** for authentication and authorization.

- **Enhanced Service Discovery and Load Balancing**

  - Future work may introduce **Spring Cloud Eureka** for service discovery.

- **Logging and Monitoring**
  - Plans to integrate centralized logging using **ELK Stack** (Elasticsearch, Logstash, Kibana) or **Prometheus/Grafana**.
