package com.cg.banking.repo;

import com.cg.banking.beans.BankAccount;
import com.cg.banking.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @Query("select b from BankAccount b where b.user.mobileNumber=:mobileNumber")
    List<BankAccount> findBankAccounts(String mobileNumber);
//    @Query("select u from bank_users u where u.mobile_number=:mobileNumber")
//    public User findByMobileNumber(@Param(value="mobileNumber") String mobileNumber);
}
