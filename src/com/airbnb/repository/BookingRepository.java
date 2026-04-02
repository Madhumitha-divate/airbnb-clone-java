package com.airbnb.repository;

import com.airbnb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    // ✅ Add Booking
    public void addBooking(int userId, int propertyId) {
        String sql = "INSERT INTO bookings (user_id, property_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, propertyId);
            ps.executeUpdate();
            System.out.println("✅ Booking confirmed!");

        } catch (SQLException e) {
            System.out.println("❌ Error adding booking: " + e.getMessage());
        }
    }

    // ✅ View All Bookings
    public List<String> getAllBookings() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT b.id, u.name, p.location, p.price, b.booking_date " +
                     "FROM bookings b " +
                     "JOIN users u ON b.user_id = u.id " +
                     "JOIN properties p ON b.property_id = p.id";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(
                    "Booking#" + rs.getInt("id") +
                    " | User: "     + rs.getString("name") +
                    " | Property: " + rs.getString("location") +
                    " | Price: Rs." + rs.getDouble("price") +
                    " | Date: "     + rs.getDate("booking_date")
                );
            }

            if (list.isEmpty()) {
                System.out.println("❌ No bookings found.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching bookings: " + e.getMessage());
        }
        return list;
    }

    // ✅ NEW — View Bookings by User ID
    public void getBookingsByUser(int userId) {
        String sql = "SELECT b.id, p.location, p.price, b.booking_date " +
                     "FROM bookings b " +
                     "JOIN properties p ON b.property_id = p.id " +
                     "WHERE b.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    "Booking#" + rs.getInt("id") +
                    " | Property: " + rs.getString("location") +
                    " | Price: Rs." + rs.getDouble("price") +
                    " | Date: "     + rs.getDate("booking_date")
                );
            }

            if (!found) {
                System.out.println("❌ No bookings found for this user.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // ✅ Cancel Booking
    public void cancelBooking(int bookingId, PropertyRepository propertyRepo) {
        // Step 1 - Get property ID from booking
        String getPropertySql = "SELECT property_id FROM bookings WHERE id = ?";
        String deleteSql      = "DELETE FROM bookings WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(getPropertySql);
             PreparedStatement ps2 = conn.prepareStatement(deleteSql)) {

            ps1.setInt(1, bookingId);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                int propertyId = rs.getInt("property_id");

                // Step 2 - Delete booking
                ps2.setInt(1, bookingId);
                ps2.executeUpdate();

                // Step 3 - Mark property as available again
                propertyRepo.updateAvailability(propertyId, true);
                System.out.println("✅ Booking cancelled! Property is now available.");

            } else {
                System.out.println("❌ Booking ID not found!");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error cancelling booking: " + e.getMessage());
        }
    }
}