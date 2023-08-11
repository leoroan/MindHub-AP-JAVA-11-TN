package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Loan {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long id;
  private String name;
  private Double maxAmount;

  /**
   * Sometimes you want to have a one-to-many relationship between an entity and a
   * simple piece of data, such as a number or a string. For example, suppose we
   * wanted to store zero or more email addresses for users. We can use our Person
   * class to represent a user. We could create a class for email addresses, but
   * it may be that all we need is the string. In that case, we can annotate the
   * Person class to say that a person has a list of email strings by just adding
   * these lines of code:
   */
  @ElementCollection
  // @Column(name="payments") -no use here, just if the db is allready creted.
  private List<Integer> payments = new ArrayList<>();

  public Loan() {
  }

  public Loan(String name, Double maxAmount, List<Integer> payments) {
    this.name = name;
    this.maxAmount = maxAmount;
    this.payments = payments;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getMaxAmount() {
    return maxAmount;
  }

  public void setMaxAmount(Double maxAmount) {
    this.maxAmount = maxAmount;
  }

  public List<Integer> getPayments() {
    return payments;
  }

  public void setPayments(List<Integer> payments) {
    this.payments = payments;
  }

}
