import java.util.*;

// -------------------- Booking Request --------------------
class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- Inventory Manager (Thread-Safe) --------------------
class InventoryManager {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryManager() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    // 🔒 Critical Section
    public synchronized String allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available <= 0) {
            return null;
        }

        // Simulate delay (to expose race conditions if not synchronized)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        inventory.put(roomType, available - 1);

        return roomType.substring(0, 1) + available;
    }

    public void printInventory() {
        System.out.println("Final Inventory: " + inventory);
    }
}

// -------------------- Booking Queue --------------------
class BookingQueue {

    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// -------------------- Booking Processor (Thread) --------------------
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private InventoryManager inventory;

    public BookingProcessor(String name, BookingQueue queue, InventoryManager inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {

            BookingRequest request;

            // 🔒 Synchronize queue access
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) {
                break;
            }

            // Process booking
            String roomId = inventory.allocateRoom(request.getRoomType());

            if (roomId != null) {
                System.out.println(getName() + " SUCCESS: "
                        + request.getGuestName()
                        + " booked " + request.getRoomType()
                        + " RoomID=" + roomId);
            } else {
                System.out.println(getName() + " FAILED: "
                        + request.getGuestName()
                        + " - No rooms available");
            }
        }
    }
}

// -------------------- Main Application --------------------
public class BookMyStay {

    public static void main(String[] args) {

        InventoryManager inventory = new InventoryManager();
        BookingQueue queue = new BookingQueue();

        // -------------------- Simulate Multiple Requests --------------------
        queue.addRequest(new BookingRequest("Alice", "Deluxe"));
        queue.addRequest(new BookingRequest("Bob", "Deluxe"));      // conflict
        queue.addRequest(new BookingRequest("Charlie", "Standard"));
        queue.addRequest(new BookingRequest("David", "Standard"));
        queue.addRequest(new BookingRequest("Eve", "Standard"));    // overflow

        // -------------------- Multiple Threads --------------------
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);
        BookingProcessor t3 = new BookingProcessor("Thread-3", queue, inventory);

        t1.start();
        t2.start();
        t3.start();

        // Wait for threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // -------------------- Final State --------------------
        System.out.println();
        inventory.printInventory();
        System.out.println("\n[No double booking occurred due to synchronization]");
    }
}