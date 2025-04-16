package com.eleetricz.auditproweb.service;

import com.eleetricz.auditproweb.model.Document;
import com.eleetricz.auditproweb.model.DocumentType;

import java.util.List;

public interface DocumentService {
    List<Document> findByEmployee(Long employeeId);
    Document findById(Long id);
    List<Document> findByEmployeeWithFilters(Long employeeId, Integer year, Integer month, DocumentType type);
}
