(function () {
    const modalEl = document.getElementById('deleteConfirmModal');
    const confirmBtn = document.getElementById('confirmDeleteBtn');
    const nameEl = document.getElementById('deleteStudentName');
    if (!modalEl || !confirmBtn || typeof bootstrap === 'undefined') return;

    const modal = new bootstrap.Modal(modalEl);
    let formToSubmit = null;

    document.addEventListener('click', function (e) {
        const form = e.target.closest('form.delete-form');
        if (!form) return;

        const deleteBtn = e.target.closest('.delete-btn');
        if (!deleteBtn) return;

        e.preventDefault();

        formToSubmit = form;
        nameEl.textContent = form.getAttribute('data-student-name') || 'this student';
        modal.show();
    });

    confirmBtn.addEventListener('click', function () {
        if (formToSubmit) formToSubmit.submit();
    });
})();
(function () {
    const btn = document.getElementById('copyEmailBtn');
    if (!btn) return;

    btn.addEventListener('click', async function () {
        const email = btn.getAttribute('data-email') || '';
        if (!email) return;

        try {
            await navigator.clipboard.writeText(email);
            const old = btn.textContent;
            btn.textContent = 'Copied';
            setTimeout(() => (btn.textContent = old), 900);
        } catch (e) {
            // fallback
            prompt('Copy email:', email);
        }
    });
})();