package com.account.bank.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter @ToString
public class Accounts {

    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int customerId;
    @Column(name="account_number")
    @Id
    private long accountNumber;
    @NotNull
    private String accountType;
    @Column(name="branch_address")
    private String branchAddress;
    private LocalDate createDt;


}
