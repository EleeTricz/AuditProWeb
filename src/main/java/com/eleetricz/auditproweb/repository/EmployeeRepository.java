package com.eleetricz.auditproweb.repository;

import com.eleetricz.auditproweb.model.Company;
import com.eleetricz.auditproweb.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByCompanyId(Long companyId);
    int countByCompany(Company company);
    Employee findByCompanyAndNameIgnoreCase(Company company, String name);
    @Query("SELECT MIN(e.admissionDate) FROM Employee e WHERE e.company = :company AND LOWER(e.name) <> 'todos' AND e.admissionDate IS NOT NULL")
    Optional<LocalDate> findOldestAdmissionDateExcludingTodos(@Param("company") Company company);


}
