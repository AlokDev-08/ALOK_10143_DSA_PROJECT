# Vaccination Slot Booking System

A console-based Java application that manages vaccination appointments using core Data Structures and Algorithms: a **Circular Queue**, an **Array**, a **HashMap** and **File Handling**.

Built as a Data Structures and Algorithms (DSA) course project at Rungta International Skills University.

## Features

- Book a vaccination slot (minimum age 18, maximum 3 slots per session)
- Automatic unique Booking IDs starting from `V101`
- First In, First Out (FIFO) vaccination order
- Vaccinate the next person and reuse the freed slot
- View all active bookings in queue order
- Search a booking by Booking ID
- Input validation for age and menu choices
- Bookings saved to `bookings.txt` on exit and loaded on startup

## Data Structures Used

| Structure | Purpose |
|---|---|
| Circular Queue (array-based) | Manages the 3 fixed slots in FIFO order and reuses freed slots |
| HashMap<String, Booking> | Finds a booking by Booking ID in O(1) average time |
| File handling (`bookings.txt`) | Keeps active bookings between runs |

## Project Structure

```
VaccinationSlotBooking/
├── Booking.java            # Stores Booking ID, name and age
├── CircularQueue.java      # Array-based circular queue (front, rear, count)
├── VaccinationSystem.java  # Booking logic, HashMap and file handling
├── Main.java               # Menu-driven console interface
└── README.md
```

## How to Run

Requirements: JDK 8 or higher.

```bash
# 1. Clone the repository
git clone https://github.com/<your-username>/<repo-name>.git
cd <repo-name>

# 2. Compile
javac *.java

# 3. Run
java Main
```

You can also open the folder in IntelliJ IDEA, VS Code or Eclipse and run `Main`.

## Menu

```
===== Vaccination Slot Booking System =====
1. Book Slot
2. Vaccinate Next
3. View Queue
4. Search Booking
5. Exit
```

## Sample Output

```
Enter your choice: 1
Enter name: Riya
Enter age: 25
Slot booked successfully. Booking ID: V101. Position: 1.

Enter your choice: 2
Riya vaccinated. Slot freed.
```

## How the Circular Queue Works

- `front` points to the next person to be vaccinated.
- `rear` points to the most recent booking.
- `count` stores the number of active bookings.
- Indexes wrap around using `(index + 1) % capacity`, so a slot freed at the front can be reused at the rear.

## File Format

`bookings.txt` stores one active booking per line as `BookingID,Name,Age`:

```
V101,Riya,25
V102,Dev,30
V103,Meera,30
```

## Time Complexity

| Operation | Complexity |
|---|---|
| Enqueue / Dequeue | O(1) |
| Check full / empty | O(1) |
| Search by Booking ID (HashMap) | O(1) average |
| View queue | O(N), N = 3 |
| Load / save file | O(N) |

## Test Cases

| ID | Scenario | Result |
|---|---|---|
| TC-01 | Successful slot booking | Pass |
| TC-02 | Booking when queue is full | Pass |
| TC-03 | Invalid age | Pass |
| TC-04 | Vaccinating the next person | Pass |

## Future Scope

- Graphical user interface (GUI)
- Multiple sessions and vaccination centres
- Database connectivity
- User authentication and admin access
- Cancel or modify bookings
- Vaccination history and reports
- Online appointment booking

## Author

**Alok Kumar** (ERP ID: RU-25-10143)
B.Tech CSE (AI & ML) in association with Google, Rungta International Skills University, Bhilai

Guide: Mr. Suryamani Verma 
