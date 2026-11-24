import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

// ========== Booking: stores details of one person ==========
// Stores the details of one person who books a vaccination slot.
class Booking {
    private String bookingId;
    private String name;
    private int age;

    public Booking(String bookingId, String name, int age) {
        this.bookingId = bookingId;
        this.name = name;
        this.age = age;
    }

    public String getBookingId() { return bookingId; }
    public String getName()      { return name; }
    public int getAge()          { return age; }

    public String toString() {
        return bookingId + " | " + name + " | Age " + age;
    }
}

// ========== CircularQueue: fixed slots, FIFO ==========
// Circular Queue built on an array. It holds the fixed vaccination slots.
// FIFO: the person who books first is vaccinated first.
class CircularQueue {
    private Booking[] queue;
    private int front;     // index of the first person waiting
    private int rear;      // index of the last person added
    private int count;     // number of active bookings
    private int capacity;  // total slots (3)

    public CircularQueue(int capacity) {
        this.capacity = capacity;
        queue = new Booking[capacity];
        front = 0;
        rear = -1;
        count = 0;
    }

    public boolean isFull()  { return count == capacity; }
    public boolean isEmpty() { return count == 0; }

    // Add a booking at the rear. O(1)
    public boolean enqueue(Booking b) {
        if (isFull()) return false;
        rear = (rear + 1) % capacity;   // wraps around: this makes it "circular"
        queue[rear] = b;
        count++;
        return true;
    }

    // Remove the booking at the front. O(1)
    public Booking dequeue() {
        if (isEmpty()) return null;
        Booking b = queue[front];
        queue[front] = null;
        front = (front + 1) % capacity; // freed slot can be reused
        count--;
        return b;
    }

    public int size() { return count; }

    // Returns all active bookings in FIFO order (used for saving to file).
    public Booking[] getAll() {
        Booking[] list = new Booking[count];
        for (int i = 0; i < count; i++) {
            list[i] = queue[(front + i) % capacity];
        }
        return list;
    }

    // Show all active bookings in FIFO order. O(N)
    public void displayQueue() {
        if (isEmpty()) {
            System.out.println("No active bookings.");
            return;
        }
        Booking[] list = getAll();
        for (int i = 0; i < list.length; i++) {
            System.out.println((i + 1) + ". " + list[i]);
        }
    }
}

// ========== VaccinationSystem: main logic ==========

// Main logic: connects the Circular Queue, the HashMap and the file.
class VaccinationSystem {
    private static final int CAPACITY = 3;
    private static final int MIN_AGE = 18;
    private static final String FILE_NAME = "bookings.txt";

    private CircularQueue queue = new CircularQueue(CAPACITY);
    private HashMap<String, Booking> bookings = new HashMap<>();
    private int nextBookingNumber = 101;   // first ID is V101

    // 1. Book a slot
    public void bookSlot(Scanner sc) {
        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter age: ");
        int age;
        try {
            age = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid age. Booking not done.");
            return;
        }
        if (name.isEmpty() || age < MIN_AGE || age > 120) {
            System.out.println("Invalid age or name (minimum age is 18). Booking not done.");
            return;
        }
        if (queue.isFull()) {
            System.out.println("Slots full. Please try the next session.");
            return;
        }
        String id = "V" + nextBookingNumber++;
        Booking b = new Booking(id, name, age);
        queue.enqueue(b);          // add to circular queue
        bookings.put(id, b);       // store in HashMap
        System.out.println("Slot booked successfully. Booking ID: " + id
                + ". Position: " + queue.size() + ".");
    }

    // 2. Vaccinate the person at the front
    public void vaccinateNext() {
        Booking b = queue.dequeue();
        if (b == null) {
            System.out.println("No people in queue.");
            return;
        }
        bookings.remove(b.getBookingId());
        System.out.println(b.getName() + " vaccinated. Slot freed.");
    }

    // 3. View the queue
    public void viewQueue() {
        System.out.println("--- Current Queue (FIFO) ---");
        queue.displayQueue();
    }

    // 4. Search by Booking ID using the HashMap. O(1) average
    public void searchBooking(Scanner sc) {
        System.out.print("Enter Booking ID: ");
        String id = sc.nextLine().trim().toUpperCase();
        Booking b = bookings.get(id);
        if (b == null) System.out.println("Booking not found.");
        else System.out.println("Found: " + b);
    }

    // Load saved bookings when the program starts
    public void loadBookings() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length != 3) continue;
                Booking b = new Booking(p[0], p[1], Integer.parseInt(p[2].trim()));
                if (queue.enqueue(b)) {
                    bookings.put(b.getBookingId(), b);
                    int num = Integer.parseInt(p[0].substring(1));
                    if (num >= nextBookingNumber) nextBookingNumber = num + 1;
                }
            }
        } catch (Exception e) {
            System.out.println("Could not read " + FILE_NAME + ": " + e.getMessage());
        }
    }

    // Save active bookings when the program exits
    public void saveBookings() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Booking b : queue.getAll()) {
                pw.println(b.getBookingId() + "," + b.getName() + "," + b.getAge());
            }
            System.out.println("Bookings saved to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Could not save bookings: " + e.getMessage());
        }
    }
}

// ========== Main: menu (run this class) ==========

// Menu-driven console interface.
public class VaccinationSlotBooking {
    static void displayMenu() {
        System.out.println("\n===== Vaccination Slot Booking System =====");
        System.out.println("1. Book Slot");
        System.out.println("2. Vaccinate Next");
        System.out.println("3. View Queue");
        System.out.println("4. Search Booking");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        VaccinationSystem system = new VaccinationSystem();
        system.loadBookings();

        boolean running = true;
        while (running) {
            displayMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": system.bookSlot(sc); break;
                case "2": system.vaccinateNext(); break;
                case "3": system.viewQueue(); break;
                case "4": system.searchBooking(sc); break;
                case "5":
                    system.saveBookings();
                    System.out.println("Thank you. Goodbye!");
                    running = false;
                    break;
                default: System.out.println("Invalid choice. Enter 1 to 5.");
            }
        }
        sc.close();
    }
}
