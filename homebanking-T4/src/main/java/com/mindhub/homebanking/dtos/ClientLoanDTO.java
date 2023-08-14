package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {

  private long id;
  private long loanId;
  private String name;
  private double amount;
  private Integer payments;

  public ClientLoanDTO(ClientLoan clientLoan) {
    this.id = clientLoan.getId();
    this.loanId = clientLoan.getLoan().getId();
    this.name = clientLoan.getLoan().getName();
    this.amount = clientLoan.getAmount();
    this.payments = clientLoan.getPayments();
  }

  public long getid() {
    return id;
  }

  public long getLoanId() {
    return loanId;
  }

  public String getName() {
    return name;
  }

  public double getAmount() {
    return amount;
  }

  public Integer getPayments() {
    return payments;
  }

}
