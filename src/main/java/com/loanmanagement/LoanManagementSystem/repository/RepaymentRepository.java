package com.loanmanagement.LoanManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loanmanagement.LoanManagementSystem.models.Repayment;

@Repository
public interface RepaymentRepository extends JpaRepository<Repayment, String> {
}
