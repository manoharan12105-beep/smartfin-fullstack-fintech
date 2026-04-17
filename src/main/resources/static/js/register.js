document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('registerForm');
    const errorMessage = document.getElementById('errorMessage');

    // Simple auth check
    if (localStorage.getItem('user')) {
        window.location.href = 'dashboard.html';
    }

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const monthlyIncome = document.getElementById('monthlyIncome').value;

        errorMessage.style.display = 'none';

        try {
            const newUser = {
                name: name,
                email: email,
                password: password,
                monthlyIncome: parseFloat(monthlyIncome) || 30000
            };
            
            const createdUser = await api.register(newUser);
            localStorage.setItem('user', JSON.stringify(createdUser));
            window.location.href = 'dashboard.html';
            
        } catch (error) {
            errorMessage.textContent = 'Registration failed. Email might already exist.';
            errorMessage.style.display = 'block';
        }
    });
});
