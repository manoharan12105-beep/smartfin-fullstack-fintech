document.addEventListener('DOMContentLoaded', async () => {
    // Check Auth
    const userStr = localStorage.getItem('user');
    if (!userStr) {
        window.location.href = 'login.html';
        return;
    }

    const user = JSON.parse(userStr);
    document.getElementById('userNameDisplay').textContent = user.name;
    document.getElementById('monthlyIncome').value = user.monthlyIncome || 30000;

    // Elements
    const loanAmountInput = document.getElementById('loanAmount');
    const loanTenureInput = document.getElementById('loanTenure');
    const monthlyIncomeInput = document.getElementById('monthlyIncome');
    const interestRateInput = document.getElementById('interestRate');
    
    const loanAmountDisplay = document.getElementById('loanAmountDisplay');
    const loanTenureDisplay = document.getElementById('loanTenureDisplay');
    
    const emiResult = document.getElementById('emiResult');
    const totalPayableResult = document.getElementById('totalPayableResult');
    const totalInterestResult = document.getElementById('totalInterestResult');
    const riskIndicator = document.getElementById('riskIndicator');
    const riskMessage = document.getElementById('riskMessage');
    
    const applyBtn = document.getElementById('applyBtn');
    const applyMessage = document.getElementById('applyMessage');
    const logoutBtn = document.getElementById('logoutBtn');
    const activeLoansList = document.getElementById('activeLoansList');

    // State
    let currentCalculation = null;
    let debounceTimer;

    // Functions
    const formatCurrency = (amount) => {
        return new Intl.NumberFormat('en-IN').format(amount);
    };

    const updateUIFromCalculation = (calc) => {
        emiResult.textContent = formatCurrency(calc.emi);
        totalPayableResult.textContent = formatCurrency(calc.totalPayable);
        totalInterestResult.textContent = formatCurrency(calc.totalInterest);
        
        riskIndicator.className = `risk-badge risk-${calc.riskLevel}`;
        riskIndicator.textContent = `${calc.riskLevel} RISK`;
        
        if(calc.riskLevel === 'LOW') {
            riskMessage.textContent = 'Great! This loan comfortably fits your monthly budget.';
        } else if (calc.riskLevel === 'MEDIUM') {
            riskMessage.textContent = 'Warning: This EMI takes up a significant portion of your income.';
        } else {
            riskMessage.textContent = 'Danger: This loan may be very hard to repay based on your current income.';
        }
    };

    const calculatePreview = async () => {
        loanAmountDisplay.textContent = formatCurrency(loanAmountInput.value);
        loanTenureDisplay.textContent = loanTenureInput.value;

        const request = {
            amount: parseFloat(loanAmountInput.value) || 50000,
            interestRate: parseFloat(interestRateInput.value) || 12,
            tenureMonths: parseInt(loanTenureInput.value) || 12,
            monthlyIncome: parseFloat(monthlyIncomeInput.value) || 30000
        };

        try {
            const result = await api.calculateLoan(request);
            currentCalculation = result;
            updateUIFromCalculation(result);
        } catch (error) {
            console.error('Failed to calculate', error);
        }
    };

    const loadActiveLoans = async () => {
        try {
            const loans = await api.getUserLoans(user.id);
            if (loans.length === 0) {
                activeLoansList.innerHTML = '<p>You have no active loans.</p>';
                return;
            }

            activeLoansList.innerHTML = loans.map(loan => `
                <div style="border: 1px solid #E5E7EB; padding: 1rem; border-radius: 8px; margin-bottom: 1rem;">
                    <div class="flex justify-between align-center">
                        <strong>₹${formatCurrency(loan.amount)} Loan</strong>
                        <span class="risk-badge risk-${loan.riskLevel}" style="margin-top: 0; padding: 0.25rem 0.75rem;">${loan.riskLevel} RISK</span>
                    </div>
                    <p style="margin: 0.5rem 0; font-size: 0.875rem;">EMI: <strong>₹${formatCurrency(loan.emi)}</strong> for ${loan.tenureMonths} months</p>
                    <p style="margin: 0; font-size: 0.875rem; color: var(--text-secondary);">Applied on: ${new Date(loan.applicationDate).toLocaleDateString()}</p>
                </div>
            `).join('');
        } catch (error) {
            activeLoansList.innerHTML = '<p>Failed to load active loans.</p>';
        }
    };

    // Event Listeners
    const debouncedCalculate = () => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(calculatePreview, 300);
    };

    loanAmountInput.addEventListener('input', () => {
        loanAmountDisplay.textContent = formatCurrency(loanAmountInput.value);
        debouncedCalculate();
    });
    
    loanTenureInput.addEventListener('input', () => {
        loanTenureDisplay.textContent = loanTenureInput.value;
        debouncedCalculate();
    });
    
    monthlyIncomeInput.addEventListener('input', debouncedCalculate);

    applyBtn.addEventListener('click', async () => {
        applyBtn.disabled = true;
        applyBtn.textContent = 'Applying...';
        applyMessage.textContent = '';

        try {
            const request = {
                userId: user.id,
                amount: parseFloat(loanAmountInput.value) || 50000,
                interestRate: parseFloat(interestRateInput.value) || 12,
                tenureMonths: parseInt(loanTenureInput.value) || 12,
                monthlyIncome: parseFloat(monthlyIncomeInput.value) || 30000
            };

            await api.applyForLoan(request);
            
            applyMessage.textContent = 'Loan applied successfully!';
            applyMessage.style.color = 'green';
            
            // Reload active loans
            loadActiveLoans();
            
            setTimeout(() => {
                applyMessage.textContent = '';
                applyBtn.disabled = false;
                applyBtn.textContent = 'Apply for this Loan';
            }, 3000);

        } catch (error) {
            applyMessage.textContent = 'Failed to apply for loan.';
            applyMessage.style.color = 'red';
            applyBtn.disabled = false;
            applyBtn.textContent = 'Apply for this Loan';
        }
    });

    logoutBtn.addEventListener('click', () => {
        localStorage.removeItem('user');
        window.location.href = 'login.html';
    });

    // Initial Load
    calculatePreview();
    loadActiveLoans();
});
