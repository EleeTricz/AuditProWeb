package com.eleetricz.auditproweb.service;

import com.eleetricz.auditproweb.model.Employee;
import com.eleetricz.auditproweb.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository repo;
    public EmployeeServiceImpl(EmployeeRepository repo){
        this.repo = repo;
    }

    @Override
    public List<Employee> findByCompany(Long companyId){
        return repo.findByCompanyId(companyId);
    }

    @Override
    public Employee findById(Long id){
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));
    }
}
