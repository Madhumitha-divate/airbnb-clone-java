package com.airbnb.main;

import com.airbnb.model.User;
import com.airbnb.model.Property;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.UserRepository;

import java.util.List;
import java.util.Scanner;

public class Main {

    static UserRepository     userRepo     = new UserRepository();
    static PropertyRepository propertyRepo = new PropertyRepository();
    static BookingRepository  bookingRepo  = new BookingRepository();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("  🏠 Welcome to Airbnb Clone System      ");
        System.out.println("==========================================");

        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1.  Add User");
            System.out.println("2.  View All Users");
            System.out.println("3.  Add Property");
            System.out.println("4.  View All Properties");
            System.out.println("5.  Book Property");
            System.out.println("6.  Cancel Booking");
            System.out.println("7.  View All Bookings");
            System.out.println("8.  View Bookings by User");
            System.out.println("9.  Search Property by Location");
            System.out.println("10. Search Property by Price Range");
            System.out.println("11. Sort Properties by Price");
            System.out.println("12. Total Revenue Report");
            System.out.println("13. Exit");
            System.out.print("\nEnter Choice: ");

            try {
                int choice = Integer.parseInt(sc.nextLine().trim());

                switch (choice) {
                    case 1  -> addUser();
                    case 2  -> viewAllUsers();
                    case 3  -> addProperty();
                    case 4  -> viewAllProperties();
                    case 5  -> bookProperty();
                    case 6  -> cancelBooking();
                    case 7  -> viewAllBookings();
                    case 8  -> viewBookingsByUser();
                    case 9  -> searchByLocation();
                    case 10 -> searchByPriceRange();
                    case 11 -> sortByPrice();
                    case 12 -> revenueReport();
                    case 13 -> {
                        System.out.println("\n👋 Thank you for using Airbnb Clone!");
                        System.out.println("==========================================");
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice! Enter 1-13.");
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a number.");
            }
        }
    }

    // ✅ 1. Add User
    static void addUser() {
        System.out.println("\n--- Add New User ---");
        System.out.print("Enter Name  : ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("❌ Name cannot be empty!");
            return;
        }

        System.out.print("Enter Email : ");
        String email = sc.nextLine().trim();

        if (email.isEmpty() || !email.contains("@")) {
            System.out.println("❌ Please enter a valid email!");
            return;
        }

        userRepo.addUser(name, email);
    }

