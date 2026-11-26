# Harsh_Raj_Bisen-11263_Attendence_Tracker
# Attendance Tracker

A console-based Java application that records daily attendance of students, calculates attendance percentages, and sorts students by percentage. Developed as a DSA project (PS No. 2) using 2D arrays, HashMap, searching, sorting, methods and file handling.

## Features

- Add students with an automatically generated roll number (R101, R102, ...)
- Mark daily attendance as P (Present) or A (Absent), for days 1 to 60
- View the attendance percentage of one student
- View the full attendance register
- Sort students by attendance percentage, highest first
- Save data to `attendance.txt` on exit and load it on start-up

## How Attendance Is Stored

- Attendance is kept in a 2D array `int[100][60]`: rows are students, columns are days.
- Values: `1` = Present, `0` = Absent, `-1` = not marked.
- A `HashMap` maps each roll number to its row, so marking attendance does not need a search.

## Percentage Calculation

```
Attendance % = (Days Present / Days Recorded) x 100
```

Days Recorded counts only the days that have a P or A mark. Unmarked days are not counted.

## Requirements

- JDK 8 or above (JDK 21 recommended)
- VS Code, IntelliJ IDEA, Eclipse, or any text editor
- Command Prompt, Terminal, or the VS Code terminal

## How to Run

```bash
javac AttendanceTracker.java
java AttendanceTracker
```

## Menu

```
1. Add student
2. Mark attendance
3. View all attendance
4. View percentage
5. Sort by percentage
0. Save and exit
```

## Sample Run

```
Enter choice: 1
Enter student name: Karan
Student added. Roll: R101 | Karan

Enter choice: 2
Enter roll number: R101
Enter day number (1-60): 1
Enter status (P = Present, A = Absent): P
Attendance marked for Day 1.

Enter choice: 4
Enter roll number: R101
R101 | Karan | Present: 1/1 | 100.0%
```

## Input Rules

- Roll numbers must already exist before attendance can be marked.
- Status must be `P` or `A`.
- Day must be between 1 and 60.
- Names and roll numbers cannot be empty.

## Limitations

- Maximum 100 students and 60 days.
- Student names cannot contain commas, because commas separate fields in `attendance.txt`.
- Console only; no graphical interface or database.

## Project Structure

```
AttendanceTracker/
├── AttendanceTracker.java
├── attendance.txt      (created automatically on exit)
└── README.md
```

## Future Scope

- Store data in a database or CSV file
- Record dates instead of day numbers
- Warn when a percentage falls below a set limit
- Export reports to text or PDF
- Add a graphical interface

## Author

[Harsh Raj Bisen] | ERP ID: [11263]
Rungta International Skills University, Bhilai
