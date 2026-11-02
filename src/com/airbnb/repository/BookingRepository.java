package com.airbnb.repository;

import com.airbnb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    // Book a property in ONE transaction.
    // The conditional UPDATE only succeeds for the first buyer, because MySQL
    // locks the row; a second buyer updates 0 rows and loses cleanly.
    // Returns true if the booking was saved, false otherwise.
    public boolean bookProperty(int userId, int propertyId) {
        String lockSql   = "UPDATE properties SET is_available = false " +
                           "WHERE id = ? AND is_available = true";
        String insertSql = "INSERT INTO bookings (user_id, property_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement lock = conn.prepareStatement(lockSql);
                 PreparedStatement insert = conn.prepareStatement(insertSql)) {

                lock.setInt(1, propertyId);
                if (lock.executeUpdate() == 0) {   // someone else got it first
                    conn.rollback();
                    return false;
                }

                insert.setInt(1, userId);
                insert.setInt(2, propertyId);
                insert.executeUpdate();

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error booking: " + e.getMessage());
            return false;
        }
    }

    // Cancel a booking in ONE transaction:
    // delete the booking and free the property together, or neither.
    public boolean cancelBooking(int bookingId) {
        String findSql   = "SELECT property_id FROM bookings WHERE id = ? FOR UPDATE";
        String deleteSql = "DELETE FROM bookings WHERE id = ?";
        String freeSql   = "UPDATE properties SET is_available = true WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement find = conn.prepareStatement(findSql);
                 PreparedStatement delete = conn.prepareStatement(deleteSql);
                 PreparedStatement free = conn.prepareStatement(freeSql)) {

                find.setInt(1, bookingId);
                int propertyId;
                try (ResultSet rs = find.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        System.out.println("❌ Booking ID not found!");
                        return false;
                    }
                    propertyId = rs.getInt("property_id");
                }

                delete.setInt(1, bookingId);
                delete.executeUpdate();

                free.setInt(1, propertyId);
                free.executeUpdate();

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error cancelling booking: " + e.getMessage());
            return false;
        }
    }

    // View all bookings (Main prints the "no bookings" message)
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

        } catch (SQLException e) {
            System.out.println("❌ Error fetching bookings: " + e.getMessage());
        }
        return list;
    }

    // View bookings of one user
    public void getBookingsByUser(int userId) {
        String sql = "SELECT b.id, p.location, p.price, b.booking_date " +
                     "FROM bookings b " +
                     "JOIN properties p ON b.property_id = p.id " +
                     "WHERE b.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {

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
            }

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
