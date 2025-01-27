document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('registerForm');
    const doctorDropdown = document.getElementById('doctor');
    const errorMessageDiv = document.createElement('div');
    errorMessageDiv.className = 'alert alert-danger mt-3';
    errorMessageDiv.style.display = 'none';
    form.appendChild(errorMessageDiv);

    fetch("/api/users/doctors")
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch doctors");
            }
            return response.json();
        })
        .then(doctors => {
            doctors.forEach(doctor => {
                const option = document.createElement("option");
                option.value = doctor.id;
                option.textContent = doctor.fullName;
                doctorDropdown.appendChild(option);
            });
        })
        .catch(error => {
            console.error("Error fetching doctors:", error);
            alert("Failed to load doctors. Please try again later.");
        });

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        const fullName = document.getElementById("full_name").value;
        const egn = document.getElementById("egn").value;
        const doctorId = document.getElementById("doctor").value;

        const user = {
            username: username,
            password: password,
            fullName: fullName,
            egn: egn || null,
            role: "PATIENT"
        };

        fetch(`/api/users/register?personalDoctorId=${doctorId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(errorText => {
                        throw new Error(errorText);
                    });
                }
                alert("Registration successful! You can now log in.");
                window.location.href = "/login";
            })
            .catch(error => {
                errorMessageDiv.textContent = error.message;
                errorMessageDiv.style.display = 'block';
            });
    });
});
