package com.med.recordsystem.web.api;

import com.med.recordsystem.model.Diagnosis;
import com.med.recordsystem.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnoses")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @Autowired
    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @GetMapping
    public ResponseEntity<List<Diagnosis>> getAllDiagnoses() {
        return ResponseEntity.ok(diagnosisService.getAllDiagnoses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Diagnosis> getDiagnosisById(@PathVariable Long id) {
        return diagnosisService.getDiagnosisById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Diagnosis> getDiagnosisByName(@PathVariable String name) {
        Diagnosis diagnosis = diagnosisService.getDiagnosisByName(name);
        return diagnosis != null ? ResponseEntity.ok(diagnosis) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Diagnosis> createDiagnosis(@RequestBody Diagnosis diagnosis) {
        Diagnosis savedDiagnosis = diagnosisService.saveDiagnosis(diagnosis);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDiagnosis);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Diagnosis> updateDiagnosis(@PathVariable Long id, @RequestBody Diagnosis diagnosis) {
        if (diagnosisService.getDiagnosisById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        diagnosis.setId(id);
        return ResponseEntity.ok(diagnosisService.saveDiagnosis(diagnosis));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiagnosis(@PathVariable Long id) {
        if (diagnosisService.getDiagnosisById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        diagnosisService.deleteDiagnosis(id);
        return ResponseEntity.noContent().build();
    }
}
