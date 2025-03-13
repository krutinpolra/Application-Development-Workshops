
/*
Workshop: 2
Name: KRUTIN BHARATBHAI POLRA
Id: 135416220
*/
package com.example.workshop_2.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MaintenanceRecord {
    private final StringProperty model;
    private final StringProperty make;
    private final StringProperty date;
    private final StringProperty description;
    private final StringProperty cost;

    public MaintenanceRecord(String model, String make, String date, String description, String cost) {
        this.model = new SimpleStringProperty(model);
        this.make = new SimpleStringProperty(make);
        this.date = new SimpleStringProperty(date);
        this.description = new SimpleStringProperty(description);
        this.cost = new SimpleStringProperty(cost);
    }

    public StringProperty modelProperty() { return model; }
    public StringProperty makeProperty() { return make; }
    public StringProperty dateProperty() { return date; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty costProperty() { return cost; }

}
