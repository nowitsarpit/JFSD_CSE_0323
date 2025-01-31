import java.util.*;

class Guest {
    private String name;
    private int age;
    private String phoneNo;
    private int numberOfNights;
    private List<FoodItem> orderedItems;

    public Guest(String name, int age, String phoneNo, int numberOfNights) {
        this.name = name;
        this.age = age;
        this.phoneNo = phoneNo;
        this.numberOfNights = numberOfNights;
        this.orderedItems = new ArrayList<>();
    }

    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPhoneNo() { return phoneNo; }
    public int getNumberOfNights() { return numberOfNights; }
    public List<FoodItem> getOrderedItems() { return orderedItems; }

    public void addFoodItem(FoodItem item) {
        orderedItems.add(item);
    }
}

class FoodItem {
    private String name;
    private double price;
    private String category;

    public FoodItem(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
}

interface RoomOperations {
    void checkIn(Guest guest);
    double checkOut();
    String getRoomStatus();
}

abstract class Room implements RoomOperations {
    protected int roomNumber;
    protected double pricePerNight;
    protected Guest guest;
    protected boolean isOccupied;
    protected static final double TAX_RATE = 0.18; // 18% GST

    public Room(int roomNumber, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.isOccupied = false;
    }

    abstract String getRoomType();

    @Override
    public void checkIn(Guest guest) {
        if (isOccupied) {
            throw new IllegalStateException("Room is already occupied");
        }
        this.guest = guest;
        this.isOccupied = true;
    }

    @Override
    public double checkOut() {
        if (!isOccupied || guest == null) {
            throw new IllegalStateException("Room is not occupied");
        }

        double baseAmount = pricePerNight * guest.getNumberOfNights();
        double taxAmount = baseAmount * TAX_RATE;
        
        // Calculate food bill
        double foodBaseAmount = 0;
        for (FoodItem item : guest.getOrderedItems()) {
            foodBaseAmount += item.getPrice();
        }
        double foodTaxAmount = foodBaseAmount * TAX_RATE;
        
        double totalAmount = baseAmount + taxAmount + foodBaseAmount + foodTaxAmount;
        
        System.out.println("\nBILL DETAILS");
        System.out.println("============");
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Guest Name: " + guest.getName());
        System.out.println("Number of Nights: " + guest.getNumberOfNights());
        System.out.printf("Room Price per Night: Rs.%.2f%n", pricePerNight);
        System.out.printf("Room Base Amount: Rs.%.2f%n", baseAmount);
        System.out.printf("Room Tax Amount (18%%): Rs.%.2f%n", taxAmount);
        
        if (!guest.getOrderedItems().isEmpty()) {
            System.out.println("\nFood Orders:");
            for (FoodItem item : guest.getOrderedItems()) {
                System.out.printf("%s - Rs.%.2f%n", item.getName(), item.getPrice());
            }
            System.out.printf("Food Base Amount: Rs.%.2f%n", foodBaseAmount);
            System.out.printf("Food Tax Amount (18%%): Rs.%.2f%n", foodTaxAmount);
        }
        
        System.out.printf("\nTotal Amount: Rs.%.2f%n", totalAmount);
        
        this.guest = null;
        this.isOccupied = false;
        
        return totalAmount;
    }

    @Override
    public String getRoomStatus() {
        if (isOccupied) {
            return String.format("Room %d (%s): Occupied by %s | Age: %d | Phone: %s | Nights: %d | Price: Rs.%.2f/night",
                roomNumber, getRoomType(), guest.getName(), guest.getAge(), guest.getPhoneNo(), 
                guest.getNumberOfNights(), pricePerNight);
        }
        return String.format("Room %d (%s): Available - Price: Rs.%.2f/night",
            roomNumber, getRoomType(), pricePerNight);
    }

    public boolean isOccupied() { return isOccupied; }
    public int getRoomNumber() { return roomNumber; }
    public Guest getGuest() { return guest; }
}

class NonACRoom extends Room {
    public NonACRoom(int roomNumber) {
        super(roomNumber, 500.0); // Non-AC rooms cost Rs.500 per night
    }

    @Override
    String getRoomType() {
        return "Non-AC";
    }
}

class ACRoom extends Room {
    public ACRoom(int roomNumber) {
        super(roomNumber, 1000.0); // AC rooms cost Rs.1000 per night
    }

