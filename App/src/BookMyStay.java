public class BookMyStay {

    // Abstract Room class
    static abstract class Room {
        private String roomType;
        private int beds;
        private int size;
        private double price;

        public Room(String roomType, int beds, int size, double price) {
            this.roomType = roomType;
            this.beds = beds;
            this.size = size;
            this.price = price;
        }

        public void displayRoomDetails() {
            System.out.println("Room Type: " + roomType);
            System.out.println("Beds: " + beds);
            System.out.println("Room Size: " + size + " sq ft");
            System.out.println("Price per Night: $" + price);
        }
    }

    // Single Room class
    static class SingleRoom extends Room {
        public SingleRoom() {
            super("Single Room", 1, 200, 80);
        }
    }

    // Double Room class
    static class DoubleRoom extends Room {
        public DoubleRoom() {
            super("Double Room", 2, 350, 120);
        }
    }

    // Suite Room class
    static class SuiteRoom extends Room {
        public SuiteRoom() {
            super("Suite Room", 3, 500, 250);
        }
    }

    // Main method
    public static void main(String[] args) {

        int singleRoomAvailable = 5;
        int doubleRoomAvailable = 3;
        int suiteRoomAvailable = 2;

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("===== BookMyStay Hotel Rooms =====\n");

        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleRoomAvailable);
        System.out.println("----------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleRoomAvailable);
        System.out.println("----------------------------------");

        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteRoomAvailable);
        System.out.println("----------------------------------");

        System.out.println("\nThank you for using BookMyStay!");
    }
}