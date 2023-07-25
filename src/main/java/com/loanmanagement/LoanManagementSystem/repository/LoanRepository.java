package com.loanmanagement.LoanManagementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loanmanagement.LoanManagementSystem.models.Loan;
import com.loanmanagement.LoanManagementSystem.models.User;

@Repository
public interface LoanRepository extends JpaRepository<Loan, String> {
    List<Loan> findByUser(User user);
}