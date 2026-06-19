
# Marro News - Backend

## Introduction

**Marro News - Backend** is the REST API powering the Marro News web application. This project was developed as an educational assignment for Semester 3 at Fontys University of Applied Sciences. It provides robust backend services for user authentication, article management, statistics, and real-time updates, following modern software engineering practices.

This repository is intended for educational purposes. If you wish to use, modify, or publish any part of this codebase, please contact the author for permission. Unauthorized redistribution or modification is not allowed.

## Tech Stack

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Microsoft SQL Server](https://img.shields.io/badge/MS_SQL_Server-CC2927?style=for-the-badge&logo=microsoftsqlserver&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=socketdotio&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![GitLab CI](https://img.shields.io/badge/GitLab_CI-FC6D26?style=for-the-badge&logo=gitlab&logoColor=white)
![SonarQube](https://img.shields.io/badge/SonarQube-4E9BCD?style=for-the-badge&logo=sonarqube&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Jacoco](https://img.shields.io/badge/JaCoCo-C71A36?style=for-the-badge&logo=java&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-BC4521?style=for-the-badge&logo=java&logoColor=white)

## Software Architecture

The backend follows a layered architecture using Spring Boot, with clear separation of concerns between controllers, services, repositories, and domain models.

**C3 Context Diagram:**

![C3 Software Architecture](docs/C3.png)

---

## Features

- JWT-based authentication & role-based authorization (Admin, Journalist, User)
- Article management with approval workflow
- Favourites list per user
- Article & favourites statistics
- Real-time updates via WebSocket
- Article and journalist search with filtering
- CI/CD pipeline with build, test, SonarQube analysis, and Docker deployment

## Getting Started

### Prerequisites

- Java 17
- Gradle
- Microsoft SQL Server (or use the H2 in-memory DB for testing)
- Docker (optional)

### Run locally

```bash
./gradlew bootRun
```

### Run tests

```bash
./gradlew test
```

### Build & run with Docker

```bash
./gradlew assemble
docker build -t marro-news-backend .
docker run -p 8080:8080 marro-news-backend
```


## File Structure

The main project structure is as follows:

```
src/
	main/
		java/nl/fontys/newswebapplication/
			controllers/      # REST controllers
			domain/           # Domain models and enums
			repositories/     # Data access layer
			services/         # Business logic
		resources/          # Application properties
	test/
		java/nl/fontys/newswebapplication/  # Unit and integration tests
build.gradle            # Gradle build file
Dockerfile              # Docker containerization
README.md               # Project documentation
```

---

## Documentation

This repository includes additional system and project documentation to support understanding of the architecture, design decisions, and quality assurance processes.

All documentation is stored in the `docs/` folder:

- **C3 diagram**: system-level architecture overview  
  [View C3 diagram](docs/C3.png)

- **UML class diagram**: structural representation of domain models and relationships  
  [View UML diagram](docs/uml_class_diagram_news.pdf)

- **Design document (C4 + architecture decisions)**: system design using C4 diagrams, key architectural decisions, and technology stack choices  
  [View design document](docs/design_document.pdf)

- **Security report (OWASP Top 10)**: security analysis mapped against OWASP Top 10 risks, including mitigations and findings  
  [View security report](docs/security_report.pdf)

- **SonarQube report**: code quality, maintainability, and static analysis results  
  [View SonarQube report](docs/sonarqube_report.pdf)

- **UX report**: user experience analysis and design considerations  
  [View UX report](docs/ux_report.pdf)

These documents provide additional context for reviewers and are intended to complement the implementation by illustrating design reasoning, system structure, security considerations, and engineering quality practices.

---

## License & Usage

This project is distributed for educational use only. **Not for commercial use.**

- Please request permission before modifying or publishing any part of this repository.
- For questions or collaboration, contact the project author.