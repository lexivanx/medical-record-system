package com.med.recordsystem.web.api;

import com.med.recordsystem.model.Visit;
import com.med.recordsystem.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping
    public ResponseEntity<List<Visit>> getAllVisits() {
        return ResponseEntity.ok(visitService.getAllVisits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visit> getVisitById(@PathVariable Long id) {
        return visitService.getVisitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Visit>> getVisitsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(visitService.getVisitsByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Visit>> getVisitsByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(visitService.getVisitsByDoctorId(doctorId));
    }

    @GetMapping("/slot")
    public ResponseEntity<List<Visit>> getVisitsBySlot(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime slot) {
        return ResponseEntity.ok(visitService.getVisitsBySlot(slot));
    }

    @PostMapping
    public ResponseEntity<Visit> createVisit(@RequestBody Visit visit) {
        Visit savedVisit = visitService.saveVisit(visit);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVisit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Visit> updateVisit(@PathVariable Long id, @RequestBody Visit visit) {
        if (visitService.getVisitById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        visit.setId(id);
        return ResponseEntity.ok(visitService.saveVisit(visit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        if (visitService.getVisitById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        visitService.deleteVisit(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<Visit>> getVisitsForCurrentPatient(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(visitService.getVisitsByUsername(username));
    }

}
