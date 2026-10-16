# 📚 Library Management System

A console-based Library Management System developed in Java. This project provides an end-to-end command-line interface for managing books, registering members, issuing/returning books with overdue tracking, calculating penalty fines, and reviewing loan histories.

---

## 🚀 Features

- **📖 Book Management**:
  - Add new books with auto-incremented IDs.
  - View catalog with availability status (`Available` / `Issued`).
  - Search books by title or author keyword (case-insensitive).
  - Remove books (restricted if a book is currently loaned out).

- **👤 Member Management**:
  - Register new members with name and contact details.
  - View all registered members.

- **🔄 Circulation & Loan Tracking**:
  - **Issue Books**: Set 14-day loan duration with validation preventing checkout if a member holds an overdue book or if the book is already checked out.
  - **Return Books**: Automatically calculate overdue days and penalty fines (Rs. 5.00 per overdue day).
  - **Overdue Tracking**: List active overdue loans with fine calculations.
  - **Loan History**: Review audit records of all current and historical book loans.

---

## 🛠️ Project Structure & Architecture

All application logic is implemented in [`Main.java`](file:///c:/Users/sivas/OneDrive/Desktop/java%20intership/Main.java):

| Class | Description |
|---|---|
| [`Main`](file:///c:/Users/sivas/OneDrive/Desktop/java%20intership/Main.java#L286-L326) | Application entry point driving the interactive CLI menu loop. |
| [`Book`](file:///c:/Users/sivas/OneDrive/Desktop/java%20intership/Main.java#L7-L17) | Data model representing a book (`id`, `title`, `author`). |
| [`Member`](file:///c:/Users/sivas/OneDrive/Desktop/java%20intership/Main.java#L19-L29) | Data model representing a library member (`id`, `name`, `contact`). |
| [`Loan`](file:///c:/Users/sivas/OneDrive/Desktop/java%20intership/Main.java#L31-L55) | Manages loan details (`issueDate`, `dueDate`, `returnDate`) and computes overdue duration via `java.time.temporal.ChronoUnit`. |
| [`Library`](file:///c:/Users/sivas/OneDrive/Desktop/java%20intership/Main.java#L57-L284) | Encapsulates library business logic, validation rules, loan lifecycle, and input parsing. |

---

## 📋 Prerequisites

- **Java Development Kit (JDK)**: Version 14 or higher (JDK 17 or JDK 21 LTS recommended) installed and configured in your `PATH`.

To verify your Java installation:
```bash
java -version
javac -version
```

---

## 💻 How to Build and Run

### 1. Clone or Open Workspace
Open a terminal in the project root directory:
```bash
cd "c:\Users\sivas\OneDrive\Desktop\java intership"
```

### 2. Compile the Source Code
```bash
javac Main.java
```

### 3. Run the Application
```bash
java Main
```

---

## 🎮 Menu Options & Usage

Upon launch, you will be presented with the interactive menu:

```text
===== LIBRARY MANAGEMENT SYSTEM =====
1. Add Book
2. View All Books
3. Search Book
4. Remove Book
5. Register Member
6. View Members
7. Issue Book
8. Return Book
9. View Overdue Books
10. View Loan History
0. Exit
Enter choice:
```

### Configuration Constants
- **Default Loan Period**: `14 days`
- **Fine Rate**: `Rs. 5.00 / day` for overdue returns

---

## 📄 License
This project is open-source and intended for educational and internship training purposes.
