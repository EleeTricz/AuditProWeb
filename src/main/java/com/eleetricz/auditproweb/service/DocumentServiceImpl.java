package com.eleetricz.auditproweb.service;

import com.eleetricz.auditproweb.model.Document;
import com.eleetricz.auditproweb.model.DocumentType;
import com.eleetricz.auditproweb.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService{
    private final DocumentRepository repo;
    public DocumentServiceImpl(DocumentRepository repo){
        this.repo = repo;
    }

    @Override
    public List<Document> findByEmployee(Long employeeId) {
        return repo.findByEmployeeId(employeeId);
    }

    @Override
    public Document findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));
    }

    @Override
    public List<Document> findByEmployeeWithFilters(Long employeeId, Integer year, Integer month, DocumentType type) {
        return repo.findByEmployeeAndFilters(employeeId, year, month, type);
    }

    @Override
    public Long countByEmployeeId(Long employeeId) {
        return repo.countByEmployeeId(employeeId);
    }
}
