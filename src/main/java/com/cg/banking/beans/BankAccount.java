package com.cg.banking.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
@Table(name = "bank_accounts")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_no")
    private Long accountNo;
    @Column(name = "ifsc",nullable = false)
    @Pattern(regexp = "^[A-Z]{4}[0-9]{7}$", message = "IFSC code must have 4 alphabets followed by 7 numbers")
    private String ifsc;
    private int balance;
    @Column(name = "account_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    //@JsonIgnore
    private User user;

    public BankAccount() {
    }

    public BankAccount(String ifsc, int balance, AccountType accountType, User user) {
        this.ifsc = ifsc;
        this.balance = balance;
        this.accountType = accountType;
        this.user = user;
    }

    public Long getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Long accountNo) {
        this.accountNo = accountNo;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNo=" + accountNo +
                ", ifsc='" + ifsc + '\'' +
                ", balance=" + balance +
                ", accountType=" + accountType +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount)) return false;
        BankAccount that = (BankAccount) o;
        return balance == that.balance && Objects.equals(accountNo, that.accountNo) && Objects.equals(ifsc, that.ifsc) && accountType == that.accountType && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNo, ifsc, balance, accountType, user);
    }
}
