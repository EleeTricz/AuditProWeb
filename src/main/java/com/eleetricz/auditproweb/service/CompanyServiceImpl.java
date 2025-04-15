package com.eleetricz.auditproweb.service;

import com.eleetricz.auditproweb.model.Company;
import com.eleetricz.auditproweb.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService{
    private final CompanyRepository repo;
    public CompanyServiceImpl(CompanyRepository repo){
        this.repo = repo;
    }

    @Override
    public List<Company> findAll(){
        return repo.findAll();
    }

    @Override
    public Company findById(Long id){
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }

}
