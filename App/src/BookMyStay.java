import java.util.HashMap;
import java.util.Map;

public class BookMyStay {

    // ---------------- Room Domain Model ----------------
    static abstract class Room {
        private String type;
        private int beds;
        private int size;
        private double price;

        public Room(String type, int beds, int size, double price) {
            this.type = type;
            this.beds = beds;
            this.size = size;
            this.price = price;
        }

        public String getType() {
            return type;
        }

        public void displayDetails() {
            System.out.println("Room Type: " + type);
            System.out.println("Beds: " + beds);
            System.out.println("Size: " + size + " sq ft");
            System.out.println("Price per night: $" + price);
        }
    }

    // ---------------- Concrete Room Types ----------------
    static class SingleRoom extends Room {
        public SingleRoom() { super("Single", 1, 200, 80); }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() { super("Double", 2, 350, 120); }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() { super("Suite", 3, 500, 250); }
    }

    // ---------------- Inventory Management ----------------
    static class RoomInventory {
        private HashMap<String, Integer> inventory;

        public RoomInventory() {
            inventory = new HashMap<>();
            inventory.put("Single", 5);
            inventory.put("Double", 3);
            inventory.put("Suite", 2);
        }

        // Read-only access
        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        // Controlled updates (for booking)
        public void updateAvailability(String roomType, int change) {
            int current = inventory.getOrDefault(roomType, 0);
            inventory.put(roomType, current + change);
        }
    }

    // ---------------- Search Service (Read-only) ----------------
    static class SearchService {
        private RoomInventory inventory;

        public SearchService(RoomInventory inventory) {
            this.inventory = inventory;
        }

        public void displayAvailableRooms(Room[] rooms) {
            System.out.println("===== Available Rooms =====");
            boolean anyAvailable = false;

            for (Room room : rooms) {
                int available = inventory.getAvailability(room.getType());
                if (available > 0) {
                    room.displayDetails();
                    System.out.println("Available Rooms: " + available);
                    System.out.println("----------------------------------");
                    anyAvailable = true;
                }
            }

            if (!anyAvailable) {
                System.out.println("No rooms available at the moment.");
            }
        }
    }

    // ---------------- Application Entry ----------------
    public static void main(String[] args) {

        // Initialize rooms
        Room[] rooms = { new SingleRoom(), new DoubleRoom(), new SuiteRoom() };

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        SearchService searchService = new SearchService(inventory);

        // Guest performs a search
        System.out.println("Guest searches for available rooms:\n");
        searchService.displayAvailableRooms(rooms);

        // Example: Booking a Single Room (mutates inventory)
        System.out.println("\nBooking a Single Room...");
        inventory.updateAvailability("Single", -1);

        // Guest searches again after booking
        System.out.println("\nGuest searches again after booking:\n");
        searchService.displayAvailableRooms(rooms);
    }
}