    @Override
    String getRoomType() {
        return "AC";
    }
}

class Restaurant {
    private final List<FoodItem> menu;

    public Restaurant() {
        menu = new ArrayList<>();
        
        // Starters
        menu.add(new FoodItem("Veg Spring Rolls", 200.0, "Starter"));
        menu.add(new FoodItem("Chicken Tikka", 300.0, "Starter"));
        menu.add(new FoodItem("Paneer Tikka", 250.0, "Starter"));
        
        // Main Course
        menu.add(new FoodItem("Butter Chicken", 400.0, "Main Course"));
        menu.add(new FoodItem("Paneer Butter Masala", 350.0, "Main Course"));
        menu.add(new FoodItem("Veg Biryani", 300.0, "Main Course"));
        
        // Desserts
        menu.add(new FoodItem("Gulab Jamun", 150.0, "Dessert"));
        menu.add(new FoodItem("Ice Cream", 100.0, "Dessert"));
        menu.add(new FoodItem("Rasmalai", 200.0, "Dessert"));
        
        // Water
        menu.add(new FoodItem("Mineral Water", 50.0, "Beverage"));
        menu.add(new FoodItem("Cold Drink", 70.0, "Beverage"));
        menu.add(new FoodItem("Tea/Coffee", 20.0, "Beverage"));
    }

    public void displayMenu() {
        System.out.println("\nRestaurant Menu");
        System.out.println("===============");
        
        System.out.println("\nStarters:");
        displayCategoryItems("Starter");
        
        System.out.println("\nMain Course:");
        displayCategoryItems("Main Course");
        
        System.out.println("\nDesserts:");
        displayCategoryItems("Dessert");
        
        System.out.println("\nBeverages:");
        displayCategoryItems("Beverage");
    }

    private void displayCategoryItems(String category) {
        int index = 1;
        for (FoodItem item : menu) {
            if (item.getCategory().equals(category)) {
                System.out.printf("%d. %s - Rs.%.2f%n", index++, item.getName(), item.getPrice());
            }
        }
    }

    public FoodItem getItem(String category, int index) {
        int count = 0;
        for (FoodItem item : menu) {
            if (item.getCategory().equals(category)) {
                count++;
                if (count == index) {
                    return item;
                }
            }
        }
        return null;
    }
}

class User {
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

class Hotel {
    private final Room[] rooms;
    private final User[] users;
    private final Restaurant restaurant;
    private final Random random;
    private static final int TOTAL_ROOMS = 10;

    public Hotel() {
        rooms = new Room[TOTAL_ROOMS];
        users = new User[]{new User("admin", "admin123")};
        restaurant = new Restaurant();
        random = new Random();

        // Initialize 5 Non-AC rooms (101-105)
        for (int i = 0; i < 5; i++) {
            rooms[i] = new NonACRoom(101 + i);
        }
        // Initialize 5 AC rooms (201-205)
        for (int i = 5; i < TOTAL_ROOMS; i++) {
            rooms[i] = new ACRoom(201 + (i-5));
        }
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.authenticate(username, password)) {
                return true;
            }
        }
        return false;
    }

    public void displayAllRooms() {
        System.out.println("\nHotel Room Status:");
        System.out.println("==================");
        for (Room room : rooms) {
            if (room != null) {
                System.out.println(room.getRoomStatus());
            }
        }
    }

    public Room getRandomAvailableRoom(String type) {
        Room[] availableRooms = new Room[TOTAL_ROOMS];
        int count = 0;
        
        for (Room room : rooms) {
            if (room != null && !room.isOccupied() && 
                ((type.equals("AC") && room instanceof ACRoom) ||
                 (type.equals("NonAC") && room instanceof NonACRoom))) {
                availableRooms[count++] = room;
            }
        }
        
        if (count == 0) return null;
        return availableRooms[random.nextInt(count)];
    }

    public void checkInGuest() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\nSelect Room Type:");
        System.out.println("1. AC Room (Rs.1000 per night)");
        System.out.println("2. Non-AC Room (Rs.500 per night)");
        System.out.print("Enter choice (1/2): ");
        
