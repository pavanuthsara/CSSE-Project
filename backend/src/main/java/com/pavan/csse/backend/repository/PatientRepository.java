package com.pavan.csse.backend.repository;

import com.pavan.csse.backend.model.Patient;
import com.pavan.csse.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);
    Optional<Patient> findByPatientId(String patientId);
    List<Patient> findByUserFirstNameContainingIgnoreCaseOrUserLastNameContainingIgnoreCase(String firstName, String lastName);
    boolean existsByPatientId(String patientId);
}
