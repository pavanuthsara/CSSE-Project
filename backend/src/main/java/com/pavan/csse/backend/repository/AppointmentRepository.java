package com.pavan.csse.backend.repository;

import com.pavan.csse.backend.model.Appointment;
import com.pavan.csse.backend.model.AppointmentStatus;
import com.pavan.csse.backend.model.Doctor;
import com.pavan.csse.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate date);
    List<Appointment> findByAppointmentDateAndStatus(LocalDate date, AppointmentStatus status);
    List<Appointment> findByAppointmentDateBetween(LocalDate startDate, LocalDate endDate);
    boolean existsByDoctorAndAppointmentDateAndAppointmentTime(Doctor doctor, LocalDate date, LocalTime time);
    List<Appointment> findByPatientAndStatus(Patient patient, AppointmentStatus status);
    List<Appointment> findByDoctorAndStatus(Doctor doctor, AppointmentStatus status);
}