        int roomChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        String roomType = (roomChoice == 1) ? "AC" : "NonAC";
        Room room = getRandomAvailableRoom(roomType);
        
        if (room == null) {
            System.out.println("No available " + roomType + " rooms!");
            scanner.close();
            return;
        }

        try {
            System.out.println("\nEnter guest details:");
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty!");
                return;
            }
            
            System.out.print("Age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (age < 18) {
                System.out.println("Guest must be 18 years or older!");
                return;    
            }
            
            System.out.print("Phone Number: ");
            String phoneNo = scanner.nextLine().trim();
            if (phoneNo.isEmpty()) {
                System.out.println("Phone number cannot be empty!");
                return;
            }
            
            System.out.print("Number of Nights: ");
            int nights = scanner.nextInt();
            if (nights <= 0) {
                System.out.println("Number of nights must be positive!");
                return;
            }

            Guest guest = new Guest(name, age, phoneNo, nights);
            room.checkIn(guest);
            System.out.println("\nCheck-in successful!");
            System.out.println("Allocated Room: " + room.getRoomNumber());
        } catch (Exception e) {
            System.out.println("Error during check-in: " + e.getMessage());
        }
    }

    public void orderFood(int roomNumber) {
        Room targetRoom = null;
        for (Room room : rooms) {
            if (room != null && room.getRoomNumber() == roomNumber) {
                targetRoom = room;
                break;
            }
        }

        if (targetRoom == null || !targetRoom.isOccupied()) {
            System.out.println("Invalid room number or room is not occupied!");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        restaurant.displayMenu();
        Guest guest = targetRoom.getGuest();

        while (true) {
            System.out.println("\nOrder Menu:");
            System.out.println("1. Order Starter");
            System.out.println("2. Order Main Course");
            System.out.println("3. Order Dessert");
            System.out.println("4. Order Mineral Water");
            System.out.println("5. Finish Ordering");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            if (choice == 5) break;

            String category;
            switch (choice) {
                case 1: category = "Starter"; break;
                case 2: category = "Main Course"; break;
                case 3: category = "Dessert"; break;
                case 4: category = "Beverage"; break;
                default: continue;
            }

            System.out.print("Enter item number: ");
            int itemIndex = scanner.nextInt();
            
            FoodItem item = restaurant.getItem(category, itemIndex);
            if (item != null) {
                guest.addFoodItem(item);
                System.out.printf("%s added to order - Rs.%.2f%n", item.getName(), item.getPrice());
            } else {
                System.out.println("Invalid item selection!");
            }
        }
    }

    public void checkOutGuest(int roomNumber) {
        try {
            for (Room room : rooms) {
                if (room != null && room.getRoomNumber() == roomNumber) {
                    if (room.isOccupied()) {
                        room.checkOut();
                        System.out.println("Check-out successful!");
                    } else {
                        System.out.println("Room " + roomNumber + " is already vacant!");
                    }
                    return;
                }
            }
            System.out.println("Room " + roomNumber + " not found!");
        } catch (Exception e) {
            System.out.println("Error during check-out: " + e.getMessage());
        }
    }
}

public class HotelManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel();
        
        System.out.println("Welcome to Hotel Baratie");
        System.out.println("=================================");
        
        // Login
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("\nUsername: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            loggedIn = hotel.login(username, password);
            if (!loggedIn) {
                System.out.println("Invalid username or password. Please try again.");
            }
        }

        // Main menu
        while (true) {
            System.out.println("\nMain Menu");
            System.out.println("1. Display Room Status");
            System.out.println("2. Check In Guest");
            System.out.println("3. Order Food");
            System.out.println("4. Check Out Guest");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    hotel.displayAllRooms();
                    break;
                case 2:
                    hotel.checkInGuest();
                    break;
                case 3:
                    System.out.print("Enter room number to order food: ");
                    int roomNumberForOrder = scanner.nextInt();
                    hotel.orderFood(roomNumberForOrder);
                    break;
                case 4:
                    System.out.print("Enter room number to check out: ");
                    int roomNumberForCheckout = scanner.nextInt();
                    hotel.checkOutGuest(roomNumberForCheckout);
                    break;
                case 5:
                    System.out.println("Thank you for using the Hotel Management System. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
//End of Project