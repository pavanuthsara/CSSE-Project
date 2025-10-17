package com.pavan.csse.backend.repository;

import com.pavan.csse.backend.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUserId(Long userId);
    Optional<Staff> findByEmployeeId(String employeeId);
    List<Staff> findByPosition(String position);
    List<Staff> findByDepartment(String department);
    List<Staff> findBySupervisorId(Long supervisorId);
    boolean existsByEmployeeId(String employeeId);
}