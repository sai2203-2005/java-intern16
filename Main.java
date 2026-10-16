import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Book {
    int id;
    String title;
    String author;

    Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
}

class Member {
    int id;
    String name;
    String contact;

    Member(int id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }
}

class Loan {
    int bookId;
    int memberId;
    LocalDate issueDate;
    LocalDate dueDate;
    LocalDate returnDate;

    Loan(int bookId, int memberId, LocalDate issueDate,
         LocalDate dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    boolean isActive() {
        return returnDate == null;
    }

    long overdueDays(LocalDate date) {
        return Math.max(0, ChronoUnit.DAYS.between(dueDate, date));
    }
}

class Library {
    private final List<Book> books = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    private final List<Loan> loans = new ArrayList<>();

    private int nextBookId = 1;
    private int nextMemberId = 1;

    private static final int LOAN_DAYS = 14;
    private static final double FINE_PER_DAY = 5.0;

    private Book findBook(int id) {
        for (Book book : books) {
            if (book.id == id) return book;
        }
        return null;
    }

    private Member findMember(int id) {
        for (Member member : members) {
            if (member.id == id) return member;
        }
        return null;
    }

    private Loan findActiveLoan(int bookId) {
        for (Loan loan : loans) {
            if (loan.bookId == bookId && loan.isActive()) {
                return loan;
            }
        }
        return null;
    }

    private boolean hasOverdueLoan(int memberId) {
        LocalDate today = LocalDate.now();

        for (Loan loan : loans) {
            if (loan.memberId == memberId && loan.isActive()
                    && loan.overdueDays(today) > 0) {
                return true;
            }
        }
        return false;
    }

    void addBook(Scanner sc) {
        System.out.print("Enter book title: ");
        String title = sc.nextLine().trim();

        System.out.print("Enter author: ");
        String author = sc.nextLine().trim();

        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("Title and author cannot be empty.");
            return;
        }

        Book book = new Book(nextBookId++, title, author);
        books.add(book);
        System.out.println("Book added. ID: " + book.id);
    }

    void viewBooks() {
        System.out.println("\n--- BOOK LIST ---");

        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        for (Book book : books) {
            String status = findActiveLoan(book.id) == null
                    ? "Available" : "Issued";

            System.out.printf(
                "ID: %d | Title: %s | Author: %s | Status: %s%n",
                book.id, book.title, book.author, status
            );
        }
    }

    void searchBook(Scanner sc) {
        System.out.print("Enter title or author to search: ");
        String query = sc.nextLine().trim().toLowerCase();
        boolean found = false;

        for (Book book : books) {
            if (book.title.toLowerCase().contains(query)
                    || book.author.toLowerCase().contains(query)) {
                System.out.printf(
                    "ID: %d | %s | Author: %s | %s%n",
                    book.id, book.title, book.author,
                    findActiveLoan(book.id) == null
                            ? "Available" : "Issued"
                );
                found = true;
            }
        }

        if (!found) System.out.println("No matching books found.");
    }

    void removeBook(Scanner sc) {
        System.out.print("Enter book ID to remove: ");
        int id = readInt(sc);

        Book book = findBook(id);

        if (book == null) {
            System.out.println("Book not found.");
        } else if (findActiveLoan(id) != null) {
            System.out.println("Cannot remove an issued book.");
        } else {
            books.remove(book);
            System.out.println("Book removed.");
        }
    }

    void registerMember(Scanner sc) {
        System.out.print("Enter member name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter contact details: ");
        String contact = sc.nextLine().trim();

        if (name.isEmpty() || contact.isEmpty()) {
            System.out.println("Name and contact cannot be empty.");
            return;
        }

        Member member = new Member(nextMemberId++, name, contact);
        members.add(member);
        System.out.println("Member registered. ID: " + member.id);
    }

    void viewMembers() {
        System.out.println("\n--- MEMBER LIST ---");

        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }

        for (Member member : members) {
            System.out.printf(
                "ID: %d | Name: %s | Contact: %s%n",
                member.id, member.name, member.contact
            );
        }
    }

    void issueBook(Scanner sc) {
        System.out.print("Enter book ID: ");
        int bookId = readInt(sc);

        System.out.print("Enter member ID: ");
        int memberId = readInt(sc);

        Book book = findBook(bookId);
        Member member = findMember(memberId);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        if (findActiveLoan(bookId) != null) {
            System.out.println("Book is already issued.");
            return;
        }

        if (hasOverdueLoan(memberId)) {
            System.out.println(
                "Cannot issue: member has an overdue book."
            );
            return;
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(LOAN_DAYS);

        loans.add(new Loan(bookId, memberId, issueDate, dueDate));

        System.out.println("Book issued successfully.");
        System.out.println("Issue date: " + issueDate);
        System.out.println("Due date: " + dueDate);
    }

    void returnBook(Scanner sc) {
        System.out.print("Enter book ID to return: ");
        int bookId = readInt(sc);

        Loan loan = findActiveLoan(bookId);

        if (loan == null) {
            System.out.println("No active issue found for this book.");
            return;
        }

        LocalDate today = LocalDate.now();
        long overdue = loan.overdueDays(today);
        double fine = overdue * FINE_PER_DAY;

        loan.returnDate = today;

        System.out.println("Book returned successfully.");
        System.out.println("Return date: " + today);
        System.out.println("Overdue days: " + overdue);
        System.out.printf("Fine: Rs. %.2f%n", fine);
    }

    void viewOverdueBooks() {
        System.out.println("\n--- OVERDUE BOOKS ---");
        LocalDate today = LocalDate.now();
        boolean found = false;

        for (Loan loan : loans) {
            if (loan.isActive() && loan.overdueDays(today) > 0) {
                Book book = findBook(loan.bookId);
                Member member = findMember(loan.memberId);

                if (book != null && member != null) {
                    long days = loan.overdueDays(today);
                    double fine = days * FINE_PER_DAY;

                    System.out.printf(
                        "Book: %s | Member: %s | Due: %s"
                        + " | Overdue: %d days | Fine: Rs. %.2f%n",
                        book.title, member.name, loan.dueDate,
                        days, fine
                    );
                    found = true;
                }
            }
        }

        if (!found) System.out.println("No overdue books.");
    }

    void viewLoanHistory() {
        System.out.println("\n--- LOAN HISTORY ---");

        if (loans.isEmpty()) {
            System.out.println("No loan records found.");
            return;
        }

        for (Loan loan : loans) {
            Book book = findBook(loan.bookId);
            Member member = findMember(loan.memberId);

            String bookTitle = book == null
                    ? "[Removed book]" : book.title;
            String memberName = member == null
                    ? "[Unknown member]" : member.name;

            System.out.printf(
                "Book: %s | Member: %s | Issued: %s"
                + " | Due: %s | Returned: %s%n",
                bookTitle, memberName, loan.issueDate,
                loan.dueDate,
                loan.returnDate == null
                        ? "Not returned" : loan.returnDate
            );
        }
    }

    static int readInt(Scanner sc) {
        while (true) {
            String value = sc.nextLine().trim();

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid numeric ID/choice: ");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = new Library();
        int choice;

        do {
            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Book");
            System.out.println("4. Remove Book");
            System.out.println("5. Register Member");
            System.out.println("6. View Members");
            System.out.println("7. Issue Book");
            System.out.println("8. Return Book");
            System.out.println("9. View Overdue Books");
            System.out.println("10. View Loan History");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = Library.readInt(sc);

            switch (choice) {
                case 1 -> library.addBook(sc);
                case 2 -> library.viewBooks();
                case 3 -> library.searchBook(sc);
                case 4 -> library.removeBook(sc);
                case 5 -> library.registerMember(sc);
                case 6 -> library.viewMembers();
                case 7 -> library.issueBook(sc);
                case 8 -> library.returnBook(sc);
                case 9 -> library.viewOverdueBooks();
                case 10 -> library.viewLoanHistory();
                case 0 -> System.out.println("Exiting library system...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        sc.close();
    }
}
