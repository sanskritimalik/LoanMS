package com.loanmanagement.LoanManagementSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRepaymentAmountException extends RuntimeException {
    public InvalidRepaymentAmountException(String message) {
        super(message);
    }
}

