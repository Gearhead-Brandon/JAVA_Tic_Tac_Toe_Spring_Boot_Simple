# Tic-Tac-Toe-Simple — Java_Bootcamp  

## Summary  
In this project, you will learn how to develop a **web application** using **Java** and the **Spring framework**. The project involves implementing a **Tic-Tac-Toe game**, focusing on **API development, service layers, and dependency injection**.

## Table of Contents  
1. [Chapter I](#chapter-i)  
   - [Instructions](#instructions)  
2. [Chapter II](#chapter-ii)  
   - [General Information](#general-information)  
3. [Chapter III](#chapter-iii)  
   - [Task 0: Project Setup](#task-0-project-setup)  
   - [Task 1: Project Structure](#task-1-project-structure)  
   - [Task 2: Implementing the Domain Layer](#task-2-implementing-the-domain-layer)  
   - [Task 3: Implementing the Datasource Layer](#task-3-implementing-the-datasource-layer)  
   - [Task 4: Implementing the Web Layer](#task-4-implementing-the-web-layer)  
   - [Task 5: Implementing the DI Layer](#task-5-implementing-the-di-layer)  

## Chapter I
### Topics Covered:  
- Web Applications  
- Spring Framework  
- API Development  
- Minimax Algorithm  
- MVC Architecture  

## Chapter II
### Project: Tic-Tac-Toe Web Application  
This project involves building a **Spring-based** web application for **Tic-Tac-Toe**. The application will be structured into different layers and allow multiple concurrent games.  

### Task 0: Project Setup  
- Create a new project in **IntelliJ IDEA**.  
- Use **Java** as the programming language.  
- Use **Gradle** as the build system.  
- Select **JDK 18**.  
- Choose **Kotlin DSL** for Gradle configuration.  

### Task 1: Project Structure  
- The project should be structured into the following **layers**:  
  - **Web Layer** (for handling user interaction): model, controller, mapper.  
  - **Domain Layer** (for business logic): model, service.  
  - **Datasource Layer** (for data persistence): model, repository, mapper.  
  - **DI Layer** (for dependency injection).  

### Task 2: Implementing the Domain Layer  
- Define a **game board** as an integer matrix.  
- Define a **Game model** with a **UUID** and a board.  
- Create a **service interface** with methods for:  
  - Computing the **next move** using the **Minimax algorithm**.  
  - **Validating** the game board.  
  - **Checking** if the game has ended.  

### Task 3: Implementing the Datasource Layer  
- Implement an **in-memory storage class** using **thread-safe collections**.  
- Define **data models** for the game board and game state.  
- Implement **mappers** between **domain and datasource layers**.  
- Implement a **repository** with methods to:  
  - **Save** the current game.  
  - **Retrieve** the current game.  
- Create a **service implementation** that interacts with the repository.  

### Task 4: Implementing the Web Layer  
- Define **data models** for API communication.  
- Implement **mappers** between **domain and web layers**.  
- Create a **Spring controller** with a **POST /game/{UUID}** endpoint:  
  - Receives a **game update from the user**.  
  - Returns the **updated game state** after the computer makes a move.  
  - Returns an **error response** if the input game state is invalid.  
- The application should **support multiple concurrent games**.  

### Task 5: Implementing the DI Layer  
- Create a **Spring Configuration class** to manage dependencies.  
- Define the following **beans**:  
  - **Singleton storage class** for game sessions.  
  - **Repository** for handling game data.  
  - **Service** for game logic.  

## Conclusion  
This project provides hands-on experience with **Spring framework**, **REST API development**, and **MVC architecture**. By completing it, you will gain valuable skills in **Java web development**, **dependency injection**, and **game logic implementation**.
