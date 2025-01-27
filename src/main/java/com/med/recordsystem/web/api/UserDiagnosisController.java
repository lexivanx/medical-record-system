package com.med.recordsystem.web.api;

import com.med.recordsystem.model.Diagnosis;
import com.med.recordsystem.model.UserDiagnosis;
import com.med.recordsystem.model.UserDiagnosisId;
import com.med.recordsystem.service.UserDiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-diagnoses")
public class UserDiagnosisController {

    private final UserDiagnosisService userDiagnosisService;

    @Autowired
    public UserDiagnosisController(UserDiagnosisService userDiagnosisService) {
        this.userDiagnosisService = userDiagnosisService;
    }

    @GetMapping
    public ResponseEntity<List<UserDiagnosis>> getAllUserDiagnoses() {
        return ResponseEntity.ok(userDiagnosisService.getAllUserDiagnoses());
    }

    @GetMapping("/{userId}/{diagnosisId}")
    public ResponseEntity<UserDiagnosis> getUserDiagnosisById(@PathVariable Long userId, @PathVariable Long diagnosisId) {
        UserDiagnosisId id = new UserDiagnosisId(userId, diagnosisId);
        return userDiagnosisService.getUserDiagnosisById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserDiagnosis>> getUserDiagnosesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userDiagnosisService.getUserDiagnosesByUserId(userId));
    }

    @GetMapping("/user/{userId}/diagnoses")
    public ResponseEntity<List<Diagnosis>> getDiagnosesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userDiagnosisService.getDiagnosesByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<UserDiagnosis> createUserDiagnosis(@RequestBody UserDiagnosis userDiagnosis) {
        UserDiagnosis savedUserDiagnosis = userDiagnosisService.saveUserDiagnosis(userDiagnosis);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDiagnosis);
    }

    @PutMapping("/{userId}/{diagnosisId}")
    public ResponseEntity<UserDiagnosis> updateUserDiagnosis(
            @PathVariable Long userId,
            @PathVariable Long diagnosisId,
            @RequestBody UserDiagnosis userDiagnosis) {
        UserDiagnosisId id = new UserDiagnosisId(userId, diagnosisId);
        if (userDiagnosisService.getUserDiagnosisById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userDiagnosis.setId(id);
        return ResponseEntity.ok(userDiagnosisService.saveUserDiagnosis(userDiagnosis));
    }

    @DeleteMapping("/{userId}/{diagnosisId}")
    public ResponseEntity<Void> deleteUserDiagnosis(@PathVariable Long userId, @PathVariable Long diagnosisId) {
        UserDiagnosisId id = new UserDiagnosisId(userId, diagnosisId);
        if (userDiagnosisService.getUserDiagnosisById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userDiagnosisService.deleteUserDiagnosis(id);
        return ResponseEntity.noContent().build();
    }
}
