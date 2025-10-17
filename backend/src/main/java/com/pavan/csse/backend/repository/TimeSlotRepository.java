package com.pavan.csse.backend.repository;

import com.pavan.csse.backend.model.Doctor;
import com.pavan.csse.backend.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByDoctorAndSlotDateAndIsAvailable(Doctor doctor, LocalDate date, Boolean isAvailable);
    List<TimeSlot> findByDoctorAndSlotDateBetween(Doctor doctor, LocalDate startDate, LocalDate endDate);
    List<TimeSlot> findByDoctorAndSlotDate(Doctor doctor, LocalDate date);
}
