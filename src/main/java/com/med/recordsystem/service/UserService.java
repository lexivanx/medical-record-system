package com.med.recordsystem.service;

import com.med.recordsystem.exception.DataNotFoundException;
import com.med.recordsystem.model.User;
import com.med.recordsystem.model.Visit;
import com.med.recordsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonalDocsRepository personalDocsRepository;
    private final VisitRepository visitRepository;
    private final SickLeaveRepository sickLeaveRepository;
    private final UserDiagnosisRepository userDiagnosesRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       PersonalDocsRepository personalDocsRepository,
                       VisitRepository visitRepository,
                       SickLeaveRepository sickLeaveRepository,
                       UserDiagnosisRepository userDiagnosesRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.personalDocsRepository = personalDocsRepository;
        this.visitRepository = visitRepository;
        this.sickLeaveRepository = sickLeaveRepository;
        this.userDiagnosesRepository = userDiagnosesRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        existingUser.setUsername(user.getUsername());
        existingUser.setFullName(user.getFullName());
        existingUser.setRole(user.getRole());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(existingUser);
    }

    public void updateUserAsAdmin(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        existingUser.setUsername(user.getUsername());
        existingUser.setFullName(user.getFullName());
        existingUser.setRole(user.getRole());

        if (user.getEgn() != null) {
            existingUser.setEgn(user.getEgn());
        }

        if (user.getIsEnsured() != null) {
            existingUser.setIsEnsured(user.getIsEnsured());
        }

        if (user.getSpecialty() != null) {
            existingUser.setSpecialty(user.getSpecialty());
        }

        if (hasText(user.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(existingUser);
    }


    public User saveUser(User user) {
        if (user.getId() == null) {
            if (hasText(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        } else {
            Optional<User> existingUserOpt = userRepository.findById(user.getId());
            if (existingUserOpt.isPresent()) {
                User existingUser = existingUserOpt.get();

                if (hasText(user.getPassword())
                        && !user.getPassword().equals(existingUser.getPassword())) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                } else {
                    user.setPassword(existingUser.getPassword());
                }
            } else {
                if (hasText(user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
            }
        }

        return userRepository.save(user);
    }

    public User saveUserAsAdmin(User user) {
        if (user.getId() == null) {
            if (hasText(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        } else {
            User existingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            if (hasText(user.getPassword()) && !user.getPassword().equals(existingUser.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                user.setPassword(existingUser.getPassword());
            }
        }

        return userRepository.save(user);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteUserAsAdmin(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        String role = user.getRole();

        if ("DOCTOR".equalsIgnoreCase(role)) {
            sickLeaveRepository.deleteByDoctorId(id);
            visitRepository.deleteByDoctorId(id);
            personalDocsRepository.deleteByIdDoctorId(id);

        } else if ("PATIENT".equalsIgnoreCase(role)) {
            sickLeaveRepository.deleteByPatientId(id);
            visitRepository.deleteByPatientId(id);
            userDiagnosesRepository.deleteByIdUserId(id);
            personalDocsRepository.deleteByIdPatientId(id);
        }

        userRepository.deleteById(id);
    }

    private boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public List<User> getPatients() {
        return userRepository.findByRole("PATIENT");
    }

    public List<User> getDoctors() {
        return userRepository.findByRole("DOCTOR");
    }

    public boolean validateDoctorAccessToPatient(User doctor, Long patientId) {
        boolean isPersonalDoctor = personalDocsRepository.existsByIdDoctorIdAndIdPatientId(doctor.getId(), patientId);

        boolean hasVisitHistory = visitRepository.existsByDoctorIdAndPatientId(doctor.getId(), patientId);

        return isPersonalDoctor || hasVisitHistory;
    }
}
