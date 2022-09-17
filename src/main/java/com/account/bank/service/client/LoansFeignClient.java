package com.account.bank.service.client;

import com.account.bank.model.Cards;
import com.account.bank.model.Customer;
import com.account.bank.model.Loans;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("loans")
public interface LoansFeignClient {

    @RequestMapping(method = RequestMethod.POST,value="myLoans",consumes = "application/json")
    List<Loans> getLoansDetails(@RequestBody Customer customer);
}
