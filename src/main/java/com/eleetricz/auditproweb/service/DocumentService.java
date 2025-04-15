package com.eleetricz.auditproweb.service;

import com.eleetricz.auditproweb.model.Document;

import java.util.List;

public interface DocumentService {
    List<Document> findByEmployee(Long employeeId);
    Document findById(Long id);
}
