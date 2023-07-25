package com.loanmanagement.LoanManagementSystem.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loanmanagement.LoanManagementSystem.models.Loan;
import com.loanmanagement.LoanManagementSystem.models.LoanRequest;
import com.loanmanagement.LoanManagementSystem.models.User;
import com.loanmanagement.LoanManagementSystem.service.LoanService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody LoanRequest loanRequest, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        Loan loan = loanService.createLoan(user, loanRequest.getAmount(), loanRequest.getTerm());
        return ResponseEntity.ok(loan);
    }

    @PostMapping("/{loanId}/approve")
    public ResponseEntity<String> approveLoan(@PathVariable String loanId, @AuthenticationPrincipal UserDetails userDetails) {
        User admin = (User) userDetails;
        loanService.approveLoan(loanId, admin);
        return ResponseEntity.ok("Loan approved successfully.");
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getLoansByUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        List<Loan> loans = loanService.getLoansByUser(user);
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/{loanId}/repay")
    public ResponseEntity<String> addRepayment(@PathVariable String loanId, @RequestParam BigDecimal amount) {
        loanService.addRepayment(loanId, amount);
        return ResponseEntity.ok("Repayment added successfully.");
    }
}
