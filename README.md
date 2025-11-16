# Micro-Commerce API Demo

This project is a Spring Boot application built to demonstrate a modern, agile, and maintainable microservice architecture. It simulates the backend for an order processing system, handling order creation and dynamic cost calculation (shipping, tax).

The primary goal of this project is not just functionality, but to showcase clean code, separation of concerns, and the practical application of key design patterns (SOLID, Strategy, Factory, Adapter) to build a system that is easy to test, maintain, and extend.

---

## 🚀 Tech Stack

* **Java 17** 
* **Spring Boot** (Spring Web, Spring Data JPA)
* **Gradle** (Build Tool)
* **H2 Database** (In-memory SQL database)
* **Lombok** (To reduce boilerplate code)
* **JUnit 5 & Mockito** (For testing)

---

## ▶️ How to Run the Application

1.  Make the Gradle wrapper executable (if needed):
    ```bash
    chmod +x ./gradlew
    ```
2.  Run the application using the wrapper:
    ```bash
    ./gradlew bootRun
    ```
3.  The API will be live and accessible at `http://localhost:8080`.

---

## 🏛️ Architectural & Design Decisions

This project was built following key SOLID principles and design patterns to ensure the codebase is agile and maintainable.

### 1. 3-Tier Architecture (SOLID)

The application is split into three standard layers, following the **Single Responsibility Principle (SRP)**:

* **`controller`**: The API layer. Its only job is to handle HTTP requests, validate input, and delegate work to the service layer.
* **`service`**: The "brain" of the application. It holds all business logic and orchestrates different components (like the repository and factories).
* **`repository`**: The data access layer. Its only job is to interact with the database.

### 2. Dependency Inversion (The "D" in SOLID)

The project uses **Constructor Injection** to manage dependencies. High-level modules (like `OrderService`) depend on abstractions (interfaces like `TaxService`), not on concrete implementations. This makes the code decoupled and extremely easy to unit test with mocks.

### 3. Strategy Pattern (For Agility)

* **Problem:** The business needs to calculate shipping costs differently based on the shipping type (e.g., `STANDARD`, `EXPRESS`).
* **Solution:** The `strategy` package implements the **Strategy Pattern**.
* **Why:** This design follows the **Open/Closed Principle**. When the business wants to add a new `INTERNATIONAL` shipping method, we simply add one new class. No existing, working code needs to be modified, making the change fast and low-risk.

### 4. Factory Pattern (For Separation of Concerns)

* **Problem:** The `OrderService` shouldn't be responsible for knowing how to build and select all the different shipping strategies.
* **Solution:** The `ShippingStrategyFactory` is a dedicated class whose only job is to create and provide the correct `ShippingStrategy` object.
* **Why:** This cleans up the `OrderService` and further enforces the Single Responsibility Principle.

### 5. Adapter Pattern (For Legacy Integration)

* **Problem:** The application needs to get tax data from a (pretend) legacy system that has an "ugly" or incompatible API.
* **Solution:** The `TaxServiceAdapter` class wraps the `LegacyTaxCalculator`.
* **Why:** This **insulates** our modern application from the legacy system. The `OrderService` depends on a clean `TaxService` interface and has no idea it's talking to an old system. If we replace the legacy system later, the `OrderService` never has to change.

### 6. Builder Pattern (For Clean Tests)

* **Problem:** The `Order` entity is complex and creating test data is messy.
* **Solution:** The `Order` entity uses Lombok's `@Builder` annotation.
* **Why:** This allows for the creation of clean, readable test data in our unit tests (see `OrderControllerTest.java`).

---

## ✅ How to Test the Application

The project includes a comprehensive unit test for the controller layer, which can be run with:

```bash
./gradlew test