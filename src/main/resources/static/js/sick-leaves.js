document.addEventListener("DOMContentLoaded", () => {
    fetch('/api/sick-leaves/me', {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(sickLeaves => {
            const sickLeavesTable = document.getElementById("sickLeavesTableBody");
            sickLeaves.forEach(leave => {
                const row = document.createElement("tr");

                const startDateCell = document.createElement("td");
                startDateCell.textContent = leave.dateStart;
                row.appendChild(startDateCell);

                const durationCell = document.createElement("td");
                durationCell.textContent = leave.durationDays;
                row.appendChild(durationCell);

                const doctorCell = document.createElement("td");
                doctorCell.textContent = leave.doctor.fullName || "N/A";
                row.appendChild(doctorCell);

                sickLeavesTable.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching sick leaves:', error);
            alert('Failed to load sick leaves.');
        });
});