package com.seneca.apd.ws45.workshop45.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class InHouse extends Part {
    private final IntegerProperty machineId = new SimpleIntegerProperty();

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId.set(machineId);
    }

    public int getMachineId() { return machineId.get(); }
    public void setMachineId(int machineId) { this.machineId.set(machineId); }
    public IntegerProperty machineIdProperty() { return machineId; }
}
