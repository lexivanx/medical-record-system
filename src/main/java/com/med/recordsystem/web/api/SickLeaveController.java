package com.med.recordsystem.web.api;

import com.med.recordsystem.model.SickLeave;
import com.med.recordsystem.model.User;
import com.med.recordsystem.service.SickLeaveService;
import com.med.recordsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sick-leaves")
public class SickLeaveController {

    private final SickLeaveService sickLeaveService;
    private final UserService userService;

    @Autowired
    public SickLeaveController(SickLeaveService sickLeaveService, UserService userService) {
        this.sickLeaveService = sickLeaveService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<SickLeave>> getAllSickLeaves() {
        return ResponseEntity.ok(sickLeaveService.getAllSickLeaves());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SickLeave> getSickLeaveById(@PathVariable Long id) {
        return sickLeaveService.getSickLeaveById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<SickLeave>> getSickLeavesByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(sickLeaveService.getSickLeavesByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<SickLeave>> getSickLeavesByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(sickLeaveService.getSickLeavesByDoctorId(doctorId));
    }

    @PostMapping
    public ResponseEntity<SickLeave> createSickLeave(@RequestBody SickLeave sickLeave) {
        SickLeave savedSickLeave = sickLeaveService.saveSickLeave(sickLeave);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSickLeave);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SickLeave> updateSickLeave(@PathVariable Long id, @RequestBody SickLeave sickLeave) {
        if (sickLeaveService.getSickLeaveById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        sickLeave.setId(id);
        return ResponseEntity.ok(sickLeaveService.saveSickLeave(sickLeave));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSickLeave(@PathVariable Long id) {
        if (sickLeaveService.getSickLeaveById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        sickLeaveService.deleteSickLeave(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<SickLeave>> getSickLeavesForCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User patient = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(sickLeaveService.getSickLeavesByPatientId(patient.getId()));
    }
}
