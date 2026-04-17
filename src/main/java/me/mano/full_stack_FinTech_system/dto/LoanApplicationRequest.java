package me.mano.full_stack_FinTech_system.dto;

import lombok.Data;

@Data
public class LoanApplicationRequest {
    private Long userId;
    private Double amount;
    private Double interestRate;
    private Integer tenureMonths;
    private Double monthlyIncome;
}
