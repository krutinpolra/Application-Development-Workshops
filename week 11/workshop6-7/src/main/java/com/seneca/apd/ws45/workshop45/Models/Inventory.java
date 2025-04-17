package com.seneca.apd.ws45.workshop45.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    static {
        // Load sample data on startup
        loadSampleData();
    }

    private static void loadSampleData() {
        //  Create Parts
        InHouse part1 = new InHouse(1, "Wheel", 10.99, 50, 10, 100, 1212);
        InHouse part2 = new InHouse(2, "Engine", 299.99, 10, 1, 100, 1212);
        InHouse part3 = new InHouse(3, "Battery", 59.99, 30, 10, 100, 1212);
        InHouse part4 = new InHouse(4, "Brakes", 45.50, 25, 10, 100, 1212);
        InHouse part5 = new InHouse(5, "Headlight", 12.99, 40, 10, 100, 1212);
        InHouse part6 = new InHouse(6, "Seat", 39.99, 15, 10, 100, 1212);
        InHouse part7 = new InHouse(7, "Steering Wheel", 69.99, 20, 10, 100, 1212);
        InHouse part8 = new InHouse(8, "Suspension", 89.99, 12, 10, 100, 1212);
        InHouse part9 = new InHouse(9, "Exhaust", 149.99, 8, 1, 100, 1212);
        InHouse part10 = new InHouse(10, "Transmission", 499.99, 5, 1, 100, 1212);

        // ✅ Add Parts to Inventory
        addPart(part1);
        addPart(part2);
        addPart(part3);
        addPart(part4);
        addPart(part5);
        addPart(part6);
        addPart(part7);
        addPart(part8);
        addPart(part9);
        addPart(part10);

        // ✅ Create Products with at least 1 associated part
        Product product1 = new Product(101, "Bicycle", 199.99, 15, 1, 30);
        product1.addAssociatedPart(part1);  // At least 1 part

        Product product2 = new Product(102, "Car", 19999.99, 5, 1, 10);
        product2.addAssociatedPart(part2);

        Product product3 = new Product(103, "Motorcycle", 5999.99, 8, 1, 15);
        product3.addAssociatedPart(part3);

        Product product4 = new Product(104, "Scooter", 799.99, 20, 1, 50);
        product4.addAssociatedPart(part4);

        Product product5 = new Product(105, "Electric Scooter", 1299.99, 10, 1, 25);
        product5.addAssociatedPart(part5);

        Product product6 = new Product(106, "Go-Kart", 2999.99, 3, 1, 5);
        product6.addAssociatedPart(part6);

        Product product7 = new Product(107, "Truck", 29999.99, 4, 1, 7);
        product7.addAssociatedPart(part7);

        Product product8 = new Product(108, "Electric Car", 34999.99, 6, 1, 12);
        product8.addAssociatedPart(part8);

        Product product9 = new Product(109, "Tractor", 25999.99, 2, 1, 5);
        product9.addAssociatedPart(part9);

        Product product10 = new Product(110,  "SUV", 39999.99, 7, 1, 10);
        product10.addAssociatedPart(part10);

        // ✅ Add Products to Inventory
        addProduct(product1);
        addProduct(product2);
        addProduct(product3);
        addProduct(product4);
        addProduct(product5);
        addProduct(product6);
        addProduct(product7);
        addProduct(product8);
        addProduct(product9);
        addProduct(product10);
    }

    // ✅ Add Parts & Products
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    // ✅ Search Parts & Products
    public static Part searchPartByID(int partId) {
        return allParts.stream().filter(part -> part.getId() == partId).findFirst().orElse(null);
    }

    public static Product searchProductByID(int productId) {
        return allProducts.stream().filter(product -> product.getId() == productId).findFirst().orElse(null);
    }

    public static ObservableList<Part> searchPartByName(String name) {
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(name.toLowerCase())) {
                filteredParts.add(part);
            }
        }
        return filteredParts;
    }

    public static ObservableList<Product> searchProductByName(String name) {
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    // ✅ Update Methods
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    // ✅ Delete Methods
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    // ✅ Get All Data
    public static ObservableList<Part> getAllParts() {
        return FXCollections.observableArrayList(allParts);
    }

    public static ObservableList<Product> getAllProducts() {
        System.out.println("🔄 Retrieving All Products: " + allProducts);
        return FXCollections.observableArrayList(allProducts); // Ensure a new list is returned
    }

    public static void setAllParts(List<Part> parts) {
        allParts.setAll(parts);
    }

    public static void setAllProducts(List<Product> products) {
        allProducts.setAll(products);
    }

}
