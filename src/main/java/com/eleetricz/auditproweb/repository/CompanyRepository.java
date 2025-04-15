package com.eleetricz.auditproweb.repository;

import com.eleetricz.auditproweb.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {}
