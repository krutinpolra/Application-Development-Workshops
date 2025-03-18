package com.seneca.apd.ws45.workshop45.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Outsourced extends Part {
    private final StringProperty companyName = new SimpleStringProperty();

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName.set(companyName);
    }

    public String getCompanyName() { return companyName.get(); }
    public void setCompanyName(String companyName) { this.companyName.set(companyName); }
    public StringProperty companyNameProperty() { return companyName; }
}
