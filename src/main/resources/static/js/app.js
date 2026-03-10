/* ================================================
   APP — Router, toast, initialisation
   ================================================ */

const App = (() => {
    const VIEWS = ['login', 'register', 'setup', 'dashboard'];

    /* ---------- view switching ---------- */
    function showView(name) {
        VIEWS.forEach(v => {
            const el = document.getElementById(`${v}-view`);
            if (el) el.classList.toggle('hidden', v !== name);
        });

        const navbar = document.getElementById('navbar');
        const authViews = ['login', 'register'];
        navbar.classList.toggle('hidden', authViews.includes(name));
    }

    /* ---------- toast ---------- */
    let toastTimer = null;
    function showToast(message, type = 'success') {
        const toast = document.getElementById('toast');
        toast.textContent = message;
        toast.className = `toast ${type} show`;
        clearTimeout(toastTimer);
        toastTimer = setTimeout(() => {
            toast.classList.remove('show');
        }, 3500);
    }

    /* ---------- routing logic ---------- */
    async function navigate() {
        if (!API.isLoggedIn()) {
            showView('login');
            return;
        }

        try {
            const json = await API.getProfile();
            const profile = json.data;

            if (profile.profileComplete) {
                await Profile.loadDashboard();
                showView('dashboard');
            } else {
                showView('setup');
            }
        } catch (err) {
            // token expired or invalid
            API.logout();
            showView('login');
            showToast('Session expired. Please log in again.', 'error');
        }
    }

    /* ---------- init ---------- */
    function init() {
        Auth.init();
        Profile.init();

        // logout
        document.getElementById('btn-logout').addEventListener('click', () => {
            API.logout();
            showView('login');
            showToast('Logged out', 'success');
        });

        // initial routing
        navigate();
    }

    document.addEventListener('DOMContentLoaded', init);

    return { showView, showToast, navigate };
})();
