package com.med.recordsystem.web.api;

import com.med.recordsystem.model.User;
import com.med.recordsystem.service.PersonalDocsService;
import com.med.recordsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PersonalDocsService personalDocsService;

    @Autowired
    public UserController(UserService userService, PersonalDocsService personalDocsService) {
        this.userService = userService;
        this.personalDocsService = personalDocsService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        if (userService.getUserById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMe(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/doctors")
    public List<User> getDoctors() {
        return userService.getDoctors();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody User user,
            @RequestParam Long personalDoctorId
    ) {
        if (user.getRole() == null || !user.getRole().equalsIgnoreCase("PATIENT")) {
            user.setRole("PATIENT");
        }

        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }

        if (user.getEgn() == null || !user.getEgn().matches("\\d{10}")) {
            return ResponseEntity.badRequest().body("EGN must be exactly 10 digits and contain only numbers.");
        }

        User doctor = userService.getUserById(personalDoctorId)
                .orElseThrow(() -> new IllegalArgumentException("Personal doctor not found or invalid."));

        if (!doctor.getRole().equalsIgnoreCase("DOCTOR")) {
            return ResponseEntity.badRequest().body("Selected personal doctor is not a valid doctor.");
        }

        User savedUser = userService.saveUser(user);

        try {
            personalDocsService.createPersonalDoc(personalDoctorId, savedUser.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }




}

