# 🏠 Airbnb Clone — Property Rental Backend System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-007396?style=for-the-badge&logo=java&logoColor=white)
![Eclipse](https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=eclipse&logoColor=white)
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge)

---

## 📌 About The Project

A fully functional **Property Rental Backend System** inspired by Airbnb, built using **Core Java and MySQL with JDBC**.

This project demonstrates real-world backend development concepts including:
- Proper **MVC-style package structure**
- **MySQL database integration** via JDBC (data persists across sessions)
- **PreparedStatements** to prevent SQL injection
- **SQL JOINs, LIKE, BETWEEN, ORDER BY, SUM, COUNT** queries
- Complete **input validation** and **exception handling**

> 💡 Upgraded from a basic ArrayList-based version to a full MySQL-connected backend with proper architecture.

---

## ✨ Features

| Feature | Description |
|---|---|
| ✅ User Management | Add users with duplicate email validation |
| ✅ Property Management | Add properties with location and price |
| ✅ Book Property | Book available properties with real-time status update |
| ✅ Cancel Booking | Cancel bookings and auto-release property |
| ✅ View All Bookings | See all bookings with user and property details |
| ✅ Bookings by User | Filter bookings by specific user using SQL JOIN |
| ✅ Search by Location | Partial search using SQL LIKE query |
| ✅ Search by Price Range | Filter available properties using SQL BETWEEN |
| ✅ Sort by Price | Sort all properties low to high using ORDER BY |
| ✅ Revenue Report | Total revenue and booking count using SQL SUM + COUNT |
| ✅ Input Validation | Handles invalid inputs without crashing |

---

## 🗂️ Project Structure

```
AirbnbClone/
├── src/
│   └── com/
│       └── airbnb/
│           ├── model/
│           │   ├── User.java          → User entity
│           │   ├── Property.java      → Property entity
│           │   └── Booking.java       → Booking entity
│           ├── repository/
│           │   ├── UserRepository.java      → User DB operations
│           │   ├── PropertyRepository.java  → Property DB operations
│           │   └── BookingRepository.java   → Booking DB operations
│           ├── util/
│           │   └── DBConnection.java   → MySQL JDBC connection
│           └── main/
│               └── Main.java           → Application entry point
├── database/
│   └── schema.sql                      → MySQL table creation script
└── README.md
```

---

## 🛠️ Tech Stack

- **Language:** Java (Core Java, Java 8+)
- **Database:** MySQL 8.0
- **Connectivity:** JDBC (Java Database Connectivity)
- **IDE:** Eclipse
- **Version Control:** Git & GitHub

---

## ⚙️ Setup & Installation

### Prerequisites
- Java JDK 8 or above
- MySQL 8.0
- Eclipse IDE
- MySQL Connector JAR (`mysql-connector-j-9.x.x.jar`)

---

### Step 1 — Clone the Repository
```bash
git clone https://github.com/Madhumitha-divate/airbnb-clone-java.git
cd airbnb-clone-java
```

### Step 2 — Setup Database
Open **MySQL Workbench** and run:
```bash
source database/schema.sql
```
Or copy-paste the contents of `database/schema.sql` into MySQL Workbench and execute.

### Step 3 — Configure DB Connection
Open `src/com/airbnb/util/DBConnection.java` and update:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/airbnb_db";
private static final String USER     = "root";
private static final String PASSWORD = "your_mysql_password"; // ← update this
```

### Step 4 — Add MySQL JAR to Eclipse
1. Right click project → **Build Path** → **Configure Build Path**
2. Click **Libraries** → **Add External JARs**
3. Select `mysql-connector-j-9.x.x.jar` → Click **Apply and Close**

### Step 5 — Run
Right click `Main.java` → **Run As** → **Java Application**

---

## 🗄️ Database Schema

```sql
CREATE DATABASE IF NOT EXISTS airbnb_db;
USE airbnb_db;

CREATE TABLE users (
    id    INT PRIMARY KEY AUTO_INCREMENT,
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE properties (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    location     VARCHAR(150) NOT NULL,
    price        DOUBLE NOT NULL,
    is_available BOOLEAN DEFAULT TRUE
);

CREATE TABLE bookings (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    user_id      INT NOT NULL,
    property_id  INT NOT NULL,
    booking_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (user_id)     REFERENCES users(id),
    FOREIGN KEY (property_id) REFERENCES properties(id)
);
```

---

## 📸 Sample Output

```
==========================================
  🏠 Welcome to Airbnb Clone System      
==========================================

========== MAIN MENU ==========
1.  Add User
2.  View All Users
3.  Add Property
4.  View All Properties
5.  Book Property
6.  Cancel Booking
7.  View All Bookings
8.  View Bookings by User
9.  Search Property by Location
10. Search Property by Price Range
11. Sort Properties by Price
12. Total Revenue Report
13. Exit

--- Book a Property ---
Enter User ID     : 1
Enter Property ID : 3
✅ Booking confirmed!
🎉 Property booked successfully for Madhu!

================================
      TOTAL REVENUE REPORT      
================================
💰 Total Revenue  : Rs.1950000.00
📋 Total Bookings : 5
================================
```

---

## 🔑 Key Concepts Demonstrated

- **JDBC Connection Pooling** — try-with-resources for safe connection handling
- **PreparedStatement** — prevents SQL injection attacks
- **SQL JOINs** — fetching bookings with user and property details
- **SQL Aggregation** — SUM, COUNT for revenue reporting
- **Exception Handling** — graceful error messages instead of crashes
- **Input Validation** — validates email format, empty fields, price range

---

## 🚀 Future Improvements

- [ ] Spring Boot REST API version
- [ ] JWT Authentication
- [ ] Date-based booking (check-in / check-out)
- [ ] Admin dashboard
- [ ] Unit Testing with JUnit

---

## 👩‍💻 Author

**Madhumitha Divate**
- 🔗 [LinkedIn](https://linkedin.com/in/madhumitha-divate)
- 💻 [GitHub](https://github.com/Madhumitha-divate)
- 📧 madhudivate009@gmail.com

---

## ⭐ If you found this project helpful, please give it a star!
