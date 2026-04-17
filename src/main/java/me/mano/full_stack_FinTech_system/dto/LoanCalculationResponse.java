package me.mano.full_stack_FinTech_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanCalculationResponse {
    private Double emi;
    private Double totalPayable;
    private Double totalInterest;
    private String riskLevel;
}
