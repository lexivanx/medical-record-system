document.addEventListener("DOMContentLoaded", () => {
    const reportSelect = document.getElementById("report-select");
    const diagnosisDropdown = document.getElementById("diagnosis-dropdown");
    const doctorDropdown = document.getElementById("doctor-dropdown");
    const patientDropdown = document.getElementById("patient-dropdown");
    const dateRange = document.getElementById("date-range");
    const yearInput = document.getElementById("year-input");

    function hideAll() {
        diagnosisDropdown.classList.add("d-none");
        doctorDropdown.classList.add("d-none");
        patientDropdown.classList.add("d-none");
        dateRange.classList.add("d-none");
        yearInput.classList.add("d-none");
    }

    function updateVisibility() {
        hideAll();
        const selected = reportSelect.value;
        if (selected === "1") {
            diagnosisDropdown.classList.remove("d-none");
        } else if (selected === "3") {
            doctorDropdown.classList.remove("d-none");
        } else if (selected === "6") {
            patientDropdown.classList.remove("d-none");
        } else if (selected === "7") {
            dateRange.classList.remove("d-none");
        } else if (selected === "8") {
            doctorDropdown.classList.remove("d-none");
            dateRange.classList.remove("d-none");
        } else if (selected === "9") {
            yearInput.classList.remove("d-none");
        }
    }

    reportSelect.addEventListener("change", updateVisibility);

    updateVisibility();
});
