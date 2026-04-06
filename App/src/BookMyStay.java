import java.util.*;

// -------------------- Reservation --------------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double price;

    public Reservation(String reservationId, String guestName, String roomType, double price) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.price = price;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Price: ₹" + price;
    }
}

// -------------------- Booking History --------------------
class BookingHistory {

    // List preserves insertion order
    private List<Reservation> confirmedBookings = new ArrayList<>();

    // Add confirmed booking
    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    // Get all bookings (read-only copy for safety)
    public List<Reservation> getAllBookings() {
        return new ArrayList<>(confirmedBookings);
    }
}

// -------------------- Booking Report Service --------------------
class BookingReportService {

    // Display all bookings
    public void printAllBookings(List<Reservation> bookings) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }

    // Total revenue
    public double calculateTotalRevenue(List<Reservation> bookings) {
        double total = 0;
        for (Reservation r : bookings) {
            total += r.getPrice();
        }
        return total;
    }

    // Count bookings by room type
    public Map<String, Integer> countByRoomType(List<Reservation> bookings) {
        Map<String, Integer> report = new HashMap<>();

        for (Reservation r : bookings) {
            report.put(r.getRoomType(),
                    report.getOrDefault(r.getRoomType(), 0) + 1);
        }

        return report;
    }
}

// -------------------- Main Application --------------------
public class BookMyStay {

    public static void main(String[] args) {

        // Step 1: Create Booking History
        BookingHistory history = new BookingHistory();

        // Step 2: Simulate confirmed bookings
        Reservation r1 = new Reservation("R101", "Alice", "Deluxe", 3000);
        Reservation r2 = new Reservation("R102", "Bob", "Standard", 2000);
        Reservation r3 = new Reservation("R103", "Charlie", "Deluxe", 3000);

        // Step 3: Store confirmed bookings
        history.addBooking(r1);
        history.addBooking(r2);
        history.addBooking(r3);

        // Step 4: Admin retrieves booking history
        List<Reservation> bookings = history.getAllBookings();

        // Step 5: Reporting
        BookingReportService reportService = new BookingReportService();

        // Print all bookings
        reportService.printAllBookings(bookings);

        // Total revenue
        double revenue = reportService.calculateTotalRevenue(bookings);
        System.out.println("\nTotal Revenue: ₹" + revenue);

        // Room type report
        Map<String, Integer> roomReport = reportService.countByRoomType(bookings);
        System.out.println("\nBookings by Room Type:");
        for (Map.Entry<String, Integer> entry : roomReport.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Ensure history is unchanged
        System.out.println("\n[Booking history remains unchanged after reporting]");
    }
}