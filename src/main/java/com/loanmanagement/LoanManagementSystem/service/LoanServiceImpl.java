package com.loanmanagement.LoanManagementSystem.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loanmanagement.LoanManagementSystem.constants.contants;
import com.loanmanagement.LoanManagementSystem.enums.LoanStatus;
import com.loanmanagement.LoanManagementSystem.enums.RepaymentStatus;
import com.loanmanagement.LoanManagementSystem.exceptions.InvalidRepaymentAmountException;
import com.loanmanagement.LoanManagementSystem.exceptions.LoanNotFoundException;
import com.loanmanagement.LoanManagementSystem.exceptions.RepaymentNotFoundException;
import com.loanmanagement.LoanManagementSystem.exceptions.UnauthorizedAccessException;
import com.loanmanagement.LoanManagementSystem.models.Loan;
import com.loanmanagement.LoanManagementSystem.models.Repayment;
import com.loanmanagement.LoanManagementSystem.models.User;
import com.loanmanagement.LoanManagementSystem.repository.LoanRepository;
import com.loanmanagement.LoanManagementSystem.repository.RepaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    @Autowired
    private final LoanRepository loanRepository;

     @Autowired
    private final RepaymentRepository repaymentRepository;

    @Override
    public Loan createLoan(User user, BigDecimal amount, Integer term) {
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setAmount(amount);
        loan.setTerm(term);
        loan.setRepayments(generateScheduledRepayments(amount, term));
        loan.setStatus(LoanStatus.PENDING);
        return loanRepository.save(loan);
    }

    @Override
    public void approveLoan(String loanId, User admin) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);
        if (optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();
            // Check if the user is an admin before approving
            if (admin.isAdmin()) {
                // Update loan status to APPROVED
                loan.setStatus(LoanStatus.APPROVED);
                loanRepository.save(loan);
            } else {
                throw new UnauthorizedAccessException("Only admin users can approve loans.");
            }
        } else {
            throw new LoanNotFoundException("Loan not found with ID: " + loanId);
        }
    }

    @Override
    public List<Loan> getLoansByUser(User user) {
        return loanRepository.findByUser(user);
    }

    @Override
    public void addRepayment(String loanId, BigDecimal amount) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);
        if (optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();
            Repayment pendingRepayment = loan.getRepayments().stream()
                    .filter(repayment -> repayment.getStatus() == RepaymentStatus.PENDING)
                    .findFirst()
                    .orElse(null);

            if (pendingRepayment != null) {
                if (amount.compareTo(pendingRepayment.getAmount()) >= 0) {
                    // Set repayment status to PAID
                    pendingRepayment.setStatus(RepaymentStatus.PAID);
                    repaymentRepository.save(pendingRepayment);

                    // Check if all repayments are PAID
                    if (loan.getRepayments().stream().allMatch(repayment -> repayment.getStatus() == RepaymentStatus.PAID)) {
                        // Set loan status to PAID
                        loan.setStatus(LoanStatus.PAID);
                        loanRepository.save(loan);
                    }
                } else {
                    throw new InvalidRepaymentAmountException("Repayment amount must be greater than or equal to the scheduled amount.");
                }
            } else {
                throw new RepaymentNotFoundException("No pending repayments found for the loan with ID: " + loanId);
            }
        } else {
            throw new LoanNotFoundException("Loan not found with ID: " + loanId);
        }
    }

    private List<Repayment> generateScheduledRepayments(BigDecimal amount, Integer term) {
        // Implement the logic to generate scheduled repayments based on loan amount and term
        // For simplicity, we'll assume weekly repayments here
        // You can use the LocalDate class to calculate the due dates
        // and split the loan amount into equal parts for each repayment.
        // Note: Please complete this part based on your specific business logic.
        List<Repayment> repayments = new ArrayList<>();
        BigDecimal weeklyAmount = amount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
        LocalDate dueDate = LocalDate.now().plusDays(contants.REPAYMENT_FREQUENCY); // Start from next week

        for (int i = 0; i < term; i++) {
            Repayment repayment = new Repayment();
            repayment.setAmount(weeklyAmount);
            repayment.setDueDate(dueDate);
            repayment.setStatus(RepaymentStatus.PENDING);
            repayments.add(repayment);
            dueDate = dueDate.plusDays(7); // Increment due date by 7 days for the next repayment
        }

        return repayments;
    }
}

