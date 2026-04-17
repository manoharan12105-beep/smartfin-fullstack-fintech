package me.mano.full_stack_FinTech_system.service;

import me.mano.full_stack_FinTech_system.dto.LoanApplicationRequest;
import me.mano.full_stack_FinTech_system.dto.LoanCalculationRequest;
import me.mano.full_stack_FinTech_system.dto.LoanCalculationResponse;
import me.mano.full_stack_FinTech_system.entity.Loan;
import me.mano.full_stack_FinTech_system.entity.User;
import me.mano.full_stack_FinTech_system.repository.LoanRepository;
import me.mano.full_stack_FinTech_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    public LoanCalculationResponse calculateLoan(LoanCalculationRequest request) {
        double principal = request.getAmount() != null ? request.getAmount() : 50000.0;
        double annualInterestRate = request.getInterestRate() != null ? request.getInterestRate() : 12.0;
        int tenureMonths = request.getTenureMonths() != null && request.getTenureMonths() > 0 ? request.getTenureMonths() : 12;
        double monthlyIncome = request.getMonthlyIncome() != null && request.getMonthlyIncome() > 0 ? request.getMonthlyIncome() : 30000.0;

        // EMI Formula: P x R x (1+R)^N / [(1+R)^N-1]
        // R = Monthly interest rate
        double monthlyInterestRate = (annualInterestRate / 12) / 100;
        
        double emi = 0;
        if (monthlyInterestRate > 0) {
            emi = (principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureMonths)) /
                    (Math.pow(1 + monthlyInterestRate, tenureMonths) - 1);
        } else {
            emi = principal / tenureMonths;
        }

        double totalPayable = emi * tenureMonths;
        double totalInterest = totalPayable - principal;

        // Risk calculation: EMI should ideally be less than 40% of monthly income
        String riskLevel = "LOW";
        double emiToIncomeRatio = (emi / monthlyIncome) * 100;

        if (emiToIncomeRatio > 50) {
            riskLevel = "HIGH";
        } else if (emiToIncomeRatio > 35) {
            riskLevel = "MEDIUM";
        }

        // Round off for clarity
        emi = Math.round(emi * 100.0) / 100.0;
        totalPayable = Math.round(totalPayable * 100.0) / 100.0;
        totalInterest = Math.round(totalInterest * 100.0) / 100.0;

        return new LoanCalculationResponse(emi, totalPayable, totalInterest, riskLevel);
    }

    public Loan applyForLoan(LoanApplicationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new me.mano.full_stack_FinTech_system.exception.ResourceNotFoundException("User not found"));

        LoanCalculationRequest calcRequest = new LoanCalculationRequest();
        calcRequest.setAmount(request.getAmount());
        calcRequest.setInterestRate(request.getInterestRate());
        calcRequest.setTenureMonths(request.getTenureMonths());
        calcRequest.setMonthlyIncome(request.getMonthlyIncome());

        LoanCalculationResponse calcResponse = calculateLoan(calcRequest);

        Loan loan = new Loan();
        loan.setAmount(request.getAmount());
        loan.setInterestRate(request.getInterestRate());
        loan.setTenureMonths(request.getTenureMonths());
        loan.setEmi(calcResponse.getEmi());
        loan.setTotalPayable(calcResponse.getTotalPayable());
        loan.setRiskLevel(calcResponse.getRiskLevel());
        loan.setApplicationDate(LocalDateTime.now());
        loan.setUser(user);

        return loanRepository.save(loan);
    }

    public List<Loan> getLoansForUser(Long userId) {
        return loanRepository.findByUserId(userId);
    }
}
