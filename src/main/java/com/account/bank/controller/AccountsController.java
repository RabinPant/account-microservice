package com.account.bank.controller;

import com.account.bank.model.Accounts;
import com.account.bank.model.Customer;
import com.account.bank.repository.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    @Autowired
    private AccountsRepository accountsRepository;

    @PostMapping("/myAccount")
    public Accounts getAccountDetails(@RequestBody Customer customer){

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        if(accounts!=null){

            return accounts;
        } else{

            return null;
        }
    }

}
