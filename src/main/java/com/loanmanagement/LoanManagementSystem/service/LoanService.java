package com.loanmanagement.LoanManagementSystem.service;

import java.math.BigDecimal;
import java.util.List;

import com.loanmanagement.LoanManagementSystem.models.Loan;
import com.loanmanagement.LoanManagementSystem.models.User;

public interface LoanService {
    public Loan createLoan(User user, BigDecimal amount, Integer term);

    public void approveLoan(String loanId, User admin);

    public List<Loan> getLoansByUser(User user);

    public void addRepayment(String loanId, BigDecimal amount);
}
