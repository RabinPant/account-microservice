package com.account.bank.controller;

import com.account.bank.config.AccountsServiceConfig;
import com.account.bank.model.*;
import com.account.bank.repository.AccountsRepository;
import com.account.bank.service.client.CardsFeignClient;
import com.account.bank.service.client.LoansFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.annotation.Timed;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class AccountsController {

    @Autowired //DI -IOC
    private AccountsRepository accountsRepository;

    @Autowired
    AccountsServiceConfig accountsConfig;

    @Autowired
    LoansFeignClient loansFeignClient;

    @Autowired
    CardsFeignClient cardsFeignClient;

    @PostMapping("/myAccount")
    @Timed(value = "getAccountDetails.time",description = "Time taken to return Account Details")
    public Accounts getAccountDetails(@RequestBody Customer customer){

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        if(accounts!=null){
            return accounts;
        } else{
            return null;
        }
    }

    @GetMapping("/account/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
                accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
        String jsonStr = ow.writeValueAsString(properties);
        return jsonStr;
    }
    @PostMapping("/myCustomerDetails")
//    @CircuitBreaker(name = "detailsForCustomerSupportApp",fallbackMethod = "myCustomerDetailsFallBack")

    @Retry(name="retryForCustomerDetails",fallbackMethod = "myCustomerDetailsFallBack")
    public CustomerDetails myCustomerDetails(@RequestBody Customer customer){
        log.info("myCustomerDetails() method started");
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        List<Loans> loans = loansFeignClient.getLoansDetails(customer);
        List<Cards> cardDetails = cardsFeignClient.getCardDetails(customer);

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setLoans(loans);
        customerDetails.setCards(cardDetails);
        log.info("myCustomerDetails() method ended");
        return  customerDetails;
    }

    private CustomerDetails myCustomerDetailsFallBack(Customer customer, Throwable t) {

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        List<Loans> loans = loansFeignClient.getLoansDetails(customer);
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setLoans(loans);
        return customerDetails;
    }
    @GetMapping("/sayHello")
    @RateLimiter(name="sayHello",fallbackMethod = "sayHelloFallBack")
    public String sayHello(){return "Hello, welcome to rabinBank";}
    public String sayHelloFallBack(Throwable t){
        return "Hi, Welcome to RaBiN BaNk";
    }
}
