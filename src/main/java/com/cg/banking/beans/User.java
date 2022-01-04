package com.cg.banking.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "bank_users")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class User {
    @Id
    @Column(name = "mobile_number")
    @Pattern(regexp = "^[6-9]{1}[0-9]{9}",message = "Mobile number should be 10 digit with first digit from 6 to 9 ")
    private String mobileNumber;
    @Column(nullable = false)
    @Pattern(regexp = "[A-Za-z ]{3,20}$",message = "Name must be only alphabets and whitespaces between 3 to 20 characters")
    private String name;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<BankAccount> bankAccounts;

    public User() {
    }

    public User(String mobileNumber, String name, String password, Set<BankAccount> bankAccounts, Roles role) {
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.password = password;
        this.bankAccounts = bankAccounts;
        this.role = role;
    }

    public Set<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(Set<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "mobileNumber=" + mobileNumber +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return mobileNumber.equals(user.mobileNumber) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mobileNumber, name, password, role);
    }
}
