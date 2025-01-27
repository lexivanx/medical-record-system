document.addEventListener("DOMContentLoaded", () => {
    fetch('/api/visits/me', {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to fetch visits. Status = ${response.status}`);
            }
            return response.json();
        })
        .then(visits => {
            const visitsTable = document.getElementById("visitsTableBody");
            visits.forEach(visit => {
                const row = document.createElement("tr");

                const slotCell = document.createElement("td");
                slotCell.textContent = visit.slot;
                row.appendChild(slotCell);

                const treatmentCell = document.createElement("td");
                treatmentCell.textContent = visit.treatment;
                row.appendChild(treatmentCell);

                const doctorCell = document.createElement("td");
                doctorCell.textContent = visit.doctor.fullName;
                row.appendChild(doctorCell);

                visitsTable.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching visits:', error);
            alert('Failed to load visits. Please try again later.');
        });
});