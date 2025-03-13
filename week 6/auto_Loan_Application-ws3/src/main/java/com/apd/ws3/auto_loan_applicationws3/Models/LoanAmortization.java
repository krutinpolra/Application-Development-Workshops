package com.apd.ws3.auto_loan_applicationws3.Models;

public class LoanAmortization {
    private int paymentNumber;
    private String paymentDate;  // Add this field
    private double paymentAmount;
    private double interestAmount;
    private double principalPaid;
    private double endingBalance;

    public LoanAmortization(int paymentNumber, String paymentDate, double paymentAmount, double interestAmount, double principalPaid, double endingBalance) {
        this.paymentNumber = paymentNumber;
        this.paymentDate = paymentDate;  // Store the date as String
        this.paymentAmount = paymentAmount;
        this.interestAmount = interestAmount;
        this.principalPaid = principalPaid;
        this.endingBalance = endingBalance;
    }

    public int getPaymentNumber() { return paymentNumber; }
    public String getPaymentDate() { return paymentDate; }  // Add getter
    public double getPaymentAmount() { return paymentAmount; }
    public double getInterestAmount() { return interestAmount; }
    public double getPrincipalPaid() { return principalPaid; }
    public double getEndingBalance() { return endingBalance; }
}
