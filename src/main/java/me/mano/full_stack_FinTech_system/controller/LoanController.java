package me.mano.full_stack_FinTech_system.controller;

import me.mano.full_stack_FinTech_system.dto.LoanApplicationRequest;
import me.mano.full_stack_FinTech_system.dto.LoanCalculationRequest;
import me.mano.full_stack_FinTech_system.dto.LoanCalculationResponse;
import me.mano.full_stack_FinTech_system.entity.Loan;
import me.mano.full_stack_FinTech_system.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/calculate")
    public LoanCalculationResponse calculateLoan(@RequestBody LoanCalculationRequest request) {
        return loanService.calculateLoan(request);
    }

    @PostMapping("/apply")
    public Loan applyForLoan(@RequestBody LoanApplicationRequest request) {
        return loanService.applyForLoan(request);
    }

    @GetMapping("/user/{userId}")
    public List<Loan> getUserLoans(@PathVariable Long userId) {
        return loanService.getLoansForUser(userId);
    }
}
