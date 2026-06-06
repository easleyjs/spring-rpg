🐉 Red Dragon RPG
> *A browser-based RPG inspired by the classic BBS game **Legend of the Red Dragon (LORD)***
Built as both a tribute to one of the earliest online RPG experiences and a vehicle for learning modern backend development with Java and Spring Boot.
---
Overview
Legend of the Red Dragon was a popular text-based role-playing game that ran on dial-up Bulletin Board Systems during the 1980s and 1990s. Players would log in, battle monsters, purchase equipment, explore locations, and progress their characters through a simple but addictive gameplay loop.
Red Dragon RPG recreates the core mechanics of that experience using a modern web architecture. Players can create accounts, manage characters, engage in combat, purchase items from shops, and persist their progress through a RESTful backend service.
---
Goals
This project was designed with two primary objectives:
Recreate the core gameplay loop and feel of classic BBS RPGs.
Demonstrate practical backend development skills using Spring Boot and related technologies.
Key areas of focus include:
REST API design
Authentication and authorization
Database persistence
Service-layer architecture
Security with JWT tokens
Validation and exception handling
Frontend/backend integration
---
Features
✅ Implemented
User registration and authentication
JWT-based security
Character creation and persistence
Inventory management
Equipment purchasing and shop system
Combat system
Location-based gameplay
Role-based authorization (User / Admin)
Persistent PostgreSQL storage
🔧 In Progress
Pagination support
Request validation
Global exception handling improvements
Enhanced frontend presentation
📋 Planned
Quest system
Additional locations and monsters
Guilds and social features
Concurrency handling
Expanded admin tools
Graphical client using libGDX
---
Technology Stack
Layer	Technologies
Backend	Java, Spring Boot, Spring Security, Spring Data JPA, Hibernate, JWT, Maven
Database	PostgreSQL
Frontend	xterm.js, HTML, CSS, JavaScript
Deployment	Fly.io
---
Architecture
The application follows a standard layered Spring Boot architecture:
```
Client (xterm.js Frontend)
        │
        ▼
  REST Controllers
        │
        ▼
     Services
        │
        ▼
   Repositories
        │
        ▼
    PostgreSQL
```
Business logic is isolated within the service layer, while controllers focus on request handling and repositories manage data persistence.
---
Authentication Flow
Authentication is implemented using JSON Web Tokens (JWT):
User submits credentials
Server validates credentials
JWT token is issued
Client stores token
Token is sent with subsequent requests
Spring Security validates the token and authorizes access to protected resources
---
Inspiration
This project is heavily inspired by the classic BBS game Legend of the Red Dragon (LORD), originally created by Seth Able Robinson. The goal is not to create a direct clone, but rather to capture the spirit of early online role-playing games while exploring modern software development practices.
---
Learning Outcomes
Through this project I have gained hands-on experience with:
Designing REST APIs
Spring Security and JWT authentication
JPA entity modeling and relationships
Service-oriented architecture
Database schema design
Deployment and cloud hosting
Frontend/backend integration
---
Future Development
Long-term plans include expanding gameplay systems, improving the user interface, and eventually building a graphical client using libGDX while retaining the Spring Boot backend.
The project serves both as a portfolio piece and as an ongoing sandbox for exploring backend software engineering concepts.
