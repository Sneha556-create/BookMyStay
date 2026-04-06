import java.util.*;

// -------------------- Add-On Service --------------------
class AddOnService {
    private String serviceId;
    private String serviceName;
    private double cost;

    public AddOnService(String serviceId, String serviceName, double cost) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// -------------------- Reservation --------------------
class Reservation {
    private String reservationId;
    private String guestName;

    public Reservation(String reservationId, String guestName) {
        this.reservationId = reservationId;
        this.guestName = guestName;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }
}

// -------------------- Add-On Service Manager --------------------
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> reservationServicesMap = new HashMap<>();

    // Add service
    public void addService(String reservationId, AddOnService service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get services
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost
    public double calculateTotalServiceCost(String reservationId) {
        double total = 0;
        for (AddOnService service : getServices(reservationId)) {
            total += service.getCost();
        }
        return total;
    }
}

// -------------------- Main Class --------------------
public class BookMyStay {

    public static void main(String[] args) {

        // Step 1: Create Reservation
        Reservation reservation = new Reservation("R101", "John Doe");

        // Step 2: Create Add-On Services
        AddOnService wifi = new AddOnService("S1", "WiFi", 500);
        AddOnService breakfast = new AddOnService("S2", "Breakfast", 800);
        AddOnService airportPickup = new AddOnService("S3", "Airport Pickup", 1500);

        // Step 3: Service Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Step 4: Guest selects services
        manager.addService(reservation.getReservationId(), wifi);
        manager.addService(reservation.getReservationId(), breakfast);
        manager.addService(reservation.getReservationId(), airportPickup);

        // Step 5: Display selected services
        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println("Guest Name: " + reservation.getGuestName());
        System.out.println("\nSelected Add-On Services:");

        List<AddOnService> services = manager.getServices(reservation.getReservationId());
        for (AddOnService service : services) {
            System.out.println("- " + service);
        }

        // Step 6: Calculate total cost
        double totalCost = manager.calculateTotalServiceCost(reservation.getReservationId());
        System.out.println("\nTotal Add-On Cost: ₹" + totalCost);

        // Step 7: Core booking remains unchanged (concept demonstration)
        System.out.println("\n[Core booking & inventory remain unaffected]");
    }
}