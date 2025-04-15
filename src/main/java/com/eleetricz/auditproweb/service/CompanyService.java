package com.eleetricz.auditproweb.service;

import com.eleetricz.auditproweb.model.Company;

import java.util.List;

public interface CompanyService {
    List<Company> findAll();
    Company findById(Long id);
}
