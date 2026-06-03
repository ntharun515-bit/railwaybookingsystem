# 🚂 RailConnect — Railway Ticket Booking System

A full-stack railway reservation web application built with **Java 21**, **Spring Boot 3**, **MySQL 8**, and **Bootstrap 5**.

## Features
- ✅ User Registration & JWT Authentication
- ✅ Role-Based Access Control (User / Admin)
- ✅ Train Search by Route (10 Stations, 15 Trains)
- ✅ Multi-Passenger Booking (up to 6)
- ✅ Payment Simulation (UPI, Credit/Debit Card, Net Banking)
- ✅ PDF Ticket Generation with QR Code
- ✅ Booking Cancellation with Auto Seat Release
- ✅ Admin Dashboard API
- ✅ Responsive Bootstrap 5 UI

## Tech Stack
| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3, Spring Security, Spring Data JPA |
| Database | MySQL 8 |
| Frontend | HTML5, CSS3, JavaScript, Bootstrap 5, Font Awesome |
| PDF | iText 5 |
| QR Code | Google ZXing |
| Auth | JWT (jjwt) |
| Build | Maven |

## How to Run

### Prerequisites
- Java 21
- MySQL 8 (running on port 3306)
- Maven 3.9+

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/ntharun515-bit/railwaybookingsystem.git
   cd railwaybookingsystem
   ```
2. Update `src/main/resources/application.properties` with your MySQL credentials.
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Open browser: **http://localhost:8080**

### Default Admin Account
- **Username:** `admin`
- **Password:** `admin123`

## API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register user |
| POST | `/api/auth/login` | Login (JWT) |
| GET | `/api/trains/search?source=NDLS&destination=MMCT` | Search trains |
| POST | `/api/bookings` | Create booking |
| POST | `/api/payments` | Process payment |
| GET | `/api/tickets/download/{bookingId}` | Download PDF ticket |
| PUT | `/api/bookings/cancel/{id}` | Cancel booking |

## License
This project is for educational purposes.
