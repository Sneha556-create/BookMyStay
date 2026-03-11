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
        public SingleRoom() {
            super("Single", 1, 200, 80);
        }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() {
            super("Double", 2, 350, 120);
        }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() {
            super("Suite", 3, 500, 250);
        }
    }

    // ---------------- Inventory Management ----------------
    static class RoomInventory {

        private HashMap<String, Integer> inventory;

        public RoomInventory() {
            inventory = new HashMap<>();

            // Initialize inventory
            inventory.put("Single", 5);
            inventory.put("Double", 3);
            inventory.put("Suite", 2);
        }

        // Get availability
        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        // Update availability
        public void updateAvailability(String roomType, int change) {
            int current = inventory.getOrDefault(roomType, 0);
            inventory.put(roomType, current + change);
        }

        // Display full inventory
        public void displayInventory() {
            System.out.println("===== Current Room Inventory =====");

            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + " Rooms Available: " + entry.getValue());
            }

            System.out.println("----------------------------------");
        }
    }

    // ---------------- Application Entry ----------------
    public static void main(String[] args) {

        // Initialize rooms
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        System.out.println("===== BookMyStay Hotel Rooms =====\n");

        single.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(single.getType()));
        System.out.println("----------------------------------");

        doubleRoom.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(doubleRoom.getType()));
        System.out.println("----------------------------------");

        suite.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(suite.getType()));
        System.out.println("----------------------------------");

        // Example inventory update
        System.out.println("\nBooking a Single Room...");
        inventory.updateAvailability("Single", -1);

        System.out.println("\nUpdated Inventory:");
        inventory.displayInventory();
    }
}