    // ✅ 2. View All Users
    static void viewAllUsers() {
        System.out.println("\n--- All Users ---");
        List<User> users = userRepo.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("❌ No users found.");
        } else {
            users.forEach(System.out::println);
        }
    }

    // ✅ 3. Add Property
    static void addProperty() {
        System.out.println("\n--- Add New Property ---");
        System.out.print("Enter Location : ");
        String location = sc.nextLine().trim();

        if (location.isEmpty()) {
            System.out.println("❌ Location cannot be empty!");
            return;
        }

        System.out.print("Enter Price    : Rs.");
        try {
            double price = Double.parseDouble(sc.nextLine().trim());

            if (price <= 0) {
                System.out.println("❌ Price must be greater than 0!");
                return;
            }

            propertyRepo.addProperty(location, price);

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid price! Enter a number.");
        }
    }

    // ✅ 4. View All Properties
    static void viewAllProperties() {
        System.out.println("\n--- All Properties ---");
        List<Property> list = propertyRepo.getAllProperties();
        if (list.isEmpty()) {
            System.out.println("❌ No properties found.");
        } else {
            list.forEach(System.out::println);
        }
    }

    // ✅ 5. Book Property
    static void bookProperty() {
        System.out.println("\n--- Book a Property ---");
        try {
            System.out.print("Enter User ID     : ");
            int userId = Integer.parseInt(sc.nextLine().trim());

            User user = userRepo.getUserById(userId);
            if (user == null) return;

            System.out.print("Enter Property ID : ");
            int propertyId = Integer.parseInt(sc.nextLine().trim());

            Property property = propertyRepo.getPropertyById(propertyId);
            if (property == null) return;

            if (!property.isAvailable()) {
                System.out.println("❌ Sorry! This property is already booked.");
                return;
            }

            bookingRepo.addBooking(userId, propertyId);
            propertyRepo.updateAvailability(propertyId, false);
            System.out.println("🎉 Property booked successfully for " + user.getName() + "!");

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Enter a valid number.");
        }
    }

    // ✅ 6. Cancel Booking
    static void cancelBooking() {
        System.out.println("\n--- Cancel Booking ---");
        try {
            System.out.print("Enter Booking ID to cancel : ");
            int bookingId = Integer.parseInt(sc.nextLine().trim());
            bookingRepo.cancelBooking(bookingId, propertyRepo);

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Enter a valid number.");
        }
    }

    // ✅ 7. View All Bookings
    static void viewAllBookings() {
        System.out.println("\n--- All Bookings ---");
        List<String> bookings = bookingRepo.getAllBookings();
        if (bookings.isEmpty()) {
            System.out.println("❌ No bookings found.");
        } else {
            bookings.forEach(System.out::println);
        }
    }

    // ✅ 8. View Bookings by User
    static void viewBookingsByUser() {
        System.out.println("\n--- Bookings by User ---");
        try {
            System.out.print("Enter User ID : ");
            int userId = Integer.parseInt(sc.nextLine().trim());

            User user = userRepo.getUserById(userId);
            if (user == null) return;

            System.out.println("Bookings for " + user.getName() + ":");
            bookingRepo.getBookingsByUser(userId);

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input!");
        }
    }

    // ✅ 9. Search by Location
    static void searchByLocation() {
        System.out.println("\n--- Search by Location ---");
        System.out.print("Enter Location : ");
        String location = sc.nextLine().trim();

        if (location.isEmpty()) {
            System.out.println("❌ Location cannot be empty!");
            return;
        }

        List<Property> results = propertyRepo.searchByLocation(location);
        if (!results.isEmpty()) {
            results.forEach(System.out::println);
        }
    }

    // ✅ 10. Search by Price Range
    static void searchByPriceRange() {
        System.out.println("\n--- Search by Price Range ---");
        try {
            System.out.print("Enter Minimum Price : Rs.");
            double minPrice = Double.parseDouble(sc.nextLine().trim());

            System.out.print("Enter Maximum Price : Rs.");
            double maxPrice = Double.parseDouble(sc.nextLine().trim());

            if (minPrice > maxPrice) {
                System.out.println("❌ Minimum price cannot be greater than maximum price!");
                return;
            }

            List<Property> results = propertyRepo.searchByPriceRange(minPrice, maxPrice);
            if (!results.isEmpty()) {
                results.forEach(System.out::println);
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Enter a valid number.");
        }
    }

    // ✅ 11. Sort by Price
    static void sortByPrice() {
        System.out.println("\n--- Properties Sorted by Price (Low to High) ---");
        List<Property> list = propertyRepo.sortByPrice();
        if (list.isEmpty()) {
            System.out.println("❌ No properties found.");
        } else {
            list.forEach(System.out::println);
        }
    }

    // ✅ 12. Revenue Report
    static void revenueReport() {
        System.out.println("\n================================");
        System.out.println("      TOTAL REVENUE REPORT      ");
        System.out.println("================================");
        String sql = "SELECT COALESCE(SUM(p.price), 0) as total_revenue, " +
                     "COUNT(b.id) as total_bookings " +
                     "FROM bookings b " +
                     "JOIN properties p ON b.property_id = p.id";
        try (java.sql.Connection conn = com.airbnb.util.DBConnection.getConnection();
             java.sql.Statement st = conn.createStatement();
             java.sql.ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                double revenue = rs.getDouble("total_revenue");
                int total      = rs.getInt("total_bookings");
                System.out.printf("💰 Total Revenue  : Rs.%.2f%n", revenue);
                System.out.println("📋 Total Bookings : " + total);
            }
        } catch (java.sql.SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        System.out.println("================================");
    }

}