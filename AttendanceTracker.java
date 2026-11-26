import java.io.*;
import java.util.*;

public class AttendanceTracker {

    static final int MAX_STUDENTS = 100;
    static final int MAX_DAYS = 60;
    static final String FILE_NAME = "attendance.txt";

    // Rows = students, columns = days. 1 = Present, 0 = Absent, -1 = not marked
    static int[][] attendance = new int[MAX_STUDENTS][MAX_DAYS];
    static String[] rolls = new String[MAX_STUDENTS];
    static String[] names = new String[MAX_STUDENTS];
    static HashMap<String, Integer> rollToRow = new HashMap<>();
    static int studentCount = 0;
    static int daysUsed = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        for (int[] row : attendance) Arrays.fill(row, -1);
        loadData();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt(sc, "Enter choice: ");
            switch (choice) {
                case 1: addStudent(sc); break;
                case 2: markAttendance(sc); break;
                case 3: viewAll(); break;
                case 4: viewPercentage(sc); break;
                case 5: sortByPercentage(); break;
                case 0:
                    saveData();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        System.out.println("Goodbye!");
        sc.close();
    }

    static void printMenu() {
        System.out.println("\n===== ATTENDANCE TRACKER =====");
        System.out.println("1. Add student");
        System.out.println("2. Mark attendance");
        System.out.println("3. View all attendance");
        System.out.println("4. View percentage");
        System.out.println("5. Sort by percentage");
        System.out.println("0. Save and exit");
    }

    static void addStudent(Scanner sc) {
        if (studentCount == MAX_STUDENTS) {
            System.out.println("Register is full. Cannot add more students.");
            return;
        }
        String name = readText(sc, "Enter student name: ");
        String roll = "R" + (101 + studentCount);
        rolls[studentCount] = roll;
        names[studentCount] = name;
        rollToRow.put(roll, studentCount);
        studentCount++;
        System.out.println("Student added. Roll: " + roll + " | " + name);
    }

    static void markAttendance(Scanner sc) {
        String roll = readText(sc, "Enter roll number: ").toUpperCase();
        Integer row = rollToRow.get(roll);
        if (row == null) {
            System.out.println("Roll number " + roll + " not found.");
            return;
        }
        int day = readInt(sc, "Enter day number (1-" + MAX_DAYS + "): ");
        while (day < 1 || day > MAX_DAYS) {
            day = readInt(sc, "Day must be between 1 and " + MAX_DAYS + ": ");
        }
        String status = readText(sc, "Enter status (P = Present, A = Absent): ").toUpperCase();
        while (!status.equals("P") && !status.equals("A")) {
            status = readText(sc, "Only P or A is accepted. Enter status: ").toUpperCase();
        }
        attendance[row][day - 1] = status.equals("P") ? 1 : 0;
        if (day > daysUsed) daysUsed = day;
        System.out.println("Attendance marked for Day " + day + ".");
    }

    // Returns {present, recorded}: recorded counts only the days this student was marked
    static int[] stats(int row) {
        int present = 0, recorded = 0;
        for (int d = 0; d < MAX_DAYS; d++) {
            if (attendance[row][d] != -1) {
                recorded++;
                if (attendance[row][d] == 1) present++;
            }
        }
        return new int[]{present, recorded};
    }

    static double percentage(int row) {
        int[] s = stats(row);
        return s[1] == 0 ? 0.0 : (s[0] * 100.0) / s[1];
    }

    static void viewAll() {
        if (studentCount == 0) {
            System.out.println("No students registered.");
            return;
        }
        System.out.println("\nRoll | Name | Present/Recorded | Percentage");
        for (int r = 0; r < studentCount; r++) {
            int[] s = stats(r);
            System.out.printf("%s | %s | %d/%d | %.1f%%%n", rolls[r], names[r], s[0], s[1], percentage(r));
        }
    }

    static void viewPercentage(Scanner sc) {
        String roll = readText(sc, "Enter roll number: ").toUpperCase();
        Integer row = rollToRow.get(roll);
        if (row == null) {
            System.out.println("Roll number " + roll + " not found.");
            return;
        }
        int[] s = stats(row);
        System.out.printf("%s | %s | Present: %d/%d | %.1f%%%n", rolls[row], names[row], s[0], s[1], percentage(row));
    }

    static void sortByPercentage() {
        if (studentCount == 0) {
            System.out.println("No students registered.");
            return;
        }
        int[] order = new int[studentCount];
        for (int i = 0; i < studentCount; i++) order[i] = i;

        // Selection sort: highest percentage first, ties broken by roll number
        for (int i = 0; i < studentCount - 1; i++) {
            int best = i;
            for (int j = i + 1; j < studentCount; j++) {
                if (isBetter(order[j], order[best])) best = j;
            }
            int temp = order[i];
            order[i] = order[best];
            order[best] = temp;
        }

        System.out.println("\nStudents sorted by percentage (highest first):");
        for (int i = 0; i < studentCount; i++) {
            int r = order[i];
            int[] s = stats(r);
            System.out.printf("%d. %s | %s | %d/%d | %.1f%%%n", i + 1, rolls[r], names[r], s[0], s[1], percentage(r));
        }
    }

    static boolean isBetter(int a, int b) {
        double pa = percentage(a);
        double pb = percentage(b);
        if (pa != pb) return pa > pb;
        return rolls[a].compareTo(rolls[b]) < 0;
    }

    static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a whole number.");
            }
        }
    }

    static String readText(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Input cannot be empty.");
        }
    }

    static void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String header = br.readLine();
            if (header == null) return;
            daysUsed = Integer.parseInt(header.trim());
            String line;
            while ((line = br.readLine()) != null && studentCount < MAX_STUDENTS) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                int row = studentCount;
                rolls[row] = parts[0];
                names[row] = parts[1];
                rollToRow.put(parts[0], row);
                for (int d = 0; d < daysUsed && d + 2 < parts.length; d++) {
                    attendance[row][d] = toValue(parts[d + 2]);
                }
                studentCount++;
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load data: " + e.getMessage());
        }
    }

    static void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            pw.println(daysUsed);
            for (int r = 0; r < studentCount; r++) {
                StringBuilder line = new StringBuilder(rolls[r] + "," + names[r]);
                for (int d = 0; d < daysUsed; d++) {
                    line.append(',').append(code(attendance[r][d]));
                }
                pw.println(line);
            }
            System.out.println("Data saved to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    static int toValue(String s) {
        if (s.equals("P")) return 1;
        if (s.equals("A")) return 0;
        return -1;
    }

    static char code(int v) {
        if (v == 1) return 'P';
        if (v == 0) return 'A';
        return '-';
    }
}
