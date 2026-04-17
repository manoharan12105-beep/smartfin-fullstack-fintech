document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const errorMessage = document.getElementById('errorMessage');

    // Simple auth check
    if (localStorage.getItem('user')) {
        window.location.href = 'dashboard.html';
    }

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        errorMessage.style.display = 'none';

        try {
            const user = await api.login(email, password);
            localStorage.setItem('user', JSON.stringify(user));
            window.location.href = 'dashboard.html';
        } catch (error) {
            errorMessage.textContent = 'Invalid email or password. Please try again.';
            errorMessage.style.display = 'block';
        }
    });
});
