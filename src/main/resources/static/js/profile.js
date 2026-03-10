/* ================================================
   PROFILE — Setup form & Dashboard rendering
   ================================================ */

const Profile = (() => {

    function setLoading(btnId, loading) {
        const btn = document.getElementById(btnId);
        const text = btn.querySelector('.btn-text');
        const loader = btn.querySelector('.btn-loader');
        btn.disabled = loading;
        text.classList.toggle('hidden', loading);
        loader.classList.toggle('hidden', !loading);
    }

    /* ---------- SETUP ---------- */
    function initSetup() {
        const form = document.getElementById('setup-form');

        form.addEventListener('submit', async (e) => {
            e.preventDefault();

            // clear previous errors
            form.querySelectorAll('.field-error').forEach(el => el.textContent = '');
            form.querySelectorAll('.invalid').forEach(el => el.classList.remove('invalid'));

            const dateOfBirth = document.getElementById('setup-dob').value;
            const gender = document.getElementById('setup-gender').value;
            const heighCm = parseFloat(document.getElementById('setup-height').value);
            const weightKg = parseFloat(document.getElementById('setup-weight').value);
            const activityLevel = document.getElementById('setup-activity').value;
            const goalType = document.getElementById('setup-goal').value;

            let valid = true;
            function err(id, msg) {
                document.getElementById(id).textContent = msg;
                const inputId = id.replace('-error', '');
                const input = document.getElementById(inputId);
                if (input) input.classList.add('invalid');
                valid = false;
            }

            if (!dateOfBirth) err('setup-dob-error', 'Date of birth is required');
            if (!gender) err('setup-gender-error', 'Select a gender');
            if (isNaN(heighCm) || heighCm < 50 || heighCm > 300)
                err('setup-height-error', 'Height must be 50–300 cm');
            if (isNaN(weightKg) || weightKg < 20 || weightKg > 500)
                err('setup-weight-error', 'Weight must be 20–500 kg');
            if (!activityLevel) err('setup-activity-error', 'Select an activity level');
            if (!goalType) err('setup-goal-error', 'Select a goal');
            if (!valid) return;

            setLoading('setup-submit', true);
            try {
                await API.setupProfile({ dateOfBirth, heighCm, weightKg: weightKg, gender, activityLevel, goalType });
                App.showToast('Profile saved!', 'success');
                // update stored user
                const user = API.getUser();
                if (user) {
                    user.profileComplete = true;
                    localStorage.setItem('user', JSON.stringify(user));
                }
                await loadDashboard();
                App.showView('dashboard');
            } catch (err) {
                App.showToast(err.message, 'error');
            } finally {
                setLoading('setup-submit', false);
            }
        });
    }

    /* ---------- DASHBOARD ---------- */
    async function loadDashboard() {
        try {
            const json = await API.getProfile();
            const p = json.data;

            // greeting
            document.getElementById('dash-greeting').textContent =
                `Hey ${p.fullName} 👋 — here's your personalized plan.`;

            // macro cards
            document.getElementById('dash-calories').textContent = p.dailyCalorieGoal ?? '—';
            document.getElementById('dash-protein').textContent = p.dailyProteinGoal ?? '—';
            document.getElementById('dash-carbs').textContent = p.dailyCarbGoal ?? '—';
            document.getElementById('dash-fat').textContent = p.dailyFatGoal ?? '—';

            // detail grid
            const pretty = (s) => s ? s.replace(/_/g, ' ').replace(/\b\w/g, c => c.toUpperCase()) : '—';
            const details = [
                { label: 'Email', value: p.email },
                { label: 'Age', value: p.age ?? '—' },
                { label: 'Height', value: p.heighCm ? `${p.heighCm} cm` : '—' },
                { label: 'Weight', value: p.weightKg ? `${p.weightKg} kg` : '—' },
                { label: 'Gender', value: pretty(p.gender) },
                { label: 'Activity Level', value: pretty(p.activityLevel) },
                { label: 'Goal', value: pretty(p.goalType) },
                { label: 'Member Since', value: p.createdAt ? new Date(p.createdAt).toLocaleDateString() : '—' },
            ];

            const grid = document.getElementById('detail-grid');
            grid.innerHTML = details.map(d => `
                <div class="detail-item">
                    <span class="detail-label">${d.label}</span>
                    <span class="detail-value">${d.value}</span>
                </div>
            `).join('');

            // nav user
            document.getElementById('nav-user').textContent = p.fullName;
        } catch (err) {
            App.showToast('Failed to load profile: ' + err.message, 'error');
        }
    }

    function initDashboard() {
        document.getElementById('btn-edit-profile').addEventListener('click', () => {
            App.showView('setup');
        });
    }

    function init() {
        initSetup();
        initDashboard();
    }

    return { init, loadDashboard };
})();
