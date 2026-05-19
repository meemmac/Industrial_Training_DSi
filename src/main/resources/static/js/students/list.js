(function () {
    // If this page doesn't have the students table/search input, do nothing
    const tbody = document.querySelector("tbody");
    const input = document.getElementById("studentSearch");
    const prevBtn = document.getElementById("prevPageBtn");
    const nextBtn = document.getElementById("nextPageBtn");
    const pageNumEl = document.getElementById("pageNum");
    const pageTotalEl = document.getElementById("pageTotal");
    const count = document.getElementById("searchCount");

    // You may have an "empty state" page with no table; exit safely.
    if (!tbody) return;

    // -----------------------
    // Search + Pagination
    // -----------------------
    const pageSize = 25;
    const allRows = Array.from(tbody.querySelectorAll("tr"));
    let currentPage = 1; // 1-based
    let filteredRows = allRows;

    let sortKey = "id";        // default sort
    let sortDir = "asc";       // asc | desc

    function totalPages() {
        return Math.max(1, Math.ceil(filteredRows.length / pageSize));
    }

    function clampPage() {
        const tp = totalPages();
        if (currentPage < 1) currentPage = 1;
        if (currentPage > tp) currentPage = tp;
        return tp;
    }
    function compare(a, b) {
        const av = (a.getAttribute("data-" + sortKey) || "").toString();
        const bv = (b.getAttribute("data-" + sortKey) || "").toString();

        // numeric sort for id
        if (sortKey === "id") {
            const an = Number(av);
            const bn = Number(bv);
            return sortDir === "asc" ? an - bn : bn - an;
        }

        // string sort for name/email
        const res = av.localeCompare(bv, undefined, { sensitivity: "base" });
        return sortDir === "asc" ? res : -res;
    }

    function applySort() {
        filteredRows.sort(compare);
    }

    function render() {
        const tp = clampPage();
        applySort();
        const start = (currentPage - 1) * pageSize;
        const end = start + pageSize;

        allRows.forEach((r) => (r.style.display = "none"));
        filteredRows.slice(start, end).forEach((r) => (r.style.display = ""));

        if (pageNumEl) pageNumEl.textContent = String(currentPage);
        if (pageTotalEl) pageTotalEl.textContent = String(tp);
        if (prevBtn) prevBtn.disabled = currentPage <= 1;
        if (nextBtn) nextBtn.disabled = currentPage >= tp;

        const total = filteredRows.length;
        const from = total === 0 ? 0 : start + 1;
        const to = Math.min(end, total);

        if (count) count.textContent = `Showing ${from}-${to} of ${total}`;
    }

    function applyFilter() {
        const q = input ? input.value.trim().toLowerCase() : "";
        filteredRows = allRows.filter((row) => {
            const hay = (row.getAttribute("data-search") || "").toLowerCase();
            return q === "" || hay.includes(q);
        });

        currentPage = 1;
        render();
    }
    const sortHeaders = Array.from(document.querySelectorAll("th.sort-th"));

    sortHeaders.forEach((th) => {
        th.addEventListener("click", () => {
            const key = th.getAttribute("data-sort");
            if (!key) return;

            if (sortKey === key) {
                sortDir = sortDir === "asc" ? "desc" : "asc";
            } else {
                sortKey = key;
                sortDir = "asc";
            }

            currentPage = 1;
            render();
        });
    });


    if (input) input.addEventListener("input", applyFilter);
    if (prevBtn)
        prevBtn.addEventListener("click", function () {
            currentPage--;
            render();
        });
    if (nextBtn)
        nextBtn.addEventListener("click", function () {
            currentPage++;
            render();
        });

    applyFilter();

    // -----------------------
    // Delete confirmation modal
    // -----------------------
    const modalEl = document.getElementById("deleteConfirmModal");
    const confirmBtn = document.getElementById("confirmDeleteBtn");
    const nameEl = document.getElementById("deleteStudentName");

    // Bootstrap is required for Modal
    if (!modalEl || !confirmBtn || typeof bootstrap === "undefined") return;

    const modal = new bootstrap.Modal(modalEl);
    let formToSubmit = null;

    document.addEventListener("click", function (e) {
        const form = e.target.closest("form.delete-form");
        if (!form) return;

        const deleteBtn = e.target.closest(".delete-btn");
        if (!deleteBtn) return;

        e.preventDefault();

        formToSubmit = form;
        if (nameEl) {
            nameEl.textContent = form.getAttribute("data-student-name") || "this student";
        }

        modal.show();
    });

    confirmBtn.addEventListener("click", function () {
        if (formToSubmit) formToSubmit.submit();
    });
})();