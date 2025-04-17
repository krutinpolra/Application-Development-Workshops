package com.seneca.apd.ws45.workshop45.DataBase;

import com.seneca.apd.ws45.workshop45.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class InventoryDatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:inventory.db";

    static {
        createTablesIfNotExists();
    }

    private static void createTablesIfNotExists() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            try (PreparedStatement stmt1 = conn.prepareStatement("""
                CREATE TABLE IF NOT EXISTS Parts (
                    id INTEGER PRIMARY KEY,
                    name TEXT,
                    price REAL,
                    stock INTEGER,
                    min INTEGER,
                    max INTEGER,
                    isInHouse BOOLEAN,
                    machineId INTEGER,
                    companyName TEXT
                )
            """)) {
                stmt1.execute();
            }

            try (PreparedStatement stmt2 = conn.prepareStatement("""
                CREATE TABLE IF NOT EXISTS Products (
                    id INTEGER PRIMARY KEY,
                    name TEXT,
                    price REAL,
                    stock INTEGER,
                    min INTEGER,
                    max INTEGER
                )
            """)) {
                stmt2.execute();
            }

            try (PreparedStatement stmt3 = conn.prepareStatement("""
                CREATE TABLE IF NOT EXISTS ProductParts (
                    productId INTEGER,
                    partId INTEGER,
                    FOREIGN KEY(productId) REFERENCES Products(id),
                    FOREIGN KEY(partId) REFERENCES Parts(id)
                )
            """)) {
                stmt3.execute();
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to initialize DB: " + e.getMessage());
        }
    }

    public static boolean saveInventoryToDB(ObservableList<Part> parts, ObservableList<Product> products) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            try (PreparedStatement deletePP = conn.prepareStatement("DELETE FROM ProductParts");
                 PreparedStatement deleteProd = conn.prepareStatement("DELETE FROM Products");
                 PreparedStatement deletePart = conn.prepareStatement("DELETE FROM Parts")) {
                deletePP.executeUpdate();
                deleteProd.executeUpdate();
                deletePart.executeUpdate();
            }

            String partSQL = "INSERT INTO Parts VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement partStmt = conn.prepareStatement(partSQL)) {
                for (Part part : parts) {
                    partStmt.setInt(1, part.getId());
                    partStmt.setString(2, part.getName());
                    partStmt.setDouble(3, part.getPrice());
                    partStmt.setInt(4, part.getStock());
                    partStmt.setInt(5, part.getMin());
                    partStmt.setInt(6, part.getMax());

                    if (part instanceof InHouse inHouse) {
                        partStmt.setBoolean(7, true);
                        partStmt.setInt(8, inHouse.getMachineId());
                        partStmt.setNull(9, Types.VARCHAR);
                    } else if (part instanceof Outsourced outsourced) {
                        partStmt.setBoolean(7, false);
                        partStmt.setNull(8, Types.INTEGER);
                        partStmt.setString(9, outsourced.getCompanyName());
                    }
                    partStmt.addBatch();
                }
                partStmt.executeBatch();
            }

            String productSQL = "INSERT INTO Products VALUES (?, ?, ?, ?, ?, ?)";
            String assocSQL = "INSERT INTO ProductParts VALUES (?, ?)";
            try (PreparedStatement productStmt = conn.prepareStatement(productSQL);
                 PreparedStatement assocStmt = conn.prepareStatement(assocSQL)) {

                for (Product product : products) {
                    productStmt.setInt(1, product.getId());
                    productStmt.setString(2, product.getName());
                    productStmt.setDouble(3, product.getPrice());
                    productStmt.setInt(4, product.getStock());
                    productStmt.setInt(5, product.getMin());
                    productStmt.setInt(6, product.getMax());
                    productStmt.addBatch();

                    for (Part part : product.getAllAssociatedParts()) {
                        assocStmt.setInt(1, product.getId());
                        assocStmt.setInt(2, part.getId());
                        assocStmt.addBatch();
                    }
                }

                productStmt.executeBatch();
                assocStmt.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ DB Save Error: " + e.getMessage());
            return false;
        }
    }

    public static void loadInventoryFromDB() {
        ObservableList<Part> loadedParts = FXCollections.observableArrayList();
        ObservableList<Product> loadedProducts = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Parts");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");
                    int min = rs.getInt("min");
                    int max = rs.getInt("max");

                    Part part;
                    if (rs.getBoolean("isInHouse")) {
                        int machineId = rs.getInt("machineId");
                        part = new InHouse(id, name, price, stock, min, max, machineId);
                    } else {
                        String companyName = rs.getString("companyName");
                        part = new Outsourced(id, name, price, stock, min, max, companyName);
                    }
                    loadedParts.add(part);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Products");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");
                    int min = rs.getInt("min");
                    int max = rs.getInt("max");
                    Product product = new Product(id, name, price, stock, min, max);
                    loadedProducts.add(product);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM ProductParts");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int prodId = rs.getInt("productId");
                    int partId = rs.getInt("partId");

                    Product prod = loadedProducts.stream().filter(p -> p.getId() == prodId).findFirst().orElse(null);
                    Part part = loadedParts.stream().filter(p -> p.getId() == partId).findFirst().orElse(null);

                    if (prod != null && part != null) {
                        prod.addAssociatedPart(part);
                    }
                }
            }

            Inventory.setAllParts(loadedParts);
            Inventory.setAllProducts(loadedProducts);

        } catch (SQLException e) {
            System.err.println("❌ DB Load Error: " + e.getMessage());
        }
    }
}
