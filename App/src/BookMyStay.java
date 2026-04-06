import java.util.*;

// -------------------- Custom Exceptions --------------------
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class InsufficientInventoryException extends Exception {
    public InsufficientInventoryException(String message) {
        super(message);
    }
}

// -------------------- Reservation --------------------
class Reservation {
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
                ", Room Type: " + roomType;
    }
}

// -------------------- Inventory Manager --------------------
class InventoryManager {

    private Map<String, Integer> roomInventory = new HashMap<>();

    public InventoryManager() {
        roomInventory.put("Standard", 2);
        roomInventory.put("Deluxe", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return roomInventory.containsKey(roomType);
    }

    public int getAvailableRooms(String roomType) {
        return roomInventory.getOrDefault(roomType, 0);
    }

    public void reserveRoom(String roomType) throws InsufficientInventoryException {
        int available = getAvailableRooms(roomType);

        if (available <= 0) {
            throw new InsufficientInventoryException(
                    "No rooms available for type: " + roomType
            );
        }

        roomInventory.put(roomType, available - 1);
    }

    public void printInventory() {
        System.out.println("Current Inventory: " + roomInventory);
    }
}

// -------------------- Booking Validator --------------------
class BookingValidator {

    public static void validate(String guestName, String roomType, InventoryManager inventory)
            throws InvalidBookingException, InvalidRoomTypeException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        // Validate room type
        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidRoomTypeException("Invalid room type: " + roomType);
        }
    }
}

// -------------------- Booking Service --------------------
class BookingService {

    private InventoryManager inventory;

    public BookingService(InventoryManager inventory) {
        this.inventory = inventory;
    }

    public Reservation createBooking(String reservationId, String guestName, String roomType)
            throws InvalidBookingException, InvalidRoomTypeException, InsufficientInventoryException {

        // ✅ Fail-Fast Validation
        BookingValidator.validate(guestName, roomType, inventory);

        // ✅ Guard Inventory State
        inventory.reserveRoom(roomType);

        // ✅ Create reservation only after successful validation
        return new Reservation(reservationId, guestName, roomType);
    }
}

// -------------------- Main Application --------------------
public class BookMyStay {

    public static void main(String[] args) {

        InventoryManager inventory = new InventoryManager();
        BookingService bookingService = new BookingService(inventory);

        inventory.printInventory();

        // Test cases
        String[][] testBookings = {
                {"R101", "Alice", "Deluxe"},     // valid
                {"R102", "", "Standard"},        // invalid name
                {"R103", "Bob", "Suite"},       // invalid room type
                {"R104", "Charlie", "Deluxe"}   // no inventory
        };

        for (String[] input : testBookings) {
            try {
                System.out.println("\nProcessing booking...");

                Reservation r = bookingService.createBooking(
                        input[0], input[1], input[2]
                );

                System.out.println("Booking Successful: " + r);

            } catch (InvalidBookingException |
                     InvalidRoomTypeException |
                     InsufficientInventoryException e) {

                // ✅ Graceful failure handling
                System.out.println("Booking Failed: " + e.getMessage());
            }
        }

        System.out.println();
        inventory.printInventory();
        System.out.println("\n[System remains stable after handling errors]");
    }
}