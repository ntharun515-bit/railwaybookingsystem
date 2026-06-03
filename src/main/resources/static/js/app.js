const API_BASE_URL = 'http://localhost:8080/api';

// Authentication Utils
function setToken(token, user) {
    localStorage.setItem('jwtToken', token);
    localStorage.setItem('user', JSON.stringify(user));
}

function getToken() {
    return localStorage.getItem('jwtToken');
}

function getUser() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
}

function logout() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('user');
    window.location.href = '/login.html';
}

// API Call Wrapper
async function fetchAPI(endpoint, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    const token = getToken();
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        ...options,
        headers
    });

    if (response.status === 401) {
        logout();
        throw new Error('Unauthorized');
    }

    if (!response.ok) {
        const err = await response.json().catch(() => ({}));
        throw new Error(err.message || 'API request failed');
    }

    return response.json().catch(() => ({}));
}

// Update UI based on auth state
function updateAuthUI() {
    const user = getUser();
    const navAuth = document.getElementById('nav-auth');
    if (!navAuth) return;

    if (user) {
        navAuth.innerHTML = `
            <li class="nav-item">
                <a class="nav-link" href="/dashboard.html">Dashboard</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#" onclick="logout()">Logout (${user.username})</a>
            </li>
        `;
    } else {
        navAuth.innerHTML = `
            <li class="nav-item">
                <a class="nav-link" href="/login.html">Login</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/register.html">Register</a>
            </li>
        `;
    }
}

document.addEventListener('DOMContentLoaded', updateAuthUI);
