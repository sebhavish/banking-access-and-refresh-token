package com.cg.banking.repo;

import com.cg.banking.beans.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Integer> {
    @Query("select b from BankAccount b where b.accountNo = :accountNumber")
    public BankAccount getByAccountNo(@Param(value = "accountNumber") Long accountNumber);
}
