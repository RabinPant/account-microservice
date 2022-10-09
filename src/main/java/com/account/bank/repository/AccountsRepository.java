package com.account.bank.repository;

import com.account.bank.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
/*
* CRUD
* JPA
* PAGINATION
* */
    Accounts findByCustomerId(int customerId);

}
