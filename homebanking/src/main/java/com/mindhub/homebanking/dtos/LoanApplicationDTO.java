package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private long loanId;
    private double amount;
    private Integer payments;
    private String toAccountNumber;

    public long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }
}
