const API_BASE = 'http://localhost:8080/api';

async function fetchJSON(url, options = {}) {
    try {
        const response = await fetch(`${API_BASE}${url}`, {
            headers: {
                'Content-Type': 'application/json'
            },
            ...options
        });
        
        if (!response.ok) {
            const errText = await response.text();
            throw new Error(errText || 'API request failed');
        }
        
        return await response.json();
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

const api = {
    login: (email, password) => fetchJSON('/users/login', {
        method: 'POST',
        body: JSON.stringify({ email, password })
    }),
    
    register: (user) => fetchJSON('/users/register', {
        method: 'POST',
        body: JSON.stringify(user)
    }),

    calculateLoan: (request) => fetchJSON('/loans/calculate', {
        method: 'POST',
        body: JSON.stringify(request)
    }),

    applyForLoan: (request) => fetchJSON('/loans/apply', {
        method: 'POST',
        body: JSON.stringify(request)
    }),

    getUserLoans: (userId) => fetchJSON(`/loans/user/${userId}`)
};
