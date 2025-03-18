package com.seneca.apd.ws45.workshop45.Models;

import javafx.beans.property.*;

public abstract class Part {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final IntegerProperty stock = new SimpleIntegerProperty();
    private final IntegerProperty min = new SimpleIntegerProperty();
    private final IntegerProperty max = new SimpleIntegerProperty();

    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id.set(id);
        this.name.set(name);
        this.price.set(price);
        this.stock.set(stock);
        this.min.set(min);
        this.max.set(max);
    }

    // Getters and Setters with Property Methods
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }
    public DoubleProperty priceProperty() { return price; }

    public int getStock() { return stock.get(); }
    public void setStock(int stock) { this.stock.set(stock); }
    public IntegerProperty stockProperty() { return stock; }

    public int getMin() { return min.get(); }
    public void setMin(int min) { this.min.set(min); }
    public IntegerProperty minProperty() { return min; }

    public int getMax() { return max.get(); }
    public void setMax(int max) { this.max.set(max); }
    public IntegerProperty maxProperty() { return max; }
}
