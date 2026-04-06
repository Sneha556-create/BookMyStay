import java.util.*;

// -------------------- Reservation --------------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", RoomType: " + roomType +
                ", RoomID: " + roomId +
                ", Cancelled: " + isCancelled;
    }
}

// -------------------- Inventory Manager --------------------
class InventoryManager {

    // Available rooms per type
    private Map<String, Integer> inventory = new HashMap<>();

    // Stack for rollback (LIFO)
    private Stack<String> releasedRoomStack = new Stack<>();

    public InventoryManager() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    // Allocate room (simulate unique room ID)
    public String allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available <= 0) {
            return null;
        }

        inventory.put(roomType, available - 1);

        // Generate room ID
        return roomType.substring(0, 1) + (available);
    }

    // Rollback: release room
    public void releaseRoom(String roomType, String roomId) {
        inventory.put(roomType, inventory.get(roomType) + 1);

        // Track rollback using stack
        releasedRoomStack.push(roomId);
    }

    public void printInventory() {
        System.out.println("Inventory: " + inventory);
    }

    public void printRollbackStack() {
        System.out.println("Rollback Stack (LIFO): " + releasedRoomStack);
    }
}

// -------------------- Booking Repository --------------------
class BookingRepository {

    private Map<String, Reservation> bookings = new HashMap<>();

    public void save(Reservation reservation) {
        bookings.put(reservation.getReservationId(), reservation);
    }

    public Reservation findById(String reservationId) {
        return bookings.get(reservationId);
    }
}

// -------------------- Cancellation Service --------------------
class CancellationService {

    private BookingRepository repository;
    private InventoryManager inventory;

    public CancellationService(BookingRepository repository, InventoryManager inventory) {
        this.repository = repository;
        this.inventory = inventory;
    }

    public void cancelBooking(String reservationId) {

        // Step 1: Validate existence
        Reservation reservation = repository.findById(reservationId);

        if (reservation == null) {
            System.out.println("Cancellation Failed: Reservation not found");
            return;
        }

        // Step 2: Prevent duplicate cancellation
        if (reservation.isCancelled()) {
            System.out.println("Cancellation Failed: Already cancelled");
            return;
        }

        // Step 3: Controlled rollback
        inventory.releaseRoom(reservation.getRoomType(), reservation.getRoomId());

        // Step 4: Update booking state
        reservation.cancel();

        System.out.println("Cancellation Successful: " + reservationId);
    }
}

// -------------------- Main Application --------------------
public class BookMyStay {

    public static void main(String[] args) {

        InventoryManager inventory = new InventoryManager();
        BookingRepository repository = new BookingRepository();
        CancellationService cancellationService =
                new CancellationService(repository, inventory);

        // -------------------- Simulate Bookings --------------------
        String room1 = inventory.allocateRoom("Deluxe");
        Reservation r1 = new Reservation("R101", "Alice", "Deluxe", room1);
        repository.save(r1);

        String room2 = inventory.allocateRoom("Standard");
        Reservation r2 = new Reservation("R102", "Bob", "Standard", room2);
        repository.save(r2);

        System.out.println("Initial State:");
        inventory.printInventory();

        // -------------------- Cancellation Flow --------------------
        System.out.println("\nCancelling R101...");
        cancellationService.cancelBooking("R101");

        System.out.println("\nCancelling R101 again...");
        cancellationService.cancelBooking("R101");

        System.out.println("\nCancelling invalid ID...");
        cancellationService.cancelBooking("R999");

        // -------------------- Final State --------------------
        System.out.println("\nFinal State:");
        inventory.printInventory();
        inventory.printRollbackStack();
    }
}