/*
Workshop: 2
Name: KRUTIN BHARATBHAI POLRA
Id: 135416220
*/

package com.example.workshop_2.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class UsageLog {
    private final StringProperty model;
    private final StringProperty make;
    private final StringProperty startDate;
    private final StringProperty endDate;
    private final StringProperty kilometers;

    public UsageLog(String model, String make, String startDate, String endDate, String kilometers) {
        this.model = new SimpleStringProperty(model);
        this.make = new SimpleStringProperty(make);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.kilometers = new SimpleStringProperty(kilometers);
    }

    public StringProperty modelProperty() { return model; }
    public StringProperty makeProperty() { return make; }
    public StringProperty startDateProperty() { return startDate; }
    public StringProperty endDateProperty() { return endDate; }
    public StringProperty kilometersProperty() { return kilometers; }

}
