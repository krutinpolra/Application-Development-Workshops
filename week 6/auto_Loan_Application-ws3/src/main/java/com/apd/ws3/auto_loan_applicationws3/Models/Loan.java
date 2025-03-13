package com.apd.ws3.auto_loan_applicationws3.Models;

public class Loan {
    private double amount;
    private double downPayment;
    private double interestRate;
    private int duration;
    private String frequency;
    private Customer customer;
    private Vehicle vehicle;

    public Loan(double amount, double interestRate, int duration, String frequency, Customer customer, Vehicle vehicle) {
        this.amount = amount;
        this.interestRate = interestRate;
        this.duration = duration;
        this.frequency = frequency;
        this.customer = customer;
        this.vehicle = vehicle;
    }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
}
