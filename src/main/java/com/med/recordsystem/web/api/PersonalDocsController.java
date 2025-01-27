package com.med.recordsystem.web.api;

import com.med.recordsystem.model.PersonalDocs;
import com.med.recordsystem.model.PersonalDocsId;
import com.med.recordsystem.model.User;
import com.med.recordsystem.service.PersonalDocsService;
import com.med.recordsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal-docs")
public class PersonalDocsController {

    private final PersonalDocsService personalDocsService;
    private final UserService userService;

    @Autowired
    public PersonalDocsController(PersonalDocsService personalDocsService, UserService userService) {
        this.personalDocsService = personalDocsService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<PersonalDocs>> getAllPersonalDocs() {
        return ResponseEntity.ok(personalDocsService.getAllPersonalDocs());
    }

    @GetMapping("/{doctorId}/{patientId}")
    public ResponseEntity<PersonalDocs> getPersonalDocsById(@PathVariable Long doctorId, @PathVariable Long patientId) {
        PersonalDocsId id = new PersonalDocsId(doctorId, patientId);
        return personalDocsService.getPersonalDocsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PersonalDocs>> getPersonalDocsByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(personalDocsService.getPersonalDocsByDoctorId(doctorId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PersonalDocs>> getPersonalDocsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(personalDocsService.getPersonalDocsByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}/patients")
    public ResponseEntity<List<User>> getPatientsByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(personalDocsService.getPatientsByDoctorId(doctorId));
    }

    @GetMapping("/patient/{patientId}/doctors")
    public ResponseEntity<List<User>> getDoctorsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(personalDocsService.getDoctorsByPatientId(patientId));
    }

    @PostMapping
    public ResponseEntity<PersonalDocs> createPersonalDocs(@RequestBody PersonalDocs personalDocs) {
        PersonalDocs savedPersonalDocs = personalDocsService.savePersonalDocs(personalDocs);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPersonalDocs);
    }

    @PutMapping("/{doctorId}/{patientId}")
    public ResponseEntity<PersonalDocs> updatePersonalDocs(
            @PathVariable Long doctorId,
            @PathVariable Long patientId,
            @RequestBody PersonalDocs personalDocs) {
        PersonalDocsId id = new PersonalDocsId(doctorId, patientId);
        if (personalDocsService.getPersonalDocsById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        personalDocs.setId(id);
        return ResponseEntity.ok(personalDocsService.savePersonalDocs(personalDocs));
    }

    @DeleteMapping("/{doctorId}/{patientId}")
    public ResponseEntity<Void> deletePersonalDocs(@PathVariable Long doctorId, @PathVariable Long patientId) {
        PersonalDocsId id = new PersonalDocsId(doctorId, patientId);
        if (personalDocsService.getPersonalDocsById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        personalDocsService.deletePersonalDocs(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<String> getPersonalDoctor(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User patient = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return personalDocsService.getPersonalDoctor(patient.getId())
                .map(doctor -> ResponseEntity.ok(doctor.getFullName()))
                .orElse(ResponseEntity.ok("N/A"));
    }
}
