import java.io.*;
import java.util.*;

// -------------------- Reservation --------------------
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType;
    }
}

// -------------------- System State --------------------
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// -------------------- Inventory Manager --------------------
class InventoryManager {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryManager() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    public void printInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// -------------------- Booking History --------------------
class BookingHistory {

    private List<Reservation> bookings = new ArrayList<>();

    public void addBooking(Reservation r) {
        bookings.add(r);
    }

    public List<Reservation> getBookings() {
        return bookings;
    }

    public void setBookings(List<Reservation> bookings) {
        this.bookings = bookings;
    }

    public void printBookings() {
        System.out.println("\nBookings:");
        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }
}

// -------------------- Persistence Service --------------------
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save system state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\n✅ System state saved successfully");

        } catch (IOException e) {
            System.out.println("\n⚠️ Error saving system state: " + e.getMessage());
        }
    }

    // Load system state
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("✅ System state loaded successfully");
            return (SystemState) ois.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("⚠️ No previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("⚠️ Corrupted data. Starting with safe defaults.");
        }

        return null;
    }
}

// -------------------- Main Application --------------------
public class BookMyStay {

    public static void main(String[] args) {

        InventoryManager inventoryManager = new InventoryManager();
        BookingHistory bookingHistory = new BookingHistory();

        // -------------------- SYSTEM STARTUP --------------------
        System.out.println("🔄 Starting system...");

        SystemState loadedState = PersistenceService.load();

        if (loadedState != null) {
            inventoryManager.setInventory(loadedState.inventory);
            bookingHistory.setBookings(loadedState.bookings);
        }

        inventoryManager.printInventory();
        bookingHistory.printBookings();

        // -------------------- SIMULATE NEW BOOKINGS --------------------
        System.out.println("\n📌 Adding new bookings...");

        Reservation r1 = new Reservation("R101", "Alice", "Deluxe");
        Reservation r2 = new Reservation("R102", "Bob", "Standard");

        bookingHistory.addBooking(r1);
        bookingHistory.addBooking(r2);

        // Update inventory manually (simulate booking)
        Map<String, Integer> inv = inventoryManager.getInventory();
        inv.put("Deluxe", inv.get("Deluxe") - 1);
        inv.put("Standard", inv.get("Standard") - 1);

        inventoryManager.printInventory();
        bookingHistory.printBookings();

        // -------------------- SYSTEM SHUTDOWN --------------------
        System.out.println("\n💾 Saving system state...");

        SystemState state = new SystemState(
                inventoryManager.getInventory(),
                bookingHistory.getBookings()
        );

        PersistenceService.save(state);

        System.out.println("\n🔚 Shutdown complete. Restart app to test recovery.");
    }
}