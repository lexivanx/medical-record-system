document.addEventListener("DOMContentLoaded", () => {
    const roleSelect = document.getElementById("role");
    const isEnsuredField = document.getElementById("isEnsuredField");
    const specialtyField = document.getElementById("specialtyField");
    const egnField = document.getElementById("egnField");

    const toggleFields = () => {
        const role = roleSelect.value;
        if (role === "PATIENT") {
            isEnsuredField.style.display = "block";
            isEnsuredField.classList.remove("d-none");

            egnField.style.display = "block";
            egnField.classList.remove("d-none");

            specialtyField.style.display = "none";
            specialtyField.classList.add("d-none");
        } else if (role === "DOCTOR") {
            isEnsuredField.style.display = "none";
            isEnsuredField.classList.add("d-none");

            egnField.style.display = "none";
            egnField.classList.add("d-none");

            specialtyField.style.display = "block";
            specialtyField.classList.remove("d-none");
        } else {
            isEnsuredField.style.display = "none";
            isEnsuredField.classList.add("d-none");

            specialtyField.style.display = "none";
            specialtyField.classList.add("d-none");

            egnField.style.display = "none";
            egnField.classList.add("d-none");
        }
    };

    roleSelect.addEventListener("change", toggleFields);
    toggleFields();
});
