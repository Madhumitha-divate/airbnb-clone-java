# 🏠 Airbnb Clone — Booking Platform Backend

A fully functional property booking backend built with Java, 
Spring Boot, MySQL, and raw JDBC.

## 🔧 Tech Stack
- Java 17
- Spring Boot (MVC Architecture)
- MySQL + JDBC (no ORM)
- REST APIs
- Git

## 💡 Features
- User registration and authentication
- Property listing with location + price-range search
- Real-time booking and cancellation flows
- Double-booking prevention logic
- Revenue reporting using SQL JOINs + GROUP BY

## 🚀 How to Run
1. Clone the repo: git clone [your URL]
2. Set up MySQL — import schema from /db/schema.sql
3. Update DB credentials in application.properties
4. Run: mvn spring-boot:run

## 📐 Architecture
User → Controller → Service → Repository → MySQL
(Strict separation: business logic never touches DB directly)

## 📬 Contact
LinkedIn:https://www.linkedin.com/in/madhumitha-divate/
