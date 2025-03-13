/*
Workshop: 2
Name: KRUTIN BHARATBHAI POLRA
Id: 135416220
*/

package com.example.workshop_2.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VehicleData {
    private final StringProperty model;
    private final StringProperty make;
    private final StringProperty year;
    private final StringProperty type;

    public VehicleData(String model, String make, String year, String type) {
        this.model = new SimpleStringProperty(model);
        this.make = new SimpleStringProperty(make);
        this.year = new SimpleStringProperty(year);
        this.type = new SimpleStringProperty(type);
    }

    // Add Public Getters for JavaFX Binding
    public String getModel() { return model.get(); }
    public String getMake() { return make.get(); }
    public String getYear() { return year.get(); }
    public String getType() { return type.get(); }

}

