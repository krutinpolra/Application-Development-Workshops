package com.seneca.apd.finalproject.hotelreservation.Models;

import com.seneca.apd.finalproject.hotelreservation.InterFaces.IAdmin;

import java.util.Map;

public class Admin implements IAdmin {
    private int adminID;
    private String username;
    private String password;

    public Admin(int adminID, String username, String password) {
        this.adminID = adminID;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean login() {
        return true;
    }

    @Override
    public Map<String, String> searchGuest() {
        return Map.of("SearchResult", "Guest Found");
    }

    @Override
    public boolean checkOutGuest() {
        return true;
    }

    @Override
    public Map<String, String> generateReport() {
        return Map.of("Report", "Sample Report Data");
    }
}
