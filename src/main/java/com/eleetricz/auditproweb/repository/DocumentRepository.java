package com.eleetricz.auditproweb.repository;

import com.eleetricz.auditproweb.model.Company;
import com.eleetricz.auditproweb.model.Document;
import com.eleetricz.auditproweb.model.DocumentType;
import com.eleetricz.auditproweb.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByEmployeeId(Long employeeId);
    @Query("SELECT d FROM Document d WHERE d.employee.id = :employeeId " +
            "AND (:year IS NULL OR d.year = :year) " +
            "AND (:month IS NULL OR d.month = :month) " +
            "AND (:type IS NULL OR d.type = :type)")
    List<Document> findByEmployeeAndFilters(@Param("employeeId") long employeeId,
                                            @Param("year") Integer year,
                                            @Param("month") Integer month,
                                            @Param("type")DocumentType type);
    Long countByEmployeeId(Long employeeId);
    List<Document> findByCompanyId(Long companyId);
    boolean existsByCompanyAndEmployeeAndYearAndMonthAndType(Company company, Employee employee, int year, int month, DocumentType type);
    boolean existsByCompanyAndEmployeeIsNullAndYearAndMonthAndType(Company company, int year, int month, DocumentType type);

}
