package com.apd.ws3.auto_loan_applicationws3.Models;

public class Customer {
    private String name;
    private String phone;
    private String city;
    private String province;

    public Customer(String name, String phone, String city, String province) {
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.province = province;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
}
