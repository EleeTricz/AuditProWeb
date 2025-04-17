package com.eleetricz.auditproweb.service;



import com.eleetricz.auditproweb.dto.AuditResume;
import com.eleetricz.auditproweb.model.Company;

import java.util.List;
import java.util.Map;

public interface AuditService {
    Map<String, List<String>> listMissingDocumentsByEmployee(Company company);
    List<String> listMissingFolhaByCompany(Company company);
    Map<String, Double> calculatePresencePercentageByEmployee(Company company);
    double calculateFolhaPercentageByCompany(Company company);
    List<AuditResume> getAuditResumeByEmployee(Company company);



}
