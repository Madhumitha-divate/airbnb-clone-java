package com.airbnb.repository;

import com.airbnb.model.Property;
import com.airbnb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyRepository {

    // ✅ Add Property
    public void addProperty(String location, double price) {
        String sql = "INSERT INTO properties (location, price, is_available) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, location);
            ps.setDouble(2, price);
            ps.setBoolean(3, true);
            ps.executeUpdate();
            System.out.println("✅ Property added successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Error adding property: " + e.getMessage());
        }
    }

    // ✅ Get All Properties
    public List<Property> getAllProperties() {
        List<Property> list = new ArrayList<>();
        String sql = "SELECT * FROM properties";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Property(
                    rs.getInt("id"),
                    rs.getString("location"),
                    rs.getDouble("price"),
                    rs.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching properties: " + e.getMessage());
        }
        return list;
    }

    // ✅ Get Property by ID
    public Property getPropertyById(int id) {
        String sql = "SELECT * FROM properties WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Property(
                    rs.getInt("id"),
                    rs.getString("location"),
                    rs.getDouble("price"),
                    rs.getBoolean("is_available")
                );
            } else {
                System.out.println("❌ Property not found!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        return null;
    }

    // ✅ Update Availability
    public void updateAvailability(int id, boolean isAvailable) {
        String sql = "UPDATE properties SET is_available = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, isAvailable);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error updating property: " + e.getMessage());
        }
    }

    // ✅ Search by Location
    public List<Property> searchByLocation(String location) {
        List<Property> list = new ArrayList<>();
        String sql = "SELECT * FROM properties WHERE LOWER(location) LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + location.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Property(
                    rs.getInt("id"),
                    rs.getString("location"),
                    rs.getDouble("price"),
                    rs.getBoolean("is_available")
                ));
            }

            if (list.isEmpty()) {
                System.out.println("❌ No properties found in this location.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error searching: " + e.getMessage());
        }
        return list;
    }

    // ✅ NEW — Search by Price Range
    public List<Property> searchByPriceRange(double minPrice, double maxPrice) {
        List<Property> list = new ArrayList<>();
        String sql = "SELECT * FROM properties WHERE price BETWEEN ? AND ? AND is_available = true";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Property(
                    rs.getInt("id"),
                    rs.getString("location"),
                    rs.getDouble("price"),
                    rs.getBoolean("is_available")
                ));
            }

            if (list.isEmpty()) {
                System.out.println("❌ No properties found in this price range.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error searching: " + e.getMessage());
        }
        return list;
    }

    // ✅ NEW — Sort by Price
    public List<Property> sortByPrice() {
        List<Property> list = new ArrayList<>();
        String sql = "SELECT * FROM properties ORDER BY price ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Property(
                    rs.getInt("id"),
                    rs.getString("location"),
                    rs.getDouble("price"),
                    rs.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error sorting: " + e.getMessage());
        }
        return list;
    }

    // ✅ NEW — Total Revenue Report
    public void getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(p.price), 0) as total_revenue, " +
                     "COUNT(b.id) as total_bookings " +
                     "FROM bookings b " +
                     "JOIN properties p ON b.property_id = p.id";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                double revenue = rs.getDouble("total_revenue");
                int bookings   = rs.getInt("total_bookings");

                System.out.println("================================");
                System.out.printf ("💰 Total Revenue  : Rs.%.2f%n", revenue);
                System.out.println("📋 Total Bookings : " + bookings);
                System.out.println("================================");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    
    }
}