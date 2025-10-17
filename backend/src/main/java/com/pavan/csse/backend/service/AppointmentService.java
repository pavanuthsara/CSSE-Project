package com.pavan.csse.backend.service;

import com.pavan.csse.backend.dto.AppointmentBookingRequest;
import com.pavan.csse.backend.dto.AppointmentResponse;
import com.pavan.csse.backend.dto.AvailableTimeSlotResponse;
import com.pavan.csse.backend.model.*;
import com.pavan.csse.backend.repository.AppointmentRepository;
import com.pavan.csse.backend.repository.DoctorRepository;
import com.pavan.csse.backend.repository.PatientRepository;
import com.pavan.csse.backend.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    
    public AppointmentResponse bookAppointment(Long patientId, AppointmentBookingRequest request) {
        // Validate doctor exists
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
            .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        // Validate patient exists
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        // Check if time slot is available
        if (appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(
                doctor, request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new RuntimeException("Time slot is already booked");
        }
        
        // Validate appointment date is not in the past
        if (request.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot book appointment for past dates");
        }
        
        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReasonForVisit(request.getReasonForVisit());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        
        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        // Mark time slot as unavailable
        markTimeSlotUnavailable(doctor, request.getAppointmentDate(), request.getAppointmentTime());
        
        return convertToResponse(savedAppointment);
    }
    
    public List<AvailableTimeSlotResponse> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        // Get available time slots for the doctor on the specified date
        List<TimeSlot> availableSlots = timeSlotRepository
            .findByDoctorAndSlotDateAndIsAvailable(doctor, date, true);
        
        // Filter out slots that are already booked
        List<Appointment> existingAppointments = appointmentRepository
            .findByDoctorAndAppointmentDate(doctor, date);
        
        List<LocalTime> bookedTimes = existingAppointments.stream()
            .map(Appointment::getAppointmentTime)
            .collect(Collectors.toList());
        
        return availableSlots.stream()
            .filter(slot -> !bookedTimes.contains(slot.getStartTime()))
            .map(this::convertToTimeSlotResponse)
            .collect(Collectors.toList());
    }
    
    public List<AppointmentResponse> getPatientAppointments(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        List<Appointment> appointments = appointmentRepository.findByPatient(patient);
        
        return appointments.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    public List<AppointmentResponse> getDoctorAppointments(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        List<Appointment> appointments = appointmentRepository
            .findByDoctorAndAppointmentDate(doctor, date);
        
        return appointments.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        appointment.setStatus(status);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        
        return convertToResponse(updatedAppointment);
    }
    
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel a completed appointment");
        }
        
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        
        // Mark time slot as available again
        markTimeSlotAvailable(appointment.getDoctor(), 
            appointment.getAppointmentDate(), 
            appointment.getAppointmentTime());
    }
    
    public List<AppointmentResponse> getAppointmentsByStatus(AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByAppointmentDateAndStatus(
            LocalDate.now(), status);
        
        return appointments.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    public List<AppointmentResponse> getUpcomingAppointments(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        List<Appointment> appointments = appointmentRepository
            .findByPatientAndStatus(patient, AppointmentStatus.SCHEDULED);
        
        return appointments.stream()
            .filter(appointment -> appointment.getAppointmentDate().isAfter(LocalDate.now()) ||
                    (appointment.getAppointmentDate().isEqual(LocalDate.now()) && 
                     appointment.getAppointmentTime().isAfter(LocalTime.now())))
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    private void markTimeSlotUnavailable(Doctor doctor, LocalDate date, LocalTime time) {
        List<TimeSlot> timeSlots = timeSlotRepository.findByDoctorAndSlotDate(doctor, date);
        
        for (TimeSlot slot : timeSlots) {
            if (slot.getStartTime().equals(time)) {
                slot.setIsAvailable(false);
                timeSlotRepository.save(slot);
                break;
            }
        }
    }
    
    private void markTimeSlotAvailable(Doctor doctor, LocalDate date, LocalTime time) {
        List<TimeSlot> timeSlots = timeSlotRepository.findByDoctorAndSlotDate(doctor, date);
        
        for (TimeSlot slot : timeSlots) {
            if (slot.getStartTime().equals(time)) {
                slot.setIsAvailable(true);
                timeSlotRepository.save(slot);
                break;
            }
        }
    }
    
    private AppointmentResponse convertToResponse(Appointment appointment) {
        AppointmentResponse.PatientInfo patientInfo = new AppointmentResponse.PatientInfo(
            appointment.getPatient().getId(),
            appointment.getPatient().getPatientId(),
            appointment.getPatient().getUser().getFirstName(),
            appointment.getPatient().getUser().getLastName(),
            appointment.getPatient().getUser().getEmail(),
            appointment.getPatient().getUser().getPhoneNumber()
        );
        
        AppointmentResponse.DoctorInfo doctorInfo = new AppointmentResponse.DoctorInfo(
            appointment.getDoctor().getId(),
            appointment.getDoctor().getUser().getFirstName(),
            appointment.getDoctor().getUser().getLastName(),
            appointment.getDoctor().getSpecialization(),
            appointment.getDoctor().getDepartment(),
            appointment.getDoctor().getConsultationFee()
        );
        
        return new AppointmentResponse(
            appointment.getId(),
            patientInfo,
            doctorInfo,
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime(),
            appointment.getDurationMinutes(),
            appointment.getStatus(),
            appointment.getReasonForVisit(),
            appointment.getNotes(),
            appointment.getCreatedAt()
        );
    }
    
    private AvailableTimeSlotResponse convertToTimeSlotResponse(TimeSlot timeSlot) {
        return new AvailableTimeSlotResponse(
            timeSlot.getStartTime(),
            timeSlot.getEndTime(),
            timeSlot.getSlotDurationMinutes(),
            timeSlot.getIsAvailable()
        );
    }
}
