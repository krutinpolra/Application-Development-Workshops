package com.seneca.apd.finalproject.hotelreservation.DataBase;

import java.sql.*;

public class HotelDatabase {

    private static final String DB_URL = "jdbc:sqlite:hotel.db";

    static {
        createTablesIfNotExist();
        seedDummyData();
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private static void createTablesIfNotExist() {
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();

            // Admin table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Admin (
                    adminID INTEGER PRIMARY KEY AUTOINCREMENT,
                    userName TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL
                );
            """);

            // Guest table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Guest (
                    guestID INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    phone TEXT NOT NULL,
                    email TEXT NOT NULL,
                    address TEXT
                );
            """);

            // ✅ Updated Room table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Room (
                    roomID INTEGER PRIMARY KEY AUTOINCREMENT,
                    room_type TEXT NOT NULL,
                    numberOfBeds INTEGER NOT NULL,
                    price REAL NOT NULL,
                    capacity INTEGER NOT NULL,
                    status TEXT NOT NULL DEFAULT 'available',
                    image_url TEXT
                );
            """);

            // Reservation table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Reservation (
                    bookingID INTEGER PRIMARY KEY AUTOINCREMENT,
                    guestID INTEGER NOT NULL,
                    roomID INTEGER NOT NULL,
                    check_in_date TEXT,
                    check_out_date TEXT,
                    totalPrice REAL,
                    status TEXT DEFAULT 'booked',
                    FOREIGN KEY (guestID) REFERENCES Guest(guestID),
                    FOREIGN KEY (roomID) REFERENCES Room(roomID)
                );
            """);

            // Billing table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Billing (
                    billID INTEGER PRIMARY KEY AUTOINCREMENT,
                    bookingID INTEGER NOT NULL,
                    amount REAL,
                    tax REAL,
                    discount REAL DEFAULT 0,
                    totalAmount REAL,
                    FOREIGN KEY (bookingID) REFERENCES Reservation(bookingID)
                );
            """);

            // Feedback table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Feedback (
                    feedbackID INTEGER PRIMARY KEY AUTOINCREMENT,
                    guestID INTEGER NOT NULL,
                    bookingID INTEGER NOT NULL,
                    comments TEXT,
                    rating INTEGER,
                    FOREIGN KEY (guestID) REFERENCES Guest(guestID),
                    FOREIGN KEY (bookingID) REFERENCES Reservation(bookingID)
                );
            """);

            // Cancellation table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Cancellation (
                    cancellationID INTEGER PRIMARY KEY AUTOINCREMENT,
                    bookingID INTEGER NOT NULL,
                    reason TEXT,
                    cancelled_on TEXT,
                    FOREIGN KEY (bookingID) REFERENCES Reservation(bookingID)
                );
            """);

            System.out.println("✅ Hotel database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("❌ Database setup failed: " + e.getMessage());
        }
    }

    private static void seedDummyData() {
        try (Connection conn = connect()) {

            // Seed Admin
            String adminCheck = "SELECT COUNT(*) FROM Admin WHERE userName = 'krutin'";
            ResultSet rs = conn.createStatement().executeQuery(adminCheck);
            if (rs.next() && rs.getInt(1) == 0) {
                String insertAdmin = "INSERT INTO Admin (userName, password) VALUES ('krutin', '111'), ('priyank', '222')";
                conn.createStatement().executeUpdate(insertAdmin);
                System.out.println("✅ Dummy admin inserted: krutin / 111");
            }

            // Seed Rooms if none exist
            String roomCountQuery = "SELECT COUNT(*) FROM Room";
            rs = conn.createStatement().executeQuery(roomCountQuery);
            if (rs.next() && rs.getInt(1) == 0) {
                String insertRooms = """
                    INSERT INTO Room (room_type, numberOfBeds, price, capacity, status, image_url) VALUES
                    ('Single', 1, 90.0, 2, 'available', 'SingleRoom1.jpg'),
                    ('Single', 1, 95.0, 2, 'available', 'SingleRoom2.jpg'),
                    ('Single', 1, 92.5, 2, 'available', 'SingleRoom3.jpg'),
                    ('Single', 1, 98.0, 2, 'available', 'SingleRoom4.jpg'),
                    ('Double', 2, 120.0, 4, 'available', 'DoubleRoom1.jpg'),
                    ('Double', 2, 125.0, 4, 'available', 'DoubleRoom2.jpg'),
                    ('Double', 2, 122.0, 4, 'available', 'DoubleRoom3.jpg'),
                    ('Double', 2, 119.0, 4, 'available', 'DoubleRoom4.jpg'),
                    ('Deluxe', 2, 180.0, 4, 'available', 'DeluxeRoom1.jpg'),
                    ('Deluxe', 2, 185.0, 4, 'available', 'DeluxeRoom2.jpg'),
                    ('Deluxe', 2, 190.0, 4, 'available', 'DeluxeRoom3.jpg'),
                    ('Deluxe', 2, 195.0, 4, 'available', 'DeluxeRoom4.jpg'),
                    ('PENTHOUSE', 3, 250.0, 6, 'available', 'FamilyRoom1.jpg'),
                    ('PENTHOUSE', 3, 260.0, 6, 'available', 'FamilyRoom2.jpg'),
                    ('PENTHOUSE', 4, 2000.0, 6, 'available', 'panthouse1.jpg'),
                    ('PENTHOUSE', 4, 1950.0, 6, 'available', 'panthouse2.jpg'),
                    ('Single', 1, 99.0, 2, 'available', 'SingleRoom5.jpg'),
                    ('Single', 1, 91.0, 2, 'available', 'SingleRoom6.jpg'),
                    ('Single', 1, 93.0, 2, 'available', 'SingleRoom7.jpg'),
                    ('Double', 2, 121.0, 4, 'available', 'DoubleRoom5.jpg'),
                    ('Double', 2, 118.0, 4, 'available', 'DoubleRoom6.jpg'),
                    ('Deluxe', 2, 200.0, 4, 'available', 'DeluxeRoom5.jpg'),
                    ('Deluxe', 2, 210.0, 4, 'available', 'DeluxeRoom6.jpg'),
                    ('Deluxe', 2, 220.0, 4, 'available', 'DeluxeRoom7.jpg'),
                    ('PENTHOUSE', 4, 2300.0, 6, 'available', 'panthouse3.jpg'),
                    ('PENTHOUSE', 4, 2350.0, 6, 'available', 'panthouse4.jpg'),
                    ('PENTHOUSE', 4, 2400.0, 6, 'available', 'panthouse5.jpg'),
                    ('PENTHOUSE', 4, 2450.0, 6, 'available', 'panthouse6.jpg'),
                    ('PENTHOUSE', 4, 2500.0, 6, 'available', 'panthouse7.jpg'),
                    ('PENTHOUSE', 4, 2550.0, 6, 'available', 'panthouse8.jpg');
                """;

                conn.createStatement().executeUpdate(insertRooms);
                System.out.println("✅ Dummy rooms inserted.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error seeding dummy data: " + e.getMessage());
        }
    }

}
