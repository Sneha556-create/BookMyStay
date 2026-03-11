import java.util.LinkedList;
import java.util.Queue;

public class BookMyStay {

    // ---------------- Room Domain Model ----------------
    static abstract class Room {
        private String type;

        public Room(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    static class SingleRoom extends Room {
        public SingleRoom() { super("Single"); }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() { super("Double"); }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() { super("Suite"); }
    }

    // ---------------- Booking Request ----------------
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
        }

        public void display() {
            System.out.println("Guest: " + guestName + ", Requested Room: " + roomType);
        }
    }

    // ---------------- Booking Request Queue ----------------
    static class BookingQueue {
        private Queue<Reservation> queue;

        public BookingQueue() {
            queue = new LinkedList<>();
        }

        // Add a new reservation request to the queue
        public void addRequest(Reservation reservation) {
            queue.add(reservation);
            System.out.println("Booking request added for guest: " + reservation.getGuestName());
        }

        // Peek at the next request without removing
        public Reservation peekNext() {
            return queue.peek();
        }

        // Process next request (optional, just for demonstration)
        public Reservation processNext() {
            return queue.poll();
        }

        // Display all pending requests
        public void displayQueue() {
            System.out.println("\n===== Pending Booking Requests =====");
            if (queue.isEmpty()) {
                System.out.println("No pending requests.");
            } else {
                for (Reservation res : queue) {
                    res.display();
                }
            }
            System.out.println("-----------------------------------\n");
        }
    }

    // ---------------- Application Entry ----------------
    public static void main(String[] args) {

        // Initialize booking queue
        BookingQueue bookingQueue = new BookingQueue();

        // Guests submit booking requests (arrival order matters)
        bookingQueue.addRequest(new Reservation("Alice", "Single"));
        bookingQueue.addRequest(new Reservation("Bob", "Double"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite"));
        bookingQueue.addRequest(new Reservation("Diana", "Single"));

        // Display all queued requests (FIFO order)
        bookingQueue.displayQueue();

        // Peek at the next request (without removing)
        System.out.println("Next request to process:");
        Reservation next = bookingQueue.peekNext();
        if (next != null) {
            next.display();
        }
    }
}