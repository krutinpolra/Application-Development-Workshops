package com.apd.ws3.auto_loan_applicationws3;

import com.apd.ws3.auto_loan_applicationws3.Models.LoanAmortization;

import java.util.List;

public interface LoanCalculation {
    double calculateMonthlyPayment();
    List<LoanAmortization> generateAmortizationSchedule(String frequency);
}
