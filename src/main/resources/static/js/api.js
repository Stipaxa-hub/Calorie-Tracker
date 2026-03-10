/* ================================================
   API CLIENT — thin fetch wrapper with JWT handling
   ================================================ */

const API = (() => {
    // Use absolute URL so the frontend works even if opened
    // from the IDE preview or a different port.
    const BASE = 'http://localhost:8080/api';

    /* ---------- token helpers ---------- */
    function saveTokens(data) {
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('refreshToken', data.refreshToken);
        localStorage.setItem('user', JSON.stringify(data.user));
    }

    function getToken() {
        return localStorage.getItem('accessToken');
    }

    function getUser() {
        try { return JSON.parse(localStorage.getItem('user')); }
        catch { return null; }
    }

    function isLoggedIn() {
        return !!getToken();
    }

    function clearAuth() {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
    }

    /* ---------- generic fetch ---------- */
    async function request(path, { method = 'GET', body, auth = false } = {}) {
        const headers = { 'Content-Type': 'application/json' };
        if (auth) {
            const token = getToken();
            if (token) headers['Authorization'] = `Bearer ${token}`;
        }
        const opts = { method, headers };
        if (body) opts.body = JSON.stringify(body);

        let res;
        try {
            res = await fetch(`${BASE}${path}`, opts);
        } catch (networkErr) {
            throw new Error('Cannot reach the server. Is the backend running on port 8080?');
        }

        // Handle non-JSON responses (e.g. HTML 404 pages)
        const contentType = res.headers.get('content-type') || '';
        if (!contentType.includes('application/json')) {
            throw new Error(`Server error (${res.status}). Expected JSON but got ${contentType || 'no content'}.`);
        }

        const json = await res.json();

        if (!res.ok || json.success === false) {
            const msg = json.message || json.error || `Request failed (${res.status})`;
            throw new Error(msg);
        }
        return json;
    }

    /* ---------- public API ---------- */
    async function register(fullName, email, password) {
        const json = await request('/v1/auth/register', {
            method: 'POST',
            body: { fullName, email, password }
        });
        saveTokens(json.data);
        return json;
    }

    async function login(email, password) {
        const json = await request('/v1/auth/login', {
            method: 'POST',
            body: { email, password }
        });
        saveTokens(json.data);
        return json;
    }

    async function getProfile() {
        return request('/v1/profile', { auth: true });
    }

    async function setupProfile(profileData) {
        return request('/v1/profile/setup', {
            method: 'PUT',
            body: profileData,
            auth: true
        });
    }

    function logout() {
        clearAuth();
    }

    return { register, login, getProfile, setupProfile, logout, isLoggedIn, getUser, getToken };
})();
