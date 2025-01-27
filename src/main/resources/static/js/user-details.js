document.addEventListener("DOMContentLoaded", () => {
    fetch('/api/users/me', {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to fetch user data. Status = ${response.status}`);
            }
            return response.json();
        })
        .then(user => {
            document.getElementById("username").textContent = user.username || "N/A";
            document.getElementById("fullName").textContent = user.fullName || "N/A";

            if (user.role === 'ADMIN') {
                const roleField = document.getElementById("roleField");
                roleField.style.display = 'block';
                roleField.classList.remove("d-none");
                const adminSection = document.getElementById("adminSection");
                adminSection.style.display = 'block';
                adminSection.classList.remove("d-none");
                document.getElementById("role").textContent = user.role || "N/A";
            }

            if (user.role === 'PATIENT') {
                const egnField = document.getElementById("egnField");
                egnField.style.display = 'block';
                egnField.classList.remove("d-none");

                const healthInsuranceField = document.getElementById("healthInsuranceField");
                healthInsuranceField.style.display = 'block';
                healthInsuranceField.classList.remove("d-none");

                const personalDoctorField = document.getElementById("personalDoctorField");
                personalDoctorField.style.display = 'block';
                personalDoctorField.classList.remove("d-none");

                const patientLinks = document.getElementById("patientLinks");
                patientLinks.style.display = 'block';
                patientLinks.classList.remove("d-none");

                document.getElementById("egn").textContent = user.egn || "N/A";
                document.getElementById("isEnsured").textContent = (user.isEnsured !== null) ? (user.isEnsured ? "Yes" : "No") : "N/A";

                fetch('/api/personal-docs/me', {
                    headers: {
                        'Accept': 'text/plain',
                        'Content-Type': 'application/json'
                    }
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`HTTP error! Status: ${response.status}`);
                        }
                        return response.text();
                    })
                    .then(data => {
                        document.getElementById("personalDoctor").textContent = data || "N/A";
                    })
                    .catch(error => {
                        console.error('Error fetching personal doctor:', error);
                        document.getElementById("personalDoctor").textContent = "N/A";
                    });
            }

            if (user.role === 'DOCTOR') {

                const doctorSection = document.getElementById("doctorSection");
                doctorSection.style.display = 'block';
                doctorSection.classList.remove("d-none");

                const specialtyField = document.getElementById("specialtyField");
                specialtyField.style.display = 'block';
                specialtyField.classList.remove("d-none");

                document.getElementById("specialty").textContent = user.specialty || "N/A";
            }
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            alert('Error fetching user data. Please try again later.');
            window.location.href = "/login";
        });
});
