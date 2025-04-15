package com.eleetricz.auditproweb.service;

import com.eleetricz.auditproweb.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findByCompany(Long companyId);
    Employee findById(Long id);
}
