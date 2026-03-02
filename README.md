# Todo App — Java 8 Spring Boot

A web application built with Java 8 and the Spring framework that allows users to manage a todo list.

## Features

- ✅ Add, update, and delete todo items
- ✅ Mark items as completed or pending (toggle)
- ✅ Filter todos: All / Active / Completed
- ✅ Persistent H2 file-based storage (survives restarts)
- ✅ Clean Bootstrap 5 UI with icons
- ✅ Java 8 compatible (Spring Boot 2.7.x)

## Project Structure

```
src/java/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/example/todo/
    │   │   ├── TodoApplication.java       # Spring Boot entry point
    │   │   ├── controller/TodoController.java
    │   │   ├── model/Todo.java
    │   │   ├── repository/TodoRepository.java
    │   │   └── service/TodoService.java
    │   └── resources/
    │       ├── application.properties
    │       └── templates/
    │           ├── index.html             # Main todo list page
    │           └── edit.html              # Edit todo page
    └── test/
        └── java/com/example/todo/
            └── TodoApplicationTests.java  # Integration tests
```

## Prerequisites

- Java 8 or higher (JDK 8+)
- Maven 3.6+

## Setup & Run

```bash
# Navigate to the project directory
cd src/java

# Build the project
mvn clean package -DskipTests

# Run the application
java -jar target/todo-1.0.0.jar
```

Or run directly with Maven:

```bash
cd src/java
mvn spring-boot:run
```

The application starts on **http://localhost:8080**

## Running Tests

```bash
cd src/java
mvn test
```

## Usage

1. Open **http://localhost:8080** in your browser
2. **Add a todo**: Enter a title (required) and optional description, then click **Add**
3. **Mark complete/pending**: Click the ✔ (green) or ↺ (grey) button next to a todo
4. **Edit a todo**: Click the ✏ (pencil) button to open the edit page
5. **Delete a todo**: Click the 🗑 (trash) button and confirm
6. **Filter todos**: Use the All / Active / Completed tabs

## Data Persistence

Data is stored in an H2 file-based database at `./data/tododb` (relative to where the app is run). The data persists across application restarts.

You can also access the H2 console at **http://localhost:8080/h2-console** with:
- JDBC URL: `jdbc:h2:file:./data/tododb`
- Username: `sa`
- Password: *(leave empty)*
