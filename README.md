# Mars Rover Java Project

## Description
This Java program simulates the navigation of robotic rovers deployed by NASA on a rectangular plateau on Mars. Each rover receives commands to rotate and move on a grid in order to scan the surrounding terrain.

The program reads input from a file and processes a sequence of movements for each rover, outputting their final positions.

---

## Features
- Supports multiple rovers with sequential execution
- Command parsing from input file
- Boundary control: rovers can't move off the plateau
- Clean object-oriented design
- Fully tested with JUnit 5

---

## Technologies
- Java 17
- Maven (build & test)
- JUnit 5

---

## Project Structure
```
rover/
├── pom.xml
├── input.txt
├── src/
│   ├── main/java/com/nasa/Rover.java
│   └── test/java/com/nasa/RoverTest.java
└── target/ (generated after build)
```

---

## How to Run

### 1. Build the Project
```bash
mvn clean package
```

### 2. Execute the Program
```bash
java -jar target/rover.jar input.txt
```

> Make sure `input.txt` is in the root directory and contains valid instructions.

### Example Input (`input.txt`)
```
5 5
1 2 N
LMLMLMLMM
3 3 E
MMRMMRMRRM
```

### Expected Output
```
1 3 N
5 1 E
```

---

## How to Run Tests
```bash
mvn test
```

Tests are defined in `RoverTest.java` and cover a wide range of scenarios including edge cases, invalid commands, and plateau boundaries.

---

## Author
Mayer (EPITA) — Student Engineer in Quantum Computing

---

## License
This project is open-source and available under the [MIT License](https://opensource.org/licenses/MIT).

