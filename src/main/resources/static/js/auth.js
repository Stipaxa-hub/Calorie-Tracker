/* ================================================
   AUTH — Login & Register form handlers
   ================================================ */

const Auth = (() => {

    /* ---------- validation helpers ---------- */
    const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const PASS_RE = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+\-]).{8,}$/;

    function clearErrors(form) {
        form.querySelectorAll('.field-error').forEach(el => el.textContent = '');
        form.querySelectorAll('.invalid').forEach(el => el.classList.remove('invalid'));
    }

    function showFieldError(id, msg) {
        const el = document.getElementById(id);
        if (el) el.textContent = msg;
        // also mark the input
        const inputId = id.replace('-error', '');
        const input = document.getElementById(inputId);
        if (input) input.classList.add('invalid');
    }

    function setLoading(btnId, loading) {
        const btn = document.getElementById(btnId);
        const text = btn.querySelector('.btn-text');
        const loader = btn.querySelector('.btn-loader');
        btn.disabled = loading;
        text.classList.toggle('hidden', loading);
        loader.classList.toggle('hidden', !loading);
    }

    /* ---------- LOGIN ---------- */
    function initLogin() {
        const form = document.getElementById('login-form');
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            clearErrors(form);

            const email = document.getElementById('login-email').value.trim();
            const password = document.getElementById('login-password').value;

            let valid = true;
            if (!email || !EMAIL_RE.test(email)) {
                showFieldError('login-email-error', 'Enter a valid email address');
                valid = false;
            }
            if (!password) {
                showFieldError('login-password-error', 'Password is required');
                valid = false;
            }
            if (!valid) return;

            setLoading('login-submit', true);
            try {
                await API.login(email, password);
                App.showToast('Login successful!', 'success');
                App.navigate();
            } catch (err) {
                App.showToast(err.message, 'error');
            } finally {
                setLoading('login-submit', false);
            }
        });
    }

    /* ---------- REGISTER ---------- */
    function initRegister() {
        const form = document.getElementById('register-form');
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            clearErrors(form);

            const fullName = document.getElementById('reg-name').value.trim();
            const email = document.getElementById('reg-email').value.trim();
            const password = document.getElementById('reg-password').value;

            let valid = true;
            if (!fullName || fullName.length < 2) {
                showFieldError('reg-name-error', 'Name must be at least 2 characters');
                valid = false;
            }
            if (!email || !EMAIL_RE.test(email)) {
                showFieldError('reg-email-error', 'Enter a valid email address');
                valid = false;
            }
            if (!PASS_RE.test(password)) {
                showFieldError('reg-password-error', 'Password does not meet requirements');
                valid = false;
            }
            if (!valid) return;

            setLoading('register-submit', true);
            try {
                await API.register(fullName, email, password);
                App.showToast('Account created!', 'success');
                App.navigate();
            } catch (err) {
                App.showToast(err.message, 'error');
            } finally {
                setLoading('register-submit', false);
            }
        });
    }

    function init() {
        initLogin();
        initRegister();

        document.getElementById('goto-register').addEventListener('click', (e) => {
            e.preventDefault();
            App.showView('register');
        });
        document.getElementById('goto-login').addEventListener('click', (e) => {
            e.preventDefault();
            App.showView('login');
        });
    }

    return { init };
})();
