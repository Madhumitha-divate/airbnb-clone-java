package com.airbnb.repository;
	import com.airbnb.model.User;
	import com.airbnb.util.DBConnection;

	import java.sql.*;
	import java.util.ArrayList;
	import java.util.List;

	public class UserRepository {

	    // ✅ Add User with duplicate email check
	    public boolean addUser(String name, String email) {
	        // Step 1 - Check if email already exists
	        String checkSql = "SELECT id FROM users WHERE email = ?";
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(checkSql)) {

	            ps.setString(1, email);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                System.out.println("❌ Email already registered! Use a different email.");
	                return false;
	            }

	        } catch (SQLException e) {
	            System.out.println("❌ Error checking email: " + e.getMessage());
	            return false;
	        }

	        // Step 2 - Insert user
	        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setString(1, name);
	            ps.setString(2, email);
	            ps.executeUpdate();
	            System.out.println("✅ User added successfully!");
	            return true;

	        } catch (SQLException e) {
	            System.out.println("❌ Error adding user: " + e.getMessage());
	            return false;
	        }
	    }

	    // ✅ Get all users
	    public List<User> getAllUsers() {
	        List<User> users = new ArrayList<>();
	        String sql = "SELECT * FROM users";
	        try (Connection conn = DBConnection.getConnection();
	             Statement st = conn.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {

	            while (rs.next()) {
	                users.add(new User(
	                    rs.getInt("id"),
	                    rs.getString("name"),
	                    rs.getString("email")
	                ));
	            }
	        } catch (SQLException e) {
	            System.out.println("❌ Error fetching users: " + e.getMessage());
	        }
	        return users;
	    }

	    // ✅ Get user by ID
	    public User getUserById(int id) {
	        String sql = "SELECT * FROM users WHERE id = ?";
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setInt(1, id);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                return new User(
	                    rs.getInt("id"),
	                    rs.getString("name"),
	                    rs.getString("email")
	                );
	            } else {
	                System.out.println("❌ User not found!");
	            }
	        } catch (SQLException e) {
	            System.out.println("❌ Error: " + e.getMessage());
	        }
	        return null;
	    }
	
}
