package com.apd.ws3.auto_loan_applicationws3.Models;

import com.apd.ws3.auto_loan_applicationws3.LoanCalculation;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FixedRateLoan implements LoanCalculation {
    private double loanAmount;
    private double annualInterestRate;
    private int loanTermMonths;

    public FixedRateLoan(double loanAmount, double annualInterestRate, int loanTermMonths) {
        this.loanAmount = loanAmount;
        this.annualInterestRate = annualInterestRate;
        this.loanTermMonths = loanTermMonths;
    }

    @Override
    public double calculateMonthlyPayment() {
        double principal = loanAmount;
        double rate = annualInterestRate / 100;
        double time = loanTermMonths / 12.0; // Convert months to years

        // Simple Interest Formula: Interest = P * R * T
        double totalInterest = principal * rate * time;
        double totalPayment = principal + totalInterest;
        return totalPayment / loanTermMonths; // Monthly payment
    }

    @Override
    public List<LoanAmortization> generateAmortizationSchedule(String frequency) {
        List<LoanAmortization> schedule = new ArrayList<>();
        LocalDate startDate = LocalDate.now();
        double balance = loanAmount;
        double totalPayment;
        int totalPayments;
        double ratePerPeriod;

        // Determine payment frequency
        int paymentsPerYear;
        switch (frequency.toLowerCase()) {
            case "weekly":
                paymentsPerYear = 52;
                break;
            case "bi-weekly":
                paymentsPerYear = 26;
                break;
            case "monthly":
                paymentsPerYear = 12;
                break;
            default:
                throw new IllegalArgumentException("Invalid payment frequency: " + frequency);
        }

        totalPayments = loanTermMonths * (paymentsPerYear / 12);
        ratePerPeriod = (annualInterestRate / 100) / paymentsPerYear;

        // Calculate fixed payment amount using simple interest formula
        double totalInterest = balance * (annualInterestRate / 100) * (loanTermMonths / 12.0);
        double totalAmount = balance + totalInterest;
        totalPayment = totalAmount / totalPayments;

        // Generate schedule
        for (int i = 1; i <= totalPayments; i++) {
            if (balance <= 0) break;  // Stop if loan is paid off

            LocalDate paymentDate = getPaymentDate(startDate, frequency, i);
            double interest = balance * ratePerPeriod;
            double principal = totalPayment - interest;

            // Ensure we don't overpay
            if (principal > balance) {
                principal = balance;
                totalPayment = principal + interest;
            }

            balance -= principal;

            schedule.add(new LoanAmortization(i, paymentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    totalPayment, interest, principal, Math.max(0, balance)));

            // Stop if balance reaches zero
            if (balance <= 0) break;
        }

        System.out.println("✅ Generated Amortization Schedule with " + schedule.size() + " entries.");
        return schedule;
    }


    private LocalDate getPaymentDate(LocalDate startDate, String frequency, int paymentNumber) {
        switch (frequency.toLowerCase()) {
            case "weekly":
                return startDate.plusWeeks(paymentNumber);
            case "bi-weekly":
                return startDate.plusWeeks(paymentNumber * 2);
            case "monthly":
                return startDate.plusMonths(paymentNumber);
            default:
                throw new IllegalArgumentException("Invalid payment frequency: " + frequency);
        }
    }

    public double getLoanAmount() { return loanAmount; }
    public double getAnnualInterestRate() { return annualInterestRate; }
    public int getLoanTermMonths() { return loanTermMonths; }

